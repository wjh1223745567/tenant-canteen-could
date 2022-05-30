package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.util.ImgPair;
import com.iotinall.canteen.constant.Constants;
import com.iotinall.canteen.constant.MealTypeEnum;
import com.iotinall.canteen.dto.assessrecord.AssessRecordContentReq;
import com.iotinall.canteen.dto.assessrecord.AssessSampleListDto;
import com.iotinall.canteen.dto.kitchen.FeignKitchenSampleDTO;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.dto.sample.*;
import com.iotinall.canteen.entity.KitchenItem;
import com.iotinall.canteen.entity.KitchenItemEmployee;
import com.iotinall.canteen.entity.KitchenSample;
import com.iotinall.canteen.repository.KitchenItemRepository;
import com.iotinall.canteen.repository.KitchenSampleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 留样管理 ServiceImpl
 *
 * @author xinbing
 * @date 2020-07-06 17:09:03
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class KitchenSampleService {
    @Resource
    private KitchenSampleRepository kitchenSampleRepository;
    @Resource
    private FeignEmployeeService feignEmployeeService;
    @Resource
    private KitchenResultService kitchenResultService;
    @Resource
    private KitchenItemRepository itemRepository;

    public PageDTO<KitchenSampleDTO> list(KitchenSampleCriteria criteria, Pageable pageable) {
        Specification<KitchenSample> spec = SpecificationBuilder.builder()
                .where(Criterion.eq("mealType", criteria.getMealType() == null ? null : MealTypeEnum.byCode(criteria.getMealType())))
                .where(Criterion.gte("recordTime", criteria.getBeginRecordTime() == null ? null : criteria.getBeginRecordTime().atStartOfDay()))
                .where(Criterion.lt("recordTime", criteria.getEndRecordTime() == null ? null : criteria.getEndRecordTime().atTime(LocalTime.MAX)))
                .where(Criterion.eq("state", criteria.getState()))
                .where(Criterion.like("foods", criteria.getFoodsName()))
                .whereByOr(
                        Criterion.like("dutyEmployees.name", criteria.getKeywords())
                )
                .build(true);
        Page<KitchenSample> page = kitchenSampleRepository.findAll(spec, pageable);
        List<KitchenSampleDTO> list = page.getContent().stream().map(this::toKitchenSampleDTO).collect(Collectors.toList());
        return PageUtil.toPageDTO(list, page);
    }

    private KitchenSampleDTO toKitchenSampleDTO(KitchenSample sample) {
        KitchenSampleDTO kitchenSampleDTO = new KitchenSampleDTO();
        // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
        BeanUtils.copyProperties(sample, kitchenSampleDTO);

        if (null != sample.getItem() && !CollectionUtils.isEmpty(sample.getItem().getDutyEmployeeList())) {
            List<KitchenSampleDutyEmpDTO> kitchenSampleDutyEmpDTOS = new ArrayList<>();
            KitchenSampleDutyEmpDTO kitchenSampleDutyEmpDTO;
            FeignEmployeeDTO employeeDTO;
            for (KitchenItemEmployee employee : sample.getItem().getDutyEmployeeList()) {
                employeeDTO = feignEmployeeService.findById(employee.getEmpId());
                if (null == employeeDTO) {
                    continue;
                }

                kitchenSampleDutyEmpDTO = new KitchenSampleDutyEmpDTO();
                kitchenSampleDutyEmpDTOS.add(kitchenSampleDutyEmpDTO);
                kitchenSampleDutyEmpDTO.setDutyEmpId(employeeDTO.getId());
                kitchenSampleDutyEmpDTO.setDutyEmpName(employeeDTO.getName());
                kitchenSampleDutyEmpDTO.setDutyAvatar(employeeDTO.getAvatar());
                kitchenSampleDutyEmpDTO.setRole(employeeDTO.getPosition());
            }
            kitchenSampleDTO.setKitchenSampleDutyEmpDTOS(kitchenSampleDutyEmpDTOS);
        }

        kitchenSampleDTO.setMealType(sample.getMealType().getCode());
        kitchenSampleDTO.setRequirements(sample.getRequirements());
        kitchenSampleDTO.setRecordTime(sample.getRecordTime());
        kitchenSampleDTO.setComments(sample.getComments());
        kitchenSampleDTO.setId(sample.getId());
        List<String> imgList = new ArrayList<>();
        String[] imgs = sample.getImg().split(",", -1);
        for (String img : imgs) {
            imgList.add(ImgPair.getFileServer() + img);
        }
        kitchenSampleDTO.setImg(imgList);

        return kitchenSampleDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(KitchenSampleAddReq req) {
        KitchenSample kitchenSample = new KitchenSample();
        // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
        BeanUtils.copyProperties(req, kitchenSample);

        //根据餐次查询合格标准
        KitchenItem item = itemRepository.findByGroupCodeAndName(Constants.ITEM_GROUP_SAMPLE_ITEM, String.valueOf(MealTypeEnum.byCode(req.getMealType()).getCode()));
        if (item == null) {
            throw new BizException("item_not_exists", "SampleItem不存在");
        }

        kitchenSample.setItem(item);
        kitchenSample.setRequirements(item.getRequirements());

        kitchenSample.setMealType(MealTypeEnum.byCode(req.getMealType()));

        LocalDateTime now = LocalDateTime.now();
        kitchenSample.setRecordTime(now);
        kitchenSample.setComments(req.getComments());
        List<String> imgList = new ArrayList<>();
        String[] imgs = req.getImg().split(",");
        for (String img01 : imgs) {
            String img = img01.substring(ImgPair.getFileServer().length());
            imgList.add(img);
        }
        kitchenSample.setImg(String.join(",", imgList));
        kitchenSample.setState(1);
        this.kitchenSampleRepository.save(kitchenSample);

        this.kitchenResultService.addOrUpdate(kitchenSample.getId(), Constants.ITEM_GROUP_SAMPLE_ITEM, kitchenSample.getState(), kitchenSample.getRecordTime());
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(KitchenSampleEditReq req) {
        Optional<KitchenSample> optional = kitchenSampleRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new BizException("record_not_exists", "记录不存在");
        }
        KitchenSample kitchenSample = optional.get();
        // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
        BeanUtils.copyProperties(req, kitchenSample);
        //根据餐次查询合格标准
        KitchenItem item = itemRepository.findByGroupCodeAndName(Constants.ITEM_GROUP_SAMPLE_ITEM, String.valueOf(MealTypeEnum.byCode(req.getMealType()).getCode()));
        if (item == null) {
            throw new BizException("item_not_exists", "SampleItem不存在");
        }
        kitchenSample.setItem(item);
        kitchenSample.setRequirements(item.getRequirements());

        kitchenSample.setMealType(MealTypeEnum.byCode(req.getMealType()));

        kitchenSample.setComments(req.getComments());
        List<String> imgList = new ArrayList<>();
        String[] imgs = req.getImg().split(",");
        for (String img01 : imgs) {
            String img = img01.substring(ImgPair.getFileServer().length());
            imgList.add(img);
        }
        kitchenSample.setImg(String.join(",", imgList));
        this.kitchenSampleRepository.save(kitchenSample);

        this.kitchenResultService.addOrUpdate(kitchenSample.getId(), Constants.ITEM_GROUP_SAMPLE_ITEM, kitchenSample.getState(), kitchenSample.getRecordTime());
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Optional<KitchenSample> optional = kitchenSampleRepository.findById(id);
        if (!optional.isPresent()) {
            kitchenSampleRepository.deleteById(id);
            kitchenResultService.delByRecordIdAndItemType(id, Constants.ITEM_GROUP_SAMPLE_ITEM);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(Long[] ids) {
        if (ids.length == 0) {
            throw new BizException("", "请选择需要删除的记录");
        }

        kitchenSampleRepository.deleteByIdIn(ids);
        kitchenResultService.batchDeleteByRecordId(ids, Constants.ITEM_GROUP_SAMPLE_ITEM);
    }

    public BigDecimal todayPassRate() {
        List<KitchenSample> inspectRecords = this.kitchenSampleRepository.findAllByRecordTimeBetween(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0), LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0));
        if (CollectionUtils.isEmpty(inspectRecords)) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(inspectRecords.stream().filter(item -> item.getState() == 1).count() * 100.0 / inspectRecords.size());
    }

    /**
     * 考核记录---留样管理
     *
     * @param condition
     * @return
     */
    public List<AssessSampleListDto> sampleList(AssessRecordContentReq condition) {
        Specification<KitchenSample> spec = SpecificationBuilder.builder()
                .where(Criterion.gte("recordTime", LocalDateTime.of(condition.getBeginDate(), LocalTime.MIN)))
                .where(Criterion.lt("recordTime", LocalDateTime.of(condition.getEndDate(), LocalTime.MAX)))
                .where(Criterion.in("item.dutyEmployeeList.id", condition.getEmpIds()))
                .build();

        List<KitchenSample> kitchenSamples = this.kitchenSampleRepository.findAll(spec);
        return kitchenSampleList(kitchenSamples);
    }

    public List<AssessSampleListDto> kitchenSampleList(List<KitchenSample> kitchenSamples) {
        List<AssessSampleListDto> assessSampleListDtos = kitchenSamples.stream().map(sample -> {

            AssessSampleListDto assessSampleListDto = new AssessSampleListDto();
            assessSampleListDto.setId(sample.getId());
            assessSampleListDto.setMealType(sample.getMealType() != null ? sample.getMealType().getCode() : null);
            assessSampleListDto.setRequirements(sample.getMealType() != null ? sample.getRequirements() : null);
            String[] strings = sample.getImg().split(",", -1);
            assessSampleListDto.setImg(new ArrayList<>(Arrays.asList(strings)));
            assessSampleListDto.setRecordTime(sample.getRecordTime());
            assessSampleListDto.setComments(sample.getComments());
            assessSampleListDto.setState(sample.getState());

            if (null != sample.getItem() && !CollectionUtils.isEmpty(sample.getItem().getDutyEmployeeList())) {
                List<KitchenSampleDutyEmpDTO> kitchenSampleDutyEmpDTOS = new ArrayList<>();
                KitchenSampleDutyEmpDTO kitchenSampleDutyEmpDTO;
                FeignEmployeeDTO employeeDTO;
                for (KitchenItemEmployee employee : sample.getItem().getDutyEmployeeList()) {
                    employeeDTO = feignEmployeeService.findById(employee.getEmpId());
                    if (null == employeeDTO) {
                        continue;
                    }

                    kitchenSampleDutyEmpDTO = new KitchenSampleDutyEmpDTO();
                    kitchenSampleDutyEmpDTOS.add(kitchenSampleDutyEmpDTO);
                    kitchenSampleDutyEmpDTO.setDutyEmpId(employeeDTO.getId());
                    kitchenSampleDutyEmpDTO.setDutyEmpName(employeeDTO.getName());
                    kitchenSampleDutyEmpDTO.setDutyAvatar(employeeDTO.getAvatar());
                    kitchenSampleDutyEmpDTO.setRole(employeeDTO.getPosition());
                }
                assessSampleListDto.setKitchenSampleDutyEmpDTOS(kitchenSampleDutyEmpDTOS);
            }
            return assessSampleListDto;
        }).collect(Collectors.toList());
        return assessSampleListDtos;
    }

    /**
     * 获取留样列表
     *
     * @author loki
     * @date 2021/7/14 11:42
     **/
    public List<FeignKitchenSampleDTO> getSampleImgList() {
        PageRequest pageRequest = PageRequest.of(0, 7);

        Page<KitchenSample> pageResult = this.kitchenSampleRepository.findAll(pageRequest);
        if (CollectionUtils.isEmpty(pageResult.getContent())) {
            return null;
        }

        List<FeignKitchenSampleDTO> result = new ArrayList<>();
        FeignKitchenSampleDTO sampleDTO;
        for (KitchenSample sample : pageResult.getContent()) {
            String[] imgs = sample.getImg().split(",");
            for (String img : imgs) {
                sampleDTO = new FeignKitchenSampleDTO();
                result.add(sampleDTO);
                sampleDTO.setMealTypeName(sample.getMealType().getDesc());
                sampleDTO.setImgUrl(ImgPair.getFileServer() + img);
            }
        }
        return result;
    }
}