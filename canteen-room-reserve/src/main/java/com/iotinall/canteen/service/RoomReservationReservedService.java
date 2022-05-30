package com.iotinall.canteen.service;

import com.iotinall.canteen.common.constant.MealTypeEnum;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.constant.ToExamineEnum;
import com.iotinall.canteen.dto.messprod.FeignMessProdDto;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.dto.roomreservation.*;
import com.iotinall.canteen.dto.roomreservationreserved.*;
import com.iotinall.canteen.entity.RoomReservation;
import com.iotinall.canteen.entity.RoomReservationReserved;
import com.iotinall.canteen.entity.RoomReservationReservedProd;
import com.iotinall.canteen.entity.RoomReservationTime;
import com.iotinall.canteen.repository.RoomReservationRepository;
import com.iotinall.canteen.repository.RoomReservationReservedProdRepository;
import com.iotinall.canteen.repository.RoomReservationReservedRepository;
import com.iotinall.canteen.repository.RoomReservationTimeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoomReservationReservedService {
    @Resource
    private RoomReservationRepository roomReservationRepository;
    @Resource
    private RoomReservationTimeRepository roomReservationTimeRepository;
    @Resource
    private RoomReservationReservedRepository reservedRepository;
    @Resource
    private FeignEmployeeService feignEmployeeService;
    @Resource
    private RoomReservationReservedProdRepository reservedProdRepository;
    @Resource
    private FeignMessProductService feignMessProductService;
    @Resource
    private FeignHolidayService feignHolidayService;
    @Resource
    private FeignTenantOrganizationService feignTenantOrganizationService;

    /**
     * 小程序包间预订首页
     *
     * @author HJJ
     * @date 2021/5/12 17:31
     **/
    public Object findAllReserved(RoomReservedCondition condition) {
        SpecificationBuilder builder = SpecificationBuilder
                .builder();
        if (condition.getNumberPeople() != null) {
            if (condition.getNumberPeople() == 1) {
                builder.where(
                        Criterion.lte("numberOfPeople", 10));
            }
            if (condition.getNumberPeople() == 2) {
                builder.where(
                        Criterion.gte("numberOfPeople", 10))
                        .where(Criterion.lte("numberOfPeople", 15));
            }
            if (condition.getNumberPeople() == 3) {
                builder.where(
                        Criterion.gt("numberOfPeople", 15));
            }
        }

        if (null != condition.getType()) {
            builder.where(Criterion.eq("roomTimes.typeEnum", MealTypeEnum.byCode(condition.getType())));
        }

        Long menuId = feignTenantOrganizationService.findMenuId();
        builder.where(Criterion.eq("tenantOrgId", menuId));

        List<RoomReservation> roomList = this.roomReservationRepository.findAll(builder.build(true));
        List<RoomReservedDto> result = roomList.stream().map(item -> {
            RoomReservedDto roomReservedDto = new RoomReservedDto();
            roomReservedDto.setId(item.getId());
            roomReservedDto.setRoomName(item.getRoomName());
            roomReservedDto.setRoomPicture(item.getRoomPicture());
            roomReservedDto.setNumberOfPeople(item.getNumberOfPeople());
            roomReservedDto.setRoomTypeEnum(item.getRoomTypeEnum());
            return roomReservedDto;
        }).collect(Collectors.toList());

        return result;
    }

    /**
     * 包间详情
     *
     * @author HJJ
     * @date 2021/5/12 17:31
     **/
    public Object roomDetail(Long roomId, LocalDate date) {
        RoomReservation room = this.roomReservationRepository.findById(roomId).orElseThrow(() -> new BizException("未找到包间信息"));
        RoomDetailDto roomDetailDto = new RoomDetailDto();
        roomDetailDto.setRoomId(room.getId());
        roomDetailDto.setRoomPicture(room.getRoomPicture());
        roomDetailDto.setRoomName(room.getRoomName());
        roomDetailDto.setNumberOfPeople(room.getNumberOfPeople());
        roomDetailDto.setRoomAddress(room.getRoomAddress());
        roomDetailDto.setRoomTypeEnum(room.getRoomTypeEnum());

        List<RoomReservationTime> timeList = room.getRoomTimes();
        if (CollectionUtils.isEmpty(timeList)) {
            roomDetailDto.setTimeDtos(new ArrayList<>());
        }

        //查包间时间段 以及 是否节假日开放
        List<RoomDetailTimeDto> timeDtoList = timeList.stream().map(time -> {
            RoomDetailTimeDto timeDto = new RoomDetailTimeDto();
            timeDto.setTimeId(time.getId());
            timeDto.setType(time.getTypeEnum());
            timeDto.setMealTime(time.getBeginTime() + "~" + time.getEndTime());
            timeDto.setHoliday(time.getHoliday() ? "节假日接受预定" : "节假日不接受预定");

            Boolean isHoliday = this.feignHolidayService.isHoliday(date);
            if (!isHoliday) {
                Integer reserveTimes = this.reservedRepository.countByRoomAndReservedTimeAndTime(room, date, time);
                timeDto.setReserve(reserveTimes <= 0);
            } else {
                timeDto.setReserve(false);
            }
            return timeDto;
        }).collect(Collectors.toList());

        roomDetailDto.setTimeDtos(timeDtoList);
        return roomDetailDto;
    }

    /**
     * 订房
     *
     * @author HJJ
     * @date 2021/5/12 17:31
     **/
    @Transactional(rollbackFor = Exception.class)
    public RoomReserveAddDto addReserve(RoomReservedAddReq req) {
        RoomReservation roomReservation = this.roomReservationRepository.findById(req.getRoomId()).orElseThrow(() -> new BizException("未找到当前包厢"));
        RoomReservationTime time = this.roomReservationTimeRepository.findById(req.getTimeId()).orElseThrow(() -> new BizException("未找到当前预定时间段"));

        /**
         * 刘俊修改于2021-07-28 已有人预定（包括未审核）不能再被预订
         */
        List<RoomReservationReserved> bookedRoomList = this.reservedRepository.findByRoomAndReservedTimeAndTime(roomReservation, LocalDate.now(), time);
        if (!CollectionUtils.isEmpty(bookedRoomList)) {
            throw new BizException("该包厢已被预订");
        }

        if (req.getNumberOfPeople() > roomReservation.getNumberOfPeople()) {
            throw new BizException("用餐人数不能大于" + roomReservation.getNumberOfPeople() + "人");
        }

        RoomReservationReserved reserved = new RoomReservationReserved();
        reserved.setApplyPeopleId(SecurityUtils.getUserId());
        reserved.setApplyPeopleName(SecurityUtils.getUserName());
        reserved.setRoom(roomReservation);
        reserved.setType(time.getTypeEnum().getCode());
        reserved.setReservedTime(req.getReservedTime());
        reserved.setTime(time);
        reserved.setDeleted(false);
        reserved.setToExamine(ToExamineEnum.SUCCESS.getCode());
        reserved.setNumberOfPeople(req.getNumberOfPeople());
        reserved.setRemark(req.getRemark());
        reserved.setCreateTime(LocalDateTime.now());
        reserved.setUpdateTime(LocalDateTime.now());
        this.reservedRepository.save(reserved);

        List<RoomReservationReservedProd> prods = new ArrayList<>();
        RoomReservationReservedProd prod;
        if (!CollectionUtils.isEmpty(req.getProductAddReqs())) {
            for (RoomReservedProductAddReq productAddReq : req.getProductAddReqs()) {
                prod = new RoomReservationReservedProd();
                prod.setReserved(reserved);
                prod.setMessProductId(productAddReq.getProductId());
                prod.setMessProductName(productAddReq.getProductName());
                this.reservedProdRepository.save(prod);
                prods.add(prod);
            }
        }

        reserved.setProds(prods);
        this.reservedRepository.save(reserved);

        RoomReserveAddDto addDto = new RoomReserveAddDto();
        FeignEmployeeDTO employee = feignEmployeeService.findById(SecurityUtils.getUserId());
        if (null == employee) {
            throw new BizException("申请人为空");
        }
        addDto.setEmpName(employee.getName());
        addDto.setOrgName(employee.getOrgName());
        addDto.setPhone(employee.getMobile());

        addDto.setRoomName(roomReservation.getRoomName() + "(" + roomReservation.getNumberOfPeople().toString() + ")");
        addDto.setDateTime(req.getReservedTime().toString() + " " + time.getBeginTime() + "~" + time.getEndTime());
        addDto.setTypeName(time.getTypeEnum().getDesc());
        return addDto;
    }

    /**
     * 我的预定---申请人
     */
    public Object findMyRoomReserve(Long cursor, Integer type, String key) {
        LocalDateTime localDateTime = cursor != null ? LocalDateTime.ofEpochSecond(cursor, 0, ZoneOffset.ofHours(8)) : null;
        SpecificationBuilder builder = SpecificationBuilder
                .builder()
                .where(Criterion.eq("applyPeopleId", SecurityUtils.getUserId()))
                .where(Criterion.lt("createTime", localDateTime));
        if (type != null) {
            builder.where(Criterion.eq("toExamine", type));
        }
        if (key != null) {
            builder.where(Criterion.like("room.roomName", key));
        }

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<RoomReservationReserved> page = this.reservedRepository.findAll(builder.build(), pageRequest);

        List<MyRoomReserveDto> reserveDtos = page.get().map(reserve -> {
            String dayOfWeek = "";
            if (reserve.getReservedTime() != null) {
                switch (reserve.getReservedTime().getDayOfWeek()) {
                    case MONDAY:
                        dayOfWeek = "周一";
                        break;
                    case TUESDAY:
                        dayOfWeek = "周二";
                        break;
                    case WEDNESDAY:
                        dayOfWeek = "周三";
                        break;
                    case THURSDAY:
                        dayOfWeek = "周四";
                        break;
                    case FRIDAY:
                        dayOfWeek = "周五";
                        break;
                    case SATURDAY:
                        dayOfWeek = "周六";
                        break;
                    case SUNDAY:
                        dayOfWeek = "周日";
                        break;
                    default:
                        break;
                }
            }

            MyRoomReserveDto myRoomReserveDto = new MyRoomReserveDto();
            myRoomReserveDto.setId(reserve.getId());
            myRoomReserveDto.setRoomPicture(reserve.getRoom() != null ? reserve.getRoom().getRoomPicture() : null);
            myRoomReserveDto.setRoomName(reserve.getRoom() != null ? reserve.getRoom().getRoomName() : null);
            if (reserve.getReservedTime() != null && reserve.getTime() != null && reserve.getTime().getBeginTime() != null && reserve.getTime().getEndTime() != null) {
                myRoomReserveDto.setReserveTime(reserve.getReservedTime().toString() + " "
                        + dayOfWeek + " "
                        + reserve.getTime().getBeginTime() + "~" + reserve.getTime().getEndTime());
            }
            myRoomReserveDto.setReserveMeal(reserve.getTime() != null ? reserve.getTime().getTypeEnum().getDesc() : null);
            myRoomReserveDto.setReserveType(ToExamineEnum.byCode(reserve.getToExamine()).getDesc());
            myRoomReserveDto.setNumberPeople(reserve.getNumberOfPeople());
            myRoomReserveDto.setCreateTime(reserve.getCreateTime().toInstant(ZoneOffset.ofHours(8)).getEpochSecond());

            if (reserve.getApplyPeopleId() != null) {
                FeignEmployeeDTO applyPeople = feignEmployeeService.findById(reserve.getApplyPeopleId());
                if (null != applyPeople) {
                    myRoomReserveDto.setEmpName(applyPeople.getName());
                    myRoomReserveDto.setEmpId(applyPeople.getId());
                }
            }
            return myRoomReserveDto;
        }).collect(Collectors.toList());

        return PageUtil.toCursorPageDTO(reserveDtos, CollectionUtils.isEmpty(reserveDtos) ? -1 : reserveDtos.get(reserveDtos.size() - 1).getCreateTime());
    }

    /**
     * 取消
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateReserveTypeCancel(MyReserveCancelReq req) {
        RoomReservationReserved reserved = this.reservedRepository.findById(req.getReserveId()).orElseThrow(() -> new BizException("未找到该预定记录"));
        Long empId = SecurityUtils.getUserId();
        if (Objects.equals(empId, reserved.getApplyPeopleId())) {
            reserved.setToExamine(ToExamineEnum.CANCEL.getCode());
            this.reservedRepository.save(reserved);
        } else {
            throw new BizException("非当前用户数据，无法取消");
        }
    }

    /**
     * 我的预定---申请人详情
     */
    public Object myRoomReserveDetail(Long reserveId) {
        RoomReservationReserved reserved = this.reservedRepository.findById(reserveId).orElseThrow(() -> new BizException("未找到该预定记录"));

        MyRoomReserveDetailDto detailDto = new MyRoomReserveDetailDto();

        detailDto.setNowEmp(Objects.equals(SecurityUtils.getUserId(), reserved.getApplyPeopleId()));
        detailDto.setReserveId(reserveId);
        detailDto.setTimeId(reserved.getTime() != null ? reserved.getTime().getId() : null);
        detailDto.setRoomId(reserved.getRoom() != null ? reserved.getRoom().getId() : null);
        detailDto.setReservedTime(reserved.getReservedTime());
        detailDto.setRoomName(reserved.getRoom().getRoomName() + "(" + reserved.getRoom().getNumberOfPeople().toString() + ")");
        detailDto.setRoomAddress(reserved.getRoom() != null ? reserved.getRoom().getRoomAddress() : "");
        detailDto.setDateTime(reserved.getReservedTime() + " " + reserved.getTime().getBeginTime() + "~" + reserved.getTime().getEndTime());
        detailDto.setTypeName(reserved.getTime().getTypeEnum().getDesc());
        detailDto.setNumberPeople(reserved.getNumberOfPeople());
        detailDto.setMaxNumberPeople(reserved.getRoom() != null ? reserved.getRoom().getNumberOfPeople() : 0);
        detailDto.setRemark(reserved.getRemark() != null ? reserved.getRemark() : "无");

        if (null != reserved.getApplyPeopleId()) {
            FeignEmployeeDTO applyPeople = feignEmployeeService.findById(reserved.getApplyPeopleId());
            if (null != applyPeople) {
                detailDto.setEmpName(applyPeople.getName());
                detailDto.setOrgName(applyPeople.getOrgName());
                detailDto.setPhone(applyPeople.getMobile());
            }
        }

        if (!CollectionUtils.isEmpty(reserved.getProds())) {
            List<MyRoomReserveDetailMessDto> messDtos = new ArrayList<>();
            MyRoomReserveDetailMessDto messDto = null;
            for (RoomReservationReservedProd reservedProd : reserved.getProds()) {
                messDto = new MyRoomReserveDetailMessDto();
                if(reservedProd.getMessProductId() != null){
                    FeignMessProdDto messProduct = feignMessProductService.findDtoById(reservedProd.getMessProductId());
                    if (null != messProduct) {
                        messDtos.add(messDto);
                        messDto.setMessId(messProduct.getId());
                        messDto.setMessName(messProduct.getName());
                        messDto.setMessPicture(messProduct.getImg());
                    }
                }else{
                    messDtos.add(messDto);
                    messDto.setMessId(reservedProd.getId());
                    messDto.setName(reservedProd.getMessProductName());
                }
            }
            detailDto.setMessDtos(messDtos);
        }

        detailDto.setReserveCode(ToExamineEnum.byCode(reserved.getToExamine()).getCode());
        detailDto.setReserveType(ToExamineEnum.byCode(reserved.getToExamine()).getDesc());
        detailDto.setConfirmRemark(reserved.getConfirmRemark() != null ? reserved.getConfirmRemark() : "无");
        return detailDto;
    }

    //重新提交接口
    @Transactional(rollbackFor = Exception.class)
    public void updateReserve(MyRoomReserveUpdateReq req) {
        RoomReservationReserved reserved = this.reservedRepository.findById(req.getReserveId()).orElseThrow(() -> new BizException("未找到该预定记录"));
        RoomReservation roomReservation = this.roomReservationRepository.findById(req.getRoomId()).orElseThrow(() -> new BizException("未找到当前包厢"));
        RoomReservationTime time = this.roomReservationTimeRepository.findById(req.getTimeId()).orElseThrow(() -> new BizException("未找到当前预定时间段"));

        reserved.setApplyPeopleId(SecurityUtils.getUserId());
        reserved.setRoom(roomReservation);
        reserved.setType(time.getTypeEnum().getCode());
        reserved.setReservedTime(req.getReservedTime());
        reserved.setTime(time);
        reserved.setDeleted(false);
        reserved.setNumberOfPeople(req.getNumberOfPeople());
        reserved.setRemark(req.getRemark());
        this.reservedRepository.save(reserved);

        reserved.getProds().clear();

        List<RoomReservationReservedProd> prods = reserved.getProds();
        RoomReservationReservedProd prod;
        for (RoomReservedProductAddReq productAddReq : req.getProductAddReqs()) {
//            FeignMessProdDto messProduct = feignMessProductService.findDtoById(productAddReq.getProductId());
//            if (messProduct == null) {
//                throw new BizException("未找到该菜品");
//            }
            prod = new RoomReservationReservedProd();
            prod.setReserved(reserved);
            prod.setMessProductId(productAddReq.getProductId());
            prod.setMessProductName(productAddReq.getProductName());
            this.reservedProdRepository.save(prod);
            prods.add(prod);
        }
        reserved.setProds(prods);
        this.reservedRepository.save(reserved);
    }

    /**
     * 修改过期任务自动过期
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void overdueApplicationTask() {
        Specification<RoomReservationReserved> specification = SpecificationBuilder.builder()
                .where(
                        Criterion.ne("toExamine", ToExamineEnum.BEOVERDUE.getCode()),
                        Criterion.lt("reservedTime", LocalDate.now())
                )
                .build();
        List<RoomReservationReserved> reserveds = this.reservedRepository.findAll(specification);
        for (RoomReservationReserved reserved : reserveds) {
            reserved.setToExamine(ToExamineEnum.BEOVERDUE.getCode());
            this.reservedRepository.save(reserved);
        }
    }
}
