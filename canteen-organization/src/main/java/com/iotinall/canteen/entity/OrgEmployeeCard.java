package com.iotinall.canteen.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 卡片
 *
 * @author xin-bing
 * @date 10/10/2019 20:58
 */
@Data
@Entity
@Table(name = "org_employee_card")
@ToString(exclude = {"employee"})
@JsonIgnoreProperties(value = {"employee"})
@EqualsAndHashCode(exclude = {"employee"}, callSuper = false)
public class OrgEmployeeCard extends BaseEntity implements Serializable {

    /**
     * 绑定用户
     */
    @ManyToOne
    @JoinColumn(name = "emp_id", referencedColumnName = "id")
    private OrgEmployee employee;

    /**
     * 卡物理号
     */
    private String cardId;
    /**
     * 卡唯一号
     */
    private String sCardSnr;
    /**
     * 卡号
     */
    private String cardNo;
    /**
     * 开卡时间
     */
    private String commitDate;
    /**
     * 卡状态：1--正常，3--挂失，4--冻结，5--灰色，6--停用，99--未发卡，8--限制存款
     */
    private String accStatus;

    /**
     * 失效期  yyyy-MM-dd
     */
    private String effectDate;

    /**
     * 机构编码
     */
    private String organCode;

    /**
     * 版本号
     */
    private String ver;

    /**
     * 账户类型（关联卡账户绑定的卡类型）
     */
    private String accType;

    /**
     * 账户类型名称,（关联卡账户绑定的卡类型名称）
     */
    private String accTypeName;
}
