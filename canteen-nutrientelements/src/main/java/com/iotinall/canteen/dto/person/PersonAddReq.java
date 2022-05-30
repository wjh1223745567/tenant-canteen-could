package com.iotinall.canteen.dto.person;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 营养档案添加用户基本信息请求参数
 *
 * @author loki
 * @date 2020/04/10 14:39
 */
@Data
public class PersonAddReq implements Serializable {
    /**
     * 性别 0-女 1-男 可以和员工表性别不一样,营养档案以这个为准
     */
    @NotNull(message = "用户性别不能为空")
    private Integer gender;

    /**
     * 出生日期
     */
    @NotNull(message = "出生日期不能为空")
    private LocalDate birthDate;

    /**
     * 怀孕日期
     */
    private LocalDate pregnancyDate;

    /**
     * 身高 单位cm
     */
    @NotNull(message = "身高不能为空")
    private Integer personHeight;

    /**
     * 体重 单位kg
     */
    @NotNull(message = "体重不能为空")
    private Integer personWeight;

    /**
     * 身材目标 0-减脂 1-增肌 2-保持体形
     */
    @NotNull(message = "目标不能为空")
    private Integer bodyTarget;

    /**
     * 活动水平类型 0-低强度 1-中强度 2-高强度
     */
    @NotNull(message = "活动类型不能为空")
    private Integer strengthLevel;

    /**
     * 生理状态 1-怀孕 2-乳母
     */
    private Integer physiologicalState = 0;

    private BigDecimal intake;

    /**
     * 疾病
     */
    private String disease;
}
