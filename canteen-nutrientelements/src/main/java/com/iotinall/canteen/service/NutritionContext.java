package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.constants.Constants;
import com.iotinall.canteen.dto.person.PersonRecordDTO;
import com.iotinall.canteen.utils.BigDecimalUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * 每日能量计算处理类
 *
 * @author loki
 * @date 2020/04/14 19:37
 */
@Slf4j
@Getter
@Setter
@Accessors(chain = true)
public class NutritionContext {
    private Long employee;
    private PersonRecordDTO person;

    /**
     * energy
     */
    private BigDecimal sportLevel;
    private BigDecimal sports = BigDecimal.ZERO;
    private BigDecimal x = BigDecimal.ZERO;
    private BigDecimal y = BigDecimal.ZERO;


    /**
     * 查询条件
     */
    private LocalDate queryDate = LocalDate.now();
    private Integer queryType;


    static Map<Integer, NutritionCalculate> handlers = new HashMap<>(2);

    static {
        handlers.put(Constants.FEMALE, new NutritionCalculateFemaleHandler());
        handlers.put(Constants.MALE, new NutritionCalculateMaleHandler());
    }

    /**
     * 计算每日需要的能量
     *
     * @author loki
     * @date 2020/04/14 19:50
     */
    public BigDecimal calculate() {
        if (null == this.getPerson()) {
            throw new BizException("11", "未建立个人档案");
        }
        BigDecimal result;
        switch (this.queryType) {
            case Constants.CALCULATE_TYPE_ENERGY: {
                result = handlers.get(person.getGender()).calculateEnergy(this);
                break;
            }
            case Constants.CALCULATE_TYPE_PROTEIN: {
                result = handlers.get(person.getGender()).calculateProtein(this);
                break;
            }
            case Constants.CALCULATE_TYPE_FAT: {
                result = handlers.get(person.getGender()).calculateFat(this);
                break;
            }
            case Constants.CALCULATE_TYPE_CARBS: {
                result = handlers.get(person.getGender()).calculateCarbs(this);
                break;
            }
            case Constants.CALCULATE_TYPE_DIETARY_FIBER: {
                result = handlers.get(person.getGender()).calculateDietaryFiber(this);
                break;
            }
            default: {
                log.info("未知类型");
                throw new BizException("", "未知类型");
            }
        }
        return BigDecimalUtil.convert(result);
    }
}
