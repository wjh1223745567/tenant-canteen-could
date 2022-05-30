package com.iotinall.canteen.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 组织员工
 *
 * @author xin-bing
 * @date 10/10/2019 20:46
 */
@Data
@Entity
@Accessors(chain = true)
@JsonIgnoreProperties(value = {"org", "roles", "personalRecords"})
@SQLDelete(sql = "update org_employee set deleted = 1 where id = ?")
@Table(name = "org_employee")
@ToString(exclude = {"org", "roles"})
@EqualsAndHashCode(exclude = {"org", "roles", "wallet"}, callSuper = true)
public class OrgEmployee extends BaseEntity implements Serializable {

    @Column(nullable = false)
    private String name; // 姓名

    @Column(nullable = false)
    private Integer gender; // 0女 1男

    @Column
    private String idNo; // 身份证号码，加密存储

    private String personCode; // 人员工号

    @Column(nullable = false)
    private String mobile; // 手机号码

    private String entryMonthDay;

    private String birthdayMonthDay;

    private LocalDate birthday;

    private String telephone; // 电话

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", nullable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private Org org;

    private String role; // 职位

    @Column(nullable = false)
    private Boolean enabled; // 启用/禁用

    private Boolean deleted;

    /**
     * 员工钱包
     */
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "wallet_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private EmployeeWallet wallet;

    /**
     * 0：单位职工 1：后厨人员 2：其他人员
     */
    @Column
    private Integer personnelType;

    /**
     * 入职日期
     */
    private LocalDate entryDate;

    /**
     * 登入密码
     */
    @Column(nullable = false, length = 64)
    private String pwd;

    /**
     * 微信openid
     */
    @Column(length = 32, unique = true)
    private String openid;

    /**
     * 卡号
     */
    @Column(nullable = false, unique = true, length = 20)
    private String cardNo;

    @Column(columnDefinition = "longtext")
    private String avatarFace;

    @OneToMany(mappedBy = "employee", fetch = FetchType.EAGER)
    private List<OrgEmployeePersonalRecords> personalRecords;

    /**
     * 头像路径
     */
    private String avatar;

    @ManyToMany
    @JoinTable(name = "sys_emp_roles", joinColumns = {
            @JoinColumn(name = "emp_id", referencedColumnName = "id")
    }, inverseJoinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id")
    })
    private Set<SysRole> roles;

    /**
     * 来源 0-当前系统添加 1-人脸通行系统添加
     */
    private Integer source;

    /**
     * 上一次登入时间，登入时维护该字段
     */
    private LocalDateTime lastLoginTime;

    /**
     * 人员对应的卡
     */
    @OneToMany(fetch = FetchType.LAZY)
    private List<OrgEmployeeCard> cardList;
}
