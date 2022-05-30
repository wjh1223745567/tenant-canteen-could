package com.iotinall.canteen.service;

import com.iotinall.canteen.common.constant.TenantOrganizationTypeEnum;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.dto.storehouse.FeignTenantSubOrganizationDto;
import com.iotinall.canteen.dto.tenantorg.TenantOrganizationView;
import com.iotinall.canteen.dto.tenantsuborginfo.*;
import com.iotinall.canteen.entity.TenantOrganization;
import com.iotinall.canteen.entity.TenantSubOrgInfo;
import com.iotinall.canteen.repository.TenantSubOrgInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TenantSubOrgInfoService {

    @Resource
    private TenantSubOrgInfoRepository tenantSubOrgInfoRepository;

    @Resource
    private TenantOrganizationService tenantOrganizationService;

    @Resource
    private FeignFinConsumeSettingService feignFinConsumeSettingService;

    /**
     * 查询当前用户管理的子食堂
     */
    public PageDTO<TenantSubOrgInfoDto> page(TenantSubOrgInfoCondition condition, Pageable pageable) {
        Set<Long> menuIds = new HashSet<>();
        menuIds.add(tenantOrganizationService.findMenuId());
        Set<Long> childless = tenantOrganizationService.findAllChildren(menuIds);
        menuIds.addAll(childless);
        if (menuIds.isEmpty()) {
            return PageUtil.empPage();
        }
        SpecificationBuilder builder = SpecificationBuilder.builder()
                .where(Criterion.in("tenantOrgId", menuIds));
        if (StringUtils.isNotBlank(condition.getKeyword())) {
            builder.whereByOr(
                    Criterion.like("name", condition.getKeyword()),
                    Criterion.like("address", condition.getKeyword())
            );
        }

        Page<TenantSubOrgInfoDto> page = tenantSubOrgInfoRepository.findAll(builder.build(), pageable).map(item -> {
            TenantSubOrgInfoDto orgInfoDto = new TenantSubOrgInfoDto()
                    .setName(item.getName())
                    .setAddress(item.getAddress())
                    .setCapacity(item.getCapacity());
            orgInfoDto.setId(item.getId())
                    .setRemark(item.getRemark());
            return orgInfoDto;
        });
        return PageUtil.toPageDTO(page);
    }

    /**
     * 查询当前用户管理的子食堂
     */
    public List<TenantSubOrgInfoDto> all() {
        Set<Long> menuIds = new HashSet<>();
        menuIds.add(tenantOrganizationService.findMenuId());
        Set<Long> childless = tenantOrganizationService.findAllChildren(menuIds);
        menuIds.addAll(childless);
        SpecificationBuilder builder = SpecificationBuilder.builder()
                .where(Criterion.in("tenantOrgId", menuIds));

        return tenantSubOrgInfoRepository.findAll(builder.build()).stream().map(item -> {
            TenantSubOrgInfoDto orgInfoDto = new TenantSubOrgInfoDto()
                    .setName(item.getName())
                    .setAddress(item.getAddress())
                    .setCapacity(item.getCapacity());
            orgInfoDto.setId(item.getId())
                    .setRemark(item.getRemark());
            return orgInfoDto;
        }).collect(Collectors.toList());
    }

    /**
     * 查询所有子食堂
     *
     * @return
     */
    public List<TenantSubOrgInfoDto> findAll(TenantSubOrgInfoCondition condition) {
        SpecificationBuilder builder = SpecificationBuilder.builder();
        if (StringUtils.isNotBlank(condition.getKeyword())) {
            builder.whereByOr(
                    Criterion.like("name", condition.getKeyword()),
                    Criterion.like("address", condition.getKeyword())
            );
        }
        return this.tenantSubOrgInfoRepository.findAll(builder.build()).stream().map(item -> {
            TenantSubOrgInfoDto subOrgInfoDto = new TenantSubOrgInfoDto()
                    .setId(item.getId())
                    .setAddress(item.getAddress())
                    .setCapacity(item.getCapacity())
                    .setName(item.getName())
                    .setRemark(item.getRemark());
            TenantSubOrgBusDto orgBusDto = findBusinessHouses(item.getTenantOrgId());
            subOrgInfoDto.setBusinessHours(orgBusDto.getBusinessHours());
            if(StringUtils.isBlank(orgBusDto.getDataSource())){
                return null;
            }
            subOrgInfoDto.setDataSource(orgBusDto.getDataSource());
            if (item.getLatitude() != null && item.getLongitude() != null && condition.getLatitude() != null && condition.getLongitude() != null) {
                subOrgInfoDto.setDistance(calculateTheDistance(condition.getLongitude().doubleValue(), condition.getLatitude().doubleValue(), item.getLongitude().doubleValue(), item.getLatitude().doubleValue()));
            }
            return subOrgInfoDto;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * @param longitudeFrom 起点经度
     * @param latitudeFrom  起点纬度
     * @param longitudeTo   终点经度
     * @param latitudeTo    终点纬度
     * @return
     */
    private BigDecimal calculateTheDistance(double longitudeFrom, double latitudeFrom, double longitudeTo, double latitudeTo) {
        GlobalCoordinates source = new GlobalCoordinates(latitudeFrom, longitudeFrom);
        GlobalCoordinates target = new GlobalCoordinates(latitudeTo, longitudeTo);
        long distanceMeter = Math.round(getDistanceMeter(target, source, Ellipsoid.Sphere));
        return BigDecimal.valueOf(distanceMeter);
    }

    /**
     * 计算经纬度距离
     *
     * @param gpsFrom
     * @param gpsTo
     * @param ellipsoid
     * @return
     */
    private double getDistanceMeter(GlobalCoordinates gpsFrom, GlobalCoordinates gpsTo, Ellipsoid ellipsoid) {
        // 创建GeodeticCalculator，调用计算方法，传入坐标系、经纬度用于计算距离
        GeodeticCurve geoCurve = new GeodeticCalculator().calculateGeodeticCurve(ellipsoid, gpsFrom, gpsTo);
        return geoCurve.getEllipsoidalDistance();
    }

    /**
     * 子食堂ID
     *
     * @param id
     * @return
     */
    public TenantSubOrgInfoView view(Long id, BigDecimal longitude, BigDecimal latitude) {
        TenantSubOrgInfo subOrgInfo = this.tenantSubOrgInfoRepository.findById(id).orElseThrow(() -> new BizException("", "未找到子食堂信息"));
        TenantSubOrgInfoView subOrgInfoView = new TenantSubOrgInfoView()
                .setId(subOrgInfo.getId())
                .setAddress(subOrgInfo.getAddress())
                .setCapacity(subOrgInfo.getCapacity())
                .setContactPerson(subOrgInfo.getContactPerson())
                .setForTheCrowd(subOrgInfo.getForTheCrowd())
                .setLatitude(subOrgInfo.getLatitude())
                .setLongitude(subOrgInfo.getLongitude())
                .setName(subOrgInfo.getName())
                .setPhone(subOrgInfo.getPhone())
                .setRemark(subOrgInfo.getRemark());

        if (subOrgInfo.getLatitude() != null && subOrgInfo.getLongitude() != null && latitude != null && longitude != null) {
            subOrgInfoView.setDistance(calculateTheDistance(longitude.doubleValue(), latitude.doubleValue(), subOrgInfo.getLongitude().doubleValue(), subOrgInfo.getLatitude().doubleValue()));
        }

        subOrgInfoView.setBusinessHours(findBusinessHouses(subOrgInfo.getTenantOrgId()).getBusinessHours());
        return subOrgInfoView;
    }

    private TenantSubOrgBusDto findBusinessHouses(Long tenantOrgId) {
        TenantOrganizationView organizationView = this.tenantOrganizationService.findById(tenantOrgId);
        String dataSource = "";
        if (Objects.equals(organizationView.getType(), TenantOrganizationTypeEnum.DINING_HALL.getCode())) {
            dataSource = organizationView.getDataSourceKey();
        } else if (organizationView.getPid() != null) {
            TenantOrganizationView parentView = this.tenantOrganizationService.findById(organizationView.getPid());
            if (Objects.equals(parentView.getType(), TenantOrganizationTypeEnum.DINING_HALL.getCode())) {
                dataSource = parentView.getDataSourceKey();
            }
        }
        TenantSubOrgBusDto tenantSubOrgBusDto = new TenantSubOrgBusDto();
        if (StringUtils.isNotBlank(dataSource)) {
            tenantSubOrgBusDto.setBusinessHours(feignFinConsumeSettingService.mealTime(dataSource));
            tenantSubOrgBusDto.setDataSource(dataSource);
        }
        return tenantSubOrgBusDto;
    }

    /**
     * 保存
     *
     * @param req
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(TenantSubOrgInfoReq req) {
        TenantSubOrgInfo tenantSubOrgInfo = new TenantSubOrgInfo()
                .setName(req.getName())
                .setAddress(req.getAddress())
                .setCapacity(req.getCapacity())
                .setContactPerson(req.getContactPerson())
                .setForTheCrowd(req.getForTheCrowd())
                .setLatitude(req.getLatitude())
                .setLongitude(req.getLongitude())
                .setPhone(req.getPhone());
        tenantSubOrgInfo.setId(req.getId())
                .setRemark(req.getRemark());
        this.tenantSubOrgInfoRepository.save(tenantSubOrgInfo);
    }

    /**
     * 删除
     *
     * @param tenantOrgId
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleted(Long tenantOrgId) {
        List<TenantSubOrgInfo> tenantSubOrgInfoList = this.tenantSubOrgInfoRepository.findByTenantOrgId(tenantOrgId);
        this.tenantSubOrgInfoRepository.deleteAll(tenantSubOrgInfoList);
    }

    /**
     * 根据租户信息创建子食堂
     */
    @Transactional(rollbackFor = Exception.class)
    public void addDefault(TenantOrganization tenantOrganization) {
        //是否是食堂或子食堂
        Boolean flag = Boolean.FALSE;

        List<TenantSubOrgInfo> tenantSubOrgInfos = this.tenantSubOrgInfoRepository.findByTenantOrgId(tenantOrganization.getId());
        if (Objects.equals(tenantOrganization.getType(), TenantOrganizationTypeEnum.DINING_HALL.getCode())) {
            flag = Boolean.TRUE;
        } else if (tenantOrganization.getPid() != null) {
            TenantOrganizationView tenantOrganizationView = this.tenantOrganizationService.findById(tenantOrganization.getPid());
            if (tenantOrganizationView != null && Objects.equals(tenantOrganizationView.getType(), TenantOrganizationTypeEnum.DINING_HALL.getCode())) {
                flag = Boolean.TRUE;
            }
        }

        if(flag && tenantSubOrgInfos.isEmpty()){
            TenantSubOrgInfo tenantSubOrgInfo = new TenantSubOrgInfo()
                    .setName(tenantOrganization.getName() + "的子食堂")
                    .setAddress("默认")
                    .setCapacity(1)
                    .setLatitude(BigDecimal.ZERO)
                    .setLongitude(BigDecimal.ZERO)
                    .setTenantOrgId(tenantOrganization.getId());
            this.tenantSubOrgInfoRepository.save(tenantSubOrgInfo);
        }
        if(!flag && !tenantSubOrgInfos.isEmpty()){
            this.tenantSubOrgInfoRepository.deleteAll(tenantSubOrgInfos);
        }
    }

    /**
     * 通过ID获取就餐点
     *
     * @author loki
     * @date 2021/7/28 16:32
     **/
    public FeignTenantSubOrganizationDto findById(Long id) {
        TenantSubOrgInfo tenantSubOrgInfo = this.tenantSubOrgInfoRepository.findById(id).orElseThrow(() -> new BizException("数据不存在"));
        FeignTenantSubOrganizationDto result = new FeignTenantSubOrganizationDto();
        BeanUtils.copyProperties(tenantSubOrgInfo,result);
        return result;
    }
}
