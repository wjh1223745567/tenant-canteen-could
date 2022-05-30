package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.constant.Constants;
import com.iotinall.canteen.dto.devicemanagement.FeignCameraDto;
import com.iotinall.canteen.dto.item.*;
import com.iotinall.canteen.dto.organization.FeignSimEmployeeDto;
import com.iotinall.canteen.entity.KitchenItem;
import com.iotinall.canteen.entity.KitchenItemEmployee;
import com.iotinall.canteen.repository.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class KitchenItemService {
    @Resource
    private KitchenItemRepository kitchenItemRepository;
    @Resource
    private KitchenRuleRepository ruleRepository;
    @Resource
    private FeignEmployeeService feignEmployeeService;
    @Resource
    private KitchenAssessRecordRepository assessRecordRepository;
    @Resource
    private KitchenDisinfectRepository disinfectRepository;
    @Resource
    private KitchenOperationRecordRepository operationRecordRepository;
    @Resource
    private KitchenSafetyInspectRecordRepository safetyInspectRecordRepository;
    @Resource
    private KitchenFacilityRecordRepository facilityRecordRepository;
    @Resource
    private KitchenEnvInspectRecordRepository envInspectRecordRepository;
    @Resource
    private KitchenFoodAdditiveRecordRepository foodAdditiveRecordRepository;
    @Resource
    private FeignEquFaceDeviceService feignEquFaceDeviceService;
    @Resource
    private KitchenItemEmployeeRepository kitchenItemEmployeeRepository;

    public List<ItemDTO> list(String groupCode) {
        List<KitchenItem> byGroupCode = kitchenItemRepository.findByGroupCodeOrderBySeq(groupCode);
        if (CollectionUtils.isEmpty(byGroupCode)) {
            return Collections.EMPTY_LIST;
        }
        return byGroupCode.stream().map(item -> {
            ItemDTO dto = new ItemDTO();
            dto.setId(item.getId());
            dto.setName(item.getName());
            dto.setSeq(item.getSeq());

            if (!CollectionUtils.isEmpty(item.getDutyEmployeeList())) {
                List<ItemDutyEmpDTO> itemDutyEmpDTOS = item.getDutyEmployeeList().stream().map(emp -> {
                    ItemDutyEmpDTO itemDutyEmpDTO = new ItemDutyEmpDTO();
                    itemDutyEmpDTO.setDutyEmpName(emp.getEmpName());
                    itemDutyEmpDTO.setDutyEmpId(emp.getEmpId());
                    return itemDutyEmpDTO;

                }).collect(Collectors.toList());
                dto.setItemDutyEmpDTO(itemDutyEmpDTOS);
            }

            if (StringUtils.isNotBlank(item.getCameraIds())) {
                String[] cameraIds = item.getCameraIds().split(",");
                ItemCameraDTO itemCameraDTO;
                FeignCameraDto camera;
                List<ItemCameraDTO> itemCameraDTOList = new ArrayList<>();
                for (String cameraId : cameraIds) {
                    camera = feignEquFaceDeviceService.findById(Long.valueOf(cameraId));
                    if (null != camera) {
                        itemCameraDTO = new ItemCameraDTO();
                        itemCameraDTOList.add(itemCameraDTO);
                        itemCameraDTO.setCameraId(camera.getId());
                        itemCameraDTO.setCameraName(camera.getName());
                    }
                }
                dto.setCameraDTOS(itemCameraDTOList);
            }

            dto.setCreateTime(item.getCreateTime());
            dto.setComment(item.getComment());
            dto.setRequirements(item.getRequirements());
            dto.setCheckTimes(StringUtils.isNotBlank(item.getCheckTime()) ? Arrays.asList(item.getCheckTime().split(",")) : null);
            return dto;
        }).collect(Collectors.toList());
    }

    @CacheEvict(value = "KITCHEN_ITEM", key = "#req.groupCode")
    @Transactional(rollbackFor = Exception.class)
    public Object add(ItemAddReq req) {
        KitchenItem byGroupCodeAndName = kitchenItemRepository.findByGroupCodeAndName(req.getGroupCode(), req.getName());
        if (byGroupCodeAndName != null) {
            throw new BizException("", "已存在同名项");
        }
        KitchenItem item = new KitchenItem();
        if (!Constants.ITEM_GROUP_MORNING_INSPECT.equals(req.getGroupCode())
                && !Constants.ITEM_GROUP_ASSESS_LEVEL.equals(req.getGroupCode())
                && !Constants.ITEM_GROUP_RULE_TYPE.equals(req.getGroupCode())) {
            if (CollectionUtils.isEmpty(req.getEmpIds())) {
                throw new BizException("", "责任人ID不能为空（可有多个责任人）");
            }

            /**
             * 一个groupCode里一个摄像头只能被一个item使用
             */
            if (!Constants.ITEM_GROUP_SAMPLE_ITEM.equals(item.getGroupCode())) {
                if (StringUtils.isBlank(req.getCheckTime())) {
                    throw new BizException("", "检查时间不能为空，格式为HH:MM（可有多个检查时间，用‘，’分隔）");
                }

                FeignCameraDto camera;
                for (Long cameraId : req.getDeviceId()) {
                    camera = this.feignEquFaceDeviceService.findById(cameraId);
                    if (null == camera) {
                        throw new BizException("", "未找到设备信息");
                    }

                    List<KitchenItem> kitchenItemList = this.kitchenItemRepository.findByGroupCodeOrderBySeq(req.getGroupCode());
                    for (KitchenItem kitchenItem : kitchenItemList) {
                        String[] cameraIds = kitchenItem.getCameraIds().split(",");
                        for (String cId : cameraIds) {
                            if (cId.equals(cameraId + "")) {
                                throw new BizException("", "设备在同一个组编码中已使用");
                            }
                        }
                    }
                }
                item.setCameraIds(req.getDeviceId().stream().map(Object::toString).collect(Collectors.joining(",")));
                item.setCheckTime(req.getCheckTime());
                item.setComment(req.getComment());
            }
        }

        item.setDeleted(Boolean.FALSE);
        item.setName(req.getName());
        item.setGroupCode(req.getGroupCode());
        item.setSeq(req.getSeq());
        item.setRequirements(req.getRequirements());
        item = kitchenItemRepository.save(item);

        if (!CollectionUtils.isEmpty(req.getEmpIds())) {
            FeignSimEmployeeDto employee;
            KitchenItemEmployee kitchenItemEmployee;
            Set<KitchenItemEmployee> employeeSet = new HashSet<>();
            for (Long empId : req.getEmpIds()) {
                employee = feignEmployeeService.findSimById(empId);
                if (null == employee) {
                    throw new BizException("责任人不存在");
                }
                kitchenItemEmployee = new KitchenItemEmployee();
                kitchenItemEmployee.setEmpId(empId);
                kitchenItemEmployee.setEmpName(employee.getName());
                kitchenItemEmployee.setItem(item);
                employeeSet.add(kitchenItemEmployee);
            }
            kitchenItemEmployeeRepository.saveAll(employeeSet);
        }
        return item;
    }

    @CacheEvict(value = "KITCHEN_ITEM", allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void edit(ItemEditReq req) {
        KitchenItem item = kitchenItemRepository.findById(req.getId()).orElse(null);
        if (item == null) {
            throw new BizException("", "修改的记录不存在");
        }
        if (!Constants.ITEM_GROUP_MORNING_INSPECT.equals(item.getGroupCode())
                && !Constants.ITEM_GROUP_ASSESS_LEVEL.equals(item.getGroupCode())
                && !Constants.ITEM_GROUP_RULE_TYPE.equals(item.getGroupCode())) {

            if (!Constants.ITEM_GROUP_SAMPLE_ITEM.equals(item.getGroupCode())) {
                if (req.getCheckTime().isEmpty()) {
                    throw new BizException("", "检查时间不能为空，格式为HH:MM（可有多个检查时间，用‘，’分隔）");
                }
                FeignCameraDto camera;
                for (Long cameraId : req.getDeviceId()) {
                    camera = this.feignEquFaceDeviceService.findById(cameraId);
                    if (null == camera) {
                        throw new BizException("", "未找到设备信息");
                    }

                    List<KitchenItem> kitchenItemList = this.kitchenItemRepository.findByGroupCodeOrderBySeq(item.getGroupCode());
                    for (KitchenItem kitchenItem : kitchenItemList) {
                        String[] cameraIds = kitchenItem.getCameraIds().split(",");
                        for (String cId : cameraIds) {
                            if (cId.equals(cameraId + "") && !kitchenItem.getId().equals(req.getId())) {
                                throw new BizException("", "设备在同一个组编码中已使用");
                            }
                        }
                    }
                }
                item.setCameraIds(req.getDeviceId().stream().map(Object::toString).collect(Collectors.joining(",")));
                item.setCheckTime(req.getCheckTime());
                item.setComment(req.getComment());
            }
        }

        item.setDeleted(Boolean.FALSE);
        item.setName(req.getName());
        item.setSeq(req.getSeq());
        item.setRequirements(req.getRequirements());
        kitchenItemRepository.save(item);

        //先删再存
        List<KitchenItemEmployee> employeeList = this.kitchenItemEmployeeRepository.findByItem(item);
        if (!CollectionUtils.isEmpty(employeeList)) {
            this.kitchenItemEmployeeRepository.deleteAll(employeeList);
        }

        if (!CollectionUtils.isEmpty(req.getEmpIds())) {
            FeignSimEmployeeDto employee;
            KitchenItemEmployee kitchenItemEmployee;
            Set<KitchenItemEmployee> employeeSet = new HashSet<>();
            for (Long empId : req.getEmpIds()) {
                employee = feignEmployeeService.findSimById(empId);
                if (null == employee) {
                    throw new BizException("责任人不存在");
                }
                kitchenItemEmployee = new KitchenItemEmployee();
                kitchenItemEmployee.setEmpId(empId);
                kitchenItemEmployee.setEmpName(employee.getName());
                kitchenItemEmployee.setItem(item);
                employeeSet.add(kitchenItemEmployee);
            }
            kitchenItemEmployeeRepository.saveAll(employeeSet);
        }
    }

    @CacheEvict(value = "KITCHEN_ITEM", allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void del(Long[] batch) {
        if (batch.length == 0) {
            throw new BizException("", "请选择需要删除的记录");
        }

        List<KitchenItem> itemList = new ArrayList<>();
        KitchenItem item;
        Boolean exits = false;
        for (Long id : batch) {
            item = this.kitchenItemRepository.findById(id).orElseThrow(() -> new BizException("", "删除内容不存在"));
            item.setDutyEmployeeList(null);
            itemList.add(item);
            if (item.getGroupCode().equals(Constants.ITEM_GROUP_ASSESS_LEVEL)) {
                exits = assessRecordRepository.existsByItem(item);
            } else if (item.getGroupCode().equals(Constants.ITEM_GROUP_RULE_TYPE)) {
                exits = ruleRepository.existsByType(item);
            } else if (item.getGroupCode().equals(Constants.ITEM_GROUP_DISINFECT_ITEM)) {
                exits = disinfectRepository.existsByItem(item);
            } else if (item.getGroupCode().equals(Constants.ITEM_GROUP_WASH_ITEM) ||
                    item.getGroupCode().equals(Constants.ITEM_GROUP_CHOP_ITEM)) {
                exits = operationRecordRepository.existsByItem(item);
            } else if (item.getGroupCode().equals(Constants.ITEM_GROUP_FIRE_PROTECT_ITEM)) {
                exits = safetyInspectRecordRepository.existsByItem(item);
            } else if (item.getGroupCode().equals(Constants.ITEM_GROUP_FACILITY_ITEM)) {
                exits = facilityRecordRepository.existsByItem(item);
            } else if (item.getGroupCode().equals(Constants.ITEM_GROUP_ENV_ITEM)) {
                exits = envInspectRecordRepository.existsByItem(item);
            } else if (item.getGroupCode().equals(Constants.ITEM_GROUP_FOOD_ADDITIVES)) {
                exits = foodAdditiveRecordRepository.existsByItem(item);
            }

            if (exits) {
                throw new BizException("", "存在已被使用的项目，不能删除");
            }
        }

        kitchenItemRepository.deleteAll(itemList);
    }
}
