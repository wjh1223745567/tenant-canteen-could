package com.iotinall.canteen.service;

import com.iotinall.canteen.common.constant.MealTypeEnum;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.util.RedisCacheUtil;
import com.iotinall.canteen.constant.RoomTypeEnum;
import com.iotinall.canteen.constant.ToExamineEnum;
import com.iotinall.canteen.dto.messprod.FeignMessProdDto;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.dto.roomreservation.*;
import com.iotinall.canteen.entity.RoomReservation;
import com.iotinall.canteen.entity.RoomReservationReserved;
import com.iotinall.canteen.entity.RoomReservationReservedProd;
import com.iotinall.canteen.entity.RoomReservationTime;
import com.iotinall.canteen.repository.RoomReservationRepository;
import com.iotinall.canteen.repository.RoomReservationReservedProdRepository;
import com.iotinall.canteen.repository.RoomReservationReservedRepository;
import com.iotinall.canteen.repository.RoomReservationTimeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoomReservationService {
    @Resource
    private RoomReservationRepository roomReservationRepository;
    @Resource
    private RoomReservationTimeRepository roomReservationTimeRepository;
    @Resource
    private RoomReservationReservedRepository reservedRepository;
    @Resource
    private RoomReservationReservedProdRepository prodRepository;
    @Resource
    private FeignEmployeeService feignEmployeeService;
    @Resource
    private FeignMessProductService feignMessProductService;
    @Resource
    private FeignTenantOrganizationService feignTenantOrganizationService;

    /**
     * @author HJJ
     * @date 2021/5/12 17:31
     **/
    public Object findRoomReservation(RoomReservationCondition condition, Pageable pageable) {
        Long menuId = feignTenantOrganizationService.findMenuId();

        SpecificationBuilder builder = SpecificationBuilder.builder()
                .where(Criterion.like("roomName", condition.getRoomName()))
                .where(Criterion.eq("tenantOrgId", menuId));
        Page<RoomReservation> page = this.roomReservationRepository.findAll(builder.build(), pageable);

        List<RoomReservationDTO> list = page.getContent().stream().map(item -> {
            RoomReservationDTO roomReservationDTO = new RoomReservationDTO();
            roomReservationDTO.setId(item.getId());
            roomReservationDTO.setRoomName(item.getRoomName());
            roomReservationDTO.setRoomPicture(item.getRoomPicture());
            roomReservationDTO.setPeople(item.getNumberOfPeople());
            roomReservationDTO.setRoomAddress(item.getRoomAddress());
            roomReservationDTO.setRoomReserved(item.getRoomTypeEnum().getCode());
            roomReservationDTO.setRoomReservedName(item.getRoomTypeEnum().getDesc());

            List<RoomReservationTime> roomReservationTimes = this.roomReservationTimeRepository.findByRoom(item);
            roomReservationDTO.setTimeReqs(
                    roomReservationTimes.stream().map(reservationTime -> {
                        RoomReservationTimeUpdateReq updateReq = new RoomReservationTimeUpdateReq()
                                .setId(reservationTime.getId())
                                .setBeginTime(reservationTime.getBeginTime())
                                .setEndTime(reservationTime.getEndTime())
                                .setHoliday(reservationTime.getHoliday())
                                .setMealType(reservationTime.getTypeEnum() != null ? reservationTime.getTypeEnum().getCode() : 0);
                        return updateReq;
                    }).collect(Collectors.toList())
            );

            List<RoomReservationTime> breakfastTime = roomReservationTimes.stream().filter(reservationTime -> Objects.equals(reservationTime.getTypeEnum(), MealTypeEnum.BREAKFAST)).collect(Collectors.toList());
            List<RoomReservationTime> lunchTime = roomReservationTimes.stream().filter(reservationTime -> Objects.equals(reservationTime.getTypeEnum(), MealTypeEnum.LUNCH)).collect(Collectors.toList());
            List<RoomReservationTime> dinnerTime = roomReservationTimes.stream().filter(reservationTime -> Objects.equals(reservationTime.getTypeEnum(), MealTypeEnum.DINNER)).collect(Collectors.toList());
            StringBuilder breakfast = new StringBuilder();
            StringBuilder lunch = new StringBuilder();
            StringBuilder dinner = new StringBuilder();
            for (RoomReservationTime time : breakfastTime) {
                breakfast.append("早餐：").append(time.getBeginTime()).append("~").append(time.getEndTime()).append(";");
            }
            for (RoomReservationTime time : lunchTime) {
                lunch.append("午餐：").append(time.getBeginTime()).append("~").append(time.getEndTime()).append(";");

            }
            for (RoomReservationTime time : dinnerTime) {
                dinner.append("晚餐：").append(time.getBeginTime()).append("~").append(time.getEndTime()).append(";");
            }
            String time = breakfast.toString() + lunch.toString() + dinner.toString();

            roomReservationDTO.setTime(time);
            return roomReservationDTO;
        }).collect(Collectors.toList());

        return PageUtil.toPageDTO(list, page);
    }

    /**
     * @author HJJ
     * @date 2021/5/12 17:31
     **/
    @Transactional(rollbackFor = Exception.class)
    public void addRoom(RoomReservationAddReq req) {
        RoomReservation roomReservation = new RoomReservation();
        List<RoomReservationTime> times = new ArrayList<>();
        roomReservation.setRoomName(req.getRoomName());
        roomReservation.setRoomPicture(req.getRoomPicture());
        roomReservation.setNumberOfPeople(req.getPeople());
        roomReservation.setRoomAddress(req.getRoomAddress())
                .setRoomTypeEnum(RoomTypeEnum.FREE)
                .setDeleted(false);
        this.roomReservationRepository.save(roomReservation);

        List<RoomReservationTimeReq> timeReqs = req.getTimeReqs();
        if (!CollectionUtils.isEmpty(timeReqs)) {
            for (RoomReservationTimeReq timeReq : timeReqs) {
                RoomReservationTime time = new RoomReservationTime();
                time.setTypeEnum(MealTypeEnum.byCode(timeReq.getMealType()));
                time.setBeginTime(timeReq.getBeginTime());
                time.setEndTime(timeReq.getEndTime());
                time.setRoom(roomReservation);
                time.setHoliday(timeReq.getHoliday());
                time.setCreateTime(LocalDateTime.now());
                time.setUpdateTime(LocalDateTime.now());
                time.setDeleted(false);
                this.roomReservationTimeRepository.save(time);
                times.add(time);
            }
            roomReservation.setRoomTimes(times);
        }

        this.roomReservationRepository.save(roomReservation);
    }

    /**
     * @author HJJ
     * @date 2021/5/12 17:32
     **/
    @Transactional(rollbackFor = Exception.class)
    public void update(RoomReservationUpdateReq req) {
        RoomReservation roomReservation = this.roomReservationRepository.findById(req.getId()).orElseThrow(() -> new BizException("未找到该包间信息"));
        List<Long> allIds = roomReservation.getRoomTimes().stream().map(RoomReservationTime::getId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(req.getTimeReqs())) {

            for (RoomReservationTimeUpdateReq timeReq : req.getTimeReqs()) {
                RoomReservationTime time = this.roomReservationTimeRepository.findByIdAndRoom(timeReq.getId(), roomReservation);
                if (null == time) {
                    time = new RoomReservationTime();
                }
                time.setBeginTime(timeReq.getBeginTime());
                time.setTypeEnum(MealTypeEnum.byCode(timeReq.getMealType()));
                time.setEndTime(timeReq.getEndTime());
                time.setHoliday(timeReq.getHoliday());
                time.setDeleted(Boolean.FALSE);
                time.setRoom(roomReservation);
                this.roomReservationTimeRepository.saveAndFlush(time);
            }

            List<Long> updateId = req.getTimeReqs().stream().map(RoomReservationTimeUpdateReq::getId).filter(Objects::nonNull).collect(Collectors.toList());
            allIds.removeAll(updateId);
            if (!allIds.isEmpty()) {
                for (Long id : allIds) {
                    this.roomReservationTimeRepository.deleteById(id);
                }
            }
        }

        roomReservation.setRoomName(req.getRoomName())
                .setNumberOfPeople(req.getPeople())
                .setRoomAddress(req.getRoomAddress())
                .setRoomPicture(req.getRoomPicture())
                .setUpdateTime(LocalDateTime.now());
        this.roomReservationRepository.save(roomReservation);
    }

    /**
     * @author HJJ
     * @date 2021/5/12 17:32
     **/
//    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {RedisCacheUtil.APP_INFORMATION_LIST}, allEntries = true)
    public Object del(Long[] ids) {
        if (ids.length == 0) {
            throw new BizException("请选择要删除的记录");
        }
        List<RoomReservation> roomReservations = new ArrayList<>();
        RoomReservation roomReservation;

        for (Long id : ids) {
            roomReservation = this.roomReservationRepository.findById(id).orElseThrow(() -> new BizException("未找到该记录"));
            List<RoomReservationReserved> reservedList = this.reservedRepository.findByRoom(roomReservation).stream()
                    .filter(reserve -> Objects.equals(reserve.getToExamine(), ToExamineEnum.SUCCESS.getCode())).collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(reservedList)) {
                throw new BizException("该包间存在预定，暂不可删除");
            }

            roomReservations.add(roomReservation);
            List<RoomReservationTime> time = this.roomReservationTimeRepository.findByRoom(roomReservation);
            this.roomReservationTimeRepository.deleteAll(time);
        }

        this.roomReservationRepository.deleteAll(roomReservations);
        return roomReservations;

    }

    public Object findRoomReserve(RoomReserveCondition condition, Pageable pageable) {

        Specification<RoomReservationReserved> specification = SpecificationBuilder.builder()
                .where(Criterion.like("room.roomName", condition.getRoomName()))
                .where(Criterion.gte("reservedTime", condition.getBeginDate() == null ? null : condition.getBeginDate()))
                .where(Criterion.lt("reservedTime", condition.getEndDate() == null ? null : condition.getEndDate().plusDays(1)))
                .where(Criterion.eq("toExamine", condition.getToExamine()))
                .where(Criterion.like("applyPeopleName", condition.getEmpName()))
                .build();
        Page<RoomReservationReserved> page = this.reservedRepository.findAll(specification, pageable);

        List<RoomReserveFindDto> reservedList = page.getContent().stream().map(reserve -> {

            RoomReserveFindDto dto = new RoomReserveFindDto();
            dto.setId(reserve.getId());
            dto.setRemark(reserve.getRemark());
            dto.setConfirmRemark(reserve.getConfirmRemark());
            dto.setRoomName(reserve.getRoom() != null ? reserve.getRoom().getRoomName() : null);
            if (reserve.getReservedTime() != null && reserve.getTime() != null) {
                dto.setReserveTime(reserve.getReservedTime().toString()
                        + reserve.getTime().getTypeEnum().getDesc()
                        + reserve.getTime().getBeginTime() + "~" + reserve.getTime().getEndTime());
            }

            if (null != reserve.getApplyPeopleId()) {
                FeignEmployeeDTO employee = feignEmployeeService.findById(reserve.getApplyPeopleId());
                if (null != employee) {
                    dto.setEmpName(employee.getName());
                    dto.setOrgName(employee.getOrgName());
                } else {
                    dto.setEmpName(reserve.getApplyPeopleName());
                }
            }

            List<RoomReservationReservedProd> messDtos = this.prodRepository.findByReserved(reserve);
            if (!CollectionUtils.isEmpty(messDtos)) {
                RoomReserveFindMessDto messDto;
                FeignMessProdDto feignMessProdDto;
                List<RoomReserveFindMessDto> result = new ArrayList<>();
                for (RoomReservationReservedProd mess : messDtos) {
                    feignMessProdDto = feignMessProductService.findDtoById(mess.getMessProductId());
                    if (null == feignMessProdDto) {
                        continue;
                    }
                    messDto = new RoomReserveFindMessDto();
                    result.add(messDto);
                    messDto.setMessId(feignMessProdDto.getId());
                    messDto.setMessPicture(feignMessProdDto.getImg());
                    messDto.setMessName(feignMessProdDto.getName());
                }
                dto.setMessDtos(result);
            }

            dto.setNumberOfPeople(reserve.getNumberOfPeople());
            dto.setCreateTime(reserve.getCreateTime());
            dto.setToExamine(reserve.getToExamine());
            dto.setToExamineName(ToExamineEnum.byCode(reserve.getToExamine()).getDesc());
            return dto;

        }).collect(Collectors.toList());

        return PageUtil.toPageDTO(reservedList, page);
    }

}
