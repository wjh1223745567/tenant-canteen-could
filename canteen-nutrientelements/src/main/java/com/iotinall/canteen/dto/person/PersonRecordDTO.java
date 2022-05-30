package com.iotinall.canteen.dto.person;

import com.iotinall.canteen.utils.LocalDateUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * 营养档案 用户基本信息返回实体类
 *
 * @author loki
 * @date 2020/04/10 15:15
 */
@Data
public class PersonRecordDTO implements Serializable {
    private Long id;

    /**
     * 性别 0-女 1-男 可以和员工表性别不一样,营养档案以这个为准
     */
    private Integer gender;

    /**
     * 出生日期
     */
    private LocalDate birthDate;

    /**
     * 怀孕日期
     */
    private LocalDate pregnancyDate;

    /**
     * 身高 单位cm
     */
    private Integer personHeight;

    /**
     * 体重 单位kg
     */
    private Integer personWeight;

    /**
     * 身材目标 0-减脂 1-增肌 2-保持体形
     */
    private Integer bodyTarget;

    /**
     * 活动水平类型 0-低强度 1-中强度 2-高强度
     */
    private Integer strengthLevel;

    /**
     * 生理状态 1-怀孕 2-乳母 默认 0
     */
    private Integer physiologicalState;

    /**
     * 疾病
     */
    private String disease;

    /**
     * 疾病中文名称
     */
    private String diseaseName;

    /**
     * 卡路里值
     */
    private BigDecimal intake;

    /**
     * 获取用户年龄
     *
     * @author loki
     * @date 2020/04/15 10:23
     */
    public Integer getAge(LocalDate date) {
        return LocalDateUtil.getUntil(this.getBirthDate(), date, ChronoUnit.YEARS);
    }

    /**
     * 获取用户怀孕的周
     *
     * @author loki
     * @date 2020/04/15 10:23
     */
    public Integer getPregnancyWeek(LocalDate date) {
        return LocalDateUtil.getUntil(this.getPregnancyDate(), date, ChronoUnit.WEEKS);
    }
}
