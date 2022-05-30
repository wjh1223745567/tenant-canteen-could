package com.iotinall.canteen.service;

import com.iotinall.canteen.common.constant.TenantOrganizationTypeEnum;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.DataSourceInfoDTO;
import com.iotinall.canteen.common.protocol.SimpDataSource;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.common.tenant.TenantUser;
import com.iotinall.canteen.common.tenant.TenantUserRepository;
import com.iotinall.canteen.common.util.SpringContextUtil;
import com.iotinall.canteen.dto.role.FeignRoleAddReq;
import com.iotinall.canteen.dto.storehouse.FeignTenantOrganizationDto;
import com.iotinall.canteen.dto.tenantorg.TenantOrganizationReq;
import com.iotinall.canteen.dto.tenantorg.TenantOrganizationTreeDto;
import com.iotinall.canteen.dto.tenantorg.TenantOrganizationView;
import com.iotinall.canteen.entity.TenantOrganization;
import com.iotinall.canteen.repository.TenantOrganizationRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TenantOrganizationService {

    @Resource
    private TenantOrganizationRepository tenantOrganizationRepository;

    @Resource
    private TenantUserRepository tenantUserRepository;

    @Resource
    private FeignRoleService feignRoleService;

    /**
     * 查询所有树数据
     *
     * @return
     */
    public List<TenantOrganizationTreeDto> tree(Long id) {
        SpecificationBuilder specificationBuilder = SpecificationBuilder.builder();
        if (id != null) {
            specificationBuilder.where(
                    Criterion.ne("id", id)
            );
        }

        List<TenantOrganization> tenantOrganizations = this.tenantOrganizationRepository.findAll(specificationBuilder.build());

        Map<Long, TenantOrganizationTreeDto> map = new HashMap<>();
        for (TenantOrganization tenantOrganization : tenantOrganizations) {
            TenantOrganizationTreeDto treeDto = new TenantOrganizationTreeDto()
                    .setId(tenantOrganization.getId())
                    .setName(tenantOrganization.getName())
                    .setType(TenantOrganizationTypeEnum.findByCode(tenantOrganization.getType()).getName())
                    .setDataSourceKey(tenantOrganization.getDataSourceKey())
                    .setPid(tenantOrganization.getPid())
                    .setChildren(new ArrayList<>());
            map.put(tenantOrganization.getId(), treeDto);
        }

        List<TenantOrganizationTreeDto> treeDtos = new ArrayList<>();

        for (TenantOrganizationTreeDto value : map.values()) {
            if (value.getPid() == null) {
                treeDtos.add(value);
            } else {
                TenantOrganizationTreeDto parent = map.get(value.getPid());
                if (parent != null) {
                    parent.getChildren().add(value);
                }
            }
        }

        return treeDtos;
    }

    public Map<Long, FeignTenantOrganizationDto> findByIds(Set<Long> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        List<TenantOrganization> organizations = this.tenantOrganizationRepository.findAllById(ids);
        Map<Long, FeignTenantOrganizationDto> map = new HashMap<>();
        for (TenantOrganization organization : organizations) {
            FeignTenantOrganizationDto view = new FeignTenantOrganizationDto()
                    .setName(organization.getName())
                    .setType(organization.getType())
                    .setDataSourceKey(organization.getDataSourceKey())
                    .setPid(organization.getPid());
            view.setId(organization.getId());
            map.put(view.getId(), view);
        }
        return map;
    }

    public Map<Long, FeignTenantOrganizationDto> findAll() {
        List<TenantOrganization> organizations = this.tenantOrganizationRepository.findAll();
        Map<Long, FeignTenantOrganizationDto> map = new HashMap<>();
        for (TenantOrganization organization : organizations) {
            FeignTenantOrganizationDto view = new FeignTenantOrganizationDto()
                    .setName(organization.getName())
                    .setType(organization.getType())
                    .setDataSourceKey(organization.getDataSourceKey())
                    .setPid(organization.getPid());
            view.setId(organization.getId());
            map.put(view.getId(), view);
        }
        return map;
    }

    public TenantOrganizationView findById(Long id) {
        TenantOrganization organization = this.tenantOrganizationRepository.findById(id).orElseThrow(() -> new BizException("", "未找到租户数据" + id));
        TenantOrganizationView view = new TenantOrganizationView()
                .setName(organization.getName())
                .setType(organization.getType())
                .setDataSourceKey(organization.getDataSourceKey())
                .setPid(organization.getPid());
        view.setId(organization.getId());
        return view;
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(TenantOrganizationReq req) {
        TenantOrganization tenantOrganization = new TenantOrganization()
                .setName(req.getName())
                .setType(req.getType())
                .setDataSourceKey(req.getDataSourceKey())
                .setPid(req.getPid());
        List<Long> allPids = new ArrayList<>();
        findAllPids(allPids, req.getPid());
        if (!allPids.isEmpty()) {
            tenantOrganization.setAllPids(allPids.stream().map(String::valueOf).collect(Collectors.joining("|")));
        }
        tenantOrganization.setLevel(allPids.size());
        tenantOrganization.setId(req.getId());
        this.tenantOrganizationRepository.save(tenantOrganization);
        TenantSubOrgInfoService subOrgInfoService = SpringContextUtil.getBean(TenantSubOrgInfoService.class);
        subOrgInfoService.addDefault(tenantOrganization);

        //修改数据源类型
        TenantUser tenantUser = tenantUserRepository.findBySource(req.getDataSourceKey());
        if (tenantUser != null) {
            tenantUser.setType(req.getType());
            tenantUserRepository.save(tenantUser);
        }

        //生成默认的角色
        if (req.getId() == null) {
            FeignRoleAddReq feignRoleAddReq = new FeignRoleAddReq();
            feignRoleAddReq.setName(getDefaultRoleName(req.getPid(), req.getName()));
            feignRoleAddReq.setTenantOrgId(tenantOrganization.getId());
            feignRoleService.createTenantDefaultRole(feignRoleAddReq);
        }
    }

    /**
     * 获取默认角色名
     *
     * @author loki
     * @date 2021/7/16 16:18
     **/
    private String getDefaultRoleName(Long parentTenantOrgId, String name) {
        if (null == parentTenantOrgId) {
            return name;
        }

        TenantOrganization tenantOrganization = this.tenantOrganizationRepository.findById(parentTenantOrgId).orElse(null);
        if (null != tenantOrganization) {
            name = tenantOrganization.getName() + name;
        }
        return name;
    }

    /**
     * 查询所有子租户ID
     *
     * @param ids
     * @return
     */
    public Set<Long> findAllChildren(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptySet();
        }
        Specification<TenantOrganization> specification = SpecificationBuilder.builder()
                .whereByOr(ids.stream().map(id -> Criterion.like("allPids", id)).collect(Collectors.toList()))
                .build();
        List<TenantOrganization> tenantOrganizations = this.tenantOrganizationRepository.findAll(specification);
        return tenantOrganizations.stream().map(TenantOrganization::getId).collect(Collectors.toSet());
    }

    private void findAllPids(List<Long> allPids, Long pid) {
        if (pid != null) {
            allPids.add(pid);
            this.tenantOrganizationRepository.findById(pid).ifPresent(item -> {
                findAllPids(allPids, item.getPid());
            });
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleted(Set<Long> ids) {
        TenantSubOrgInfoService tenantSubOrgInfoService = SpringContextUtil.getBean(TenantSubOrgInfoService.class);
        Set<Long> set = new HashSet<>(this.findAllChildren(ids));
        set.addAll(ids);

        List<TenantOrganization> tenantOrganizations = set.stream().map(item -> {
            TenantOrganization tenantOrganization = new TenantOrganization();
            tenantOrganization.setId(item);
            tenantSubOrgInfoService.deleted(item);
            return tenantOrganization;
        }).collect(Collectors.toList());

        this.tenantOrganizationRepository.deleteInBatch(tenantOrganizations);
    }

    /**
     * 查询当前用户餐厅所使用的库存库
     *
     * @return
     */
    public List<SimpDataSource> findStockDataSource() {
        DataSourceInfoDTO dataSourceInfoDTO = SecurityUtils.getCurrentUser().getSourceInfo();
        if (CollectionUtils.isEmpty(dataSourceInfoDTO.getAllMenu()) || StringUtils.isBlank(dataSourceInfoDTO.getMenu())) {
            throw new BizException("", "当前用户无餐厅");
        }
        SimpDataSource simpDataSource = dataSourceInfoDTO.getAllMenu().stream().filter(item -> Objects.equals(item.getDataSourceKey(), dataSourceInfoDTO.getMenu())).findFirst().orElseThrow(() -> new BizException("", "当前用户无餐厅"));
        TenantOrganization tenantOrganization = this.tenantOrganizationRepository.findById(simpDataSource.getId()).orElseThrow(() -> new BizException("", "未找到食堂信息"));
        if (tenantOrganization.getPid() == null) {
            throw new BizException("", "非餐厅层级");
        }
        Long pid = tenantOrganization.getPid();
        Specification<TenantOrganization> specification = SpecificationBuilder.builder()
                .where(
                        Criterion.like("allPids", pid),
                        Criterion.eq("type", TenantOrganizationTypeEnum.INVENTORY.getCode())
                ).build();
        List<TenantOrganization> tenantOrganizations = this.tenantOrganizationRepository.findAll(specification);
        List<SimpDataSource> simpDataSources = tenantOrganizations.stream().map(item -> new SimpDataSource()
                .setId(item.getId())
                .setName(item.getName())
                .setDataSourceKey(item.getDataSourceKey())).collect(Collectors.toList());
        return simpDataSources;
    }

    /**
     * 查询当前库存库使用的餐厅
     *
     * @return
     */
    public String findMenuDataSource() {
        DataSourceInfoDTO dataSourceInfoDTO = SecurityUtils.getCurrentUser().getSourceInfo();
        if (CollectionUtils.isEmpty(dataSourceInfoDTO.getAllStock()) || StringUtils.isBlank(dataSourceInfoDTO.getStock())) {
            throw new BizException("", "当前用户无管理库存");
        }
        SimpDataSource simpDataSource = dataSourceInfoDTO.getAllStock().stream().filter(item -> Objects.equals(item.getDataSourceKey(), dataSourceInfoDTO.getStock())).findFirst().orElseThrow(() -> new BizException("", "当前用户无管理库存"));
        TenantOrganization tenantOrganization = this.tenantOrganizationRepository.findById(simpDataSource.getId()).orElseThrow(() -> new BizException("", "未找到库存信息"));
        if (tenantOrganization.getPid() == null) {
            throw new BizException("", "非库存层级");
        }
        Long pid = tenantOrganization.getPid();
        Specification<TenantOrganization> specification = SpecificationBuilder.builder()
                .where(
                        Criterion.like("allPids", pid),
                        Criterion.eq("type", TenantOrganizationTypeEnum.DINING_HALL.getCode())
                ).build();
        List<TenantOrganization> tenantOrganizations = this.tenantOrganizationRepository.findAll(specification);
        if (tenantOrganizations.isEmpty()) {
            throw new BizException("", "未找到食堂信息");
        }
        return tenantOrganizations.get(0).getDataSourceKey();
    }

    /**
     * 查询当前食堂租户ID
     *
     * @return
     */
    public Long findMenuId() {
        DataSourceInfoDTO dataSourceInfoDTO = SecurityUtils.getCurrentUser().getSourceInfo();
        if (CollectionUtils.isEmpty(dataSourceInfoDTO.getAllMenu()) || StringUtils.isBlank(dataSourceInfoDTO.getMenu())) {
            throw new BizException("", "当前用户无管理餐厅");
        }
        List<TenantOrganization> tenantOrganizations = this.tenantOrganizationRepository.findByDataSourceAndType(dataSourceInfoDTO.getMenu());
        if (!tenantOrganizations.isEmpty()) {
            //多个只获取第一个
            return tenantOrganizations.get(0).getId();
        } else {
            return null;
        }
    }

    /**
     * 获取当前用户最高级别权限租户信息
     *
     * @return
     */
    public Set<Long> findTenantHighestChildren() {
        if (SecurityUtils.getCurrentUser().isSupperAdmin()) {
            return this.tenantOrganizationRepository.findAll().stream().map(TenantOrganization::getId).collect(Collectors.toSet());
        }
        Set<Long> roleTenantOrgIds = SecurityUtils.getCurrentUser().getRoleTenantOrgIds();
        if (CollectionUtils.isEmpty(roleTenantOrgIds)) {
            throw new BizException("", "当前用户无管理数据权限");
        }
        Set<Long> ids = this.tenantOrganizationRepository.findTenantHighestChildren(roleTenantOrgIds);
        if (ids.isEmpty()) {
            return Collections.emptySet();
        }
        Set<Long> allIds = new HashSet<>(findAllChildren(ids));
        allIds.addAll(ids);
        return allIds;
    }

    public Long findTenantHighest() {
        Set<Long> roleTenantOrgIds = SecurityUtils.getCurrentUser().getRoleTenantOrgIds();
        if (CollectionUtils.isEmpty(roleTenantOrgIds)) {
            return null;
        }
        return this.tenantOrganizationRepository.findTenantHighestChildren(roleTenantOrgIds).stream().findFirst().orElse(null);
    }

    public List<FeignTenantOrganizationDto> findAllCanteen() {
        Specification<TenantOrganization> specification = SpecificationBuilder.builder()
                .where(
                        Criterion.eq("type", TenantOrganizationTypeEnum.DINING_HALL.getCode())
                ).build();
        List<TenantOrganization> tenantOrganizations = this.tenantOrganizationRepository.findAll(specification);
        List<FeignTenantOrganizationDto> simpDataSources = tenantOrganizations.stream().map(item -> new FeignTenantOrganizationDto()
                .setId(item.getId())
                .setName(item.getName())
                .setDataSourceKey(item.getDataSourceKey())).collect(Collectors.toList());
        return simpDataSources;
    }
}
