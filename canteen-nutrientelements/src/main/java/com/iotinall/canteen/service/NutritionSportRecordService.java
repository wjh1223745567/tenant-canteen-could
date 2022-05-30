package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.dto.sport.SportAddReq;
import com.iotinall.canteen.dto.sport.SportEditReq;
import com.iotinall.canteen.dto.sport.SportRecordDTO;
import com.iotinall.canteen.entity.NutritionSportRecord;
import com.iotinall.canteen.repository.NutritionSportRecordRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 运动记录处理类
 *
 * @author loki
 * @date 2020/04/14 9:19
 */
@Service
public class NutritionSportRecordService {

    @Resource
    NutritionSportRecordRepository sportRecordRepository;

    public Object list(LocalDate date) {
        Long empId = SecurityUtils.getUserId();
        List<NutritionSportRecord> records = this.sportRecordRepository.queryByEmployeeIdAndSportDate(empId, date);
        return records.stream().map(item -> new SportRecordDTO()
                .setId(item.getId())
                .setBurnCalories(item.getBurnCalories())
                .setSportName(item.getSportName())
                .setSportTime(item.getSportTime())
        ).collect(Collectors.toList());
    }

    public void create(SportAddReq req) {
        Long empId = SecurityUtils.getUserId();

        NutritionSportRecord record = new NutritionSportRecord();
        record.setSportName(req.getName());
        record.setBurnCalories(req.getBurnCalories());
        record.setSportTime(req.getTime());
        record.setEmployeeId(empId);
        record.setSportDate(req.getSportDate());
        this.sportRecordRepository.save(record);
    }

    public void update(SportEditReq req) {
        NutritionSportRecord record = this.sportRecordRepository.findById(req.getId()).orElse(null);
        if (null == record) {
            throw new BizException("", "记录不存在");
        }

        record.setSportName(req.getName());
        record.setBurnCalories(req.getBurnCalories());
        record.setSportTime(req.getTime());
        this.sportRecordRepository.save(record);
    }

    public void delete(Long id) {
        this.sportRecordRepository.deleteById(id);
    }

    public BigDecimal calculateBurnCalories(Long orgEmployeeId, LocalDate date) {
        List<NutritionSportRecord> sportRecords = this.sportRecordRepository.queryByEmployeeIdAndSportDate(orgEmployeeId, date);

        BigDecimal totalBurnCalories = BigDecimal.ZERO;
        for(NutritionSportRecord record:sportRecords) {
            totalBurnCalories = totalBurnCalories.add(record.getBurnCalories());
        }
        return totalBurnCalories;
    }
}
