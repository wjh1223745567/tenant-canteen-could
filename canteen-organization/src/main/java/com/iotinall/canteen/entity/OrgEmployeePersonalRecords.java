package com.iotinall.canteen.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * 个人档案
 */

@EqualsAndHashCode(callSuper = true, exclude = {"employee"})
@Data
@Entity
@Accessors(chain = true)
@Table(name = "org_employee_personal_records")
@JsonIgnoreProperties(value = "employee")
@ToString(exclude = {"employee"})
public class OrgEmployeePersonalRecords extends BaseEntity {

    /**
     * 人员信息
     */
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false, foreignKey = @ForeignKey(name = "null"))
    private OrgEmployee employee;

    /**
     * 档案
     */
    @Column(length = 200, nullable = false)
    private String name;

    /**
     * 获取日期
     */
    @Column(nullable = false)
    private LocalDate haveDate;

    /**
     * 上传图片路径
     */
    @Column(nullable = false, length = 400)
    private String url;
}
