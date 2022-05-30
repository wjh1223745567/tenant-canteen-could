package com.iotinall.canteen.common.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.constant.TenantOrganizationTypeEnum;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.protocol.DataSourceInfoDTO;
import com.iotinall.canteen.common.protocol.SimpDataSource;
import com.iotinall.canteen.common.util.ImgPair;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author xin-bing
 * @date 10/17/2019 16:35
 */
@Data
public class SecurityUserDetails implements UserDetails {
    private Long empId; // org employee id
    private String openId;
    private String username; // 登录名
    @JsonIgnore
    private String password; // 密码

    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String avatar; // 头像
    private String nickName; // 昵称
    private String cardNo;
    private String email; // 邮箱
    private String phone; // 手机号码
    private Long orgId;//组织机构ID
    private String orgName;
    private List<String> roles = new ArrayList<>(); // 角色
    private Set<Long> roleTenantOrgIds = new HashSet<>();//所属租户ID
    private String token;
    private boolean isSupperAdmin; // 是否是超级管理员

    private DataSourceInfoDTO sourceInfo;

    private Collection<GrantedAuthority> authorities = new ArrayList<>(); // 权限
    private List<String> permissions = new ArrayList<>();
    private boolean enabled = true; // 是否启用
    private boolean accountNonExpired = true; // 账号未过期
    private boolean accountNonLocked = true; // 账号未被锁定
    private boolean credentialsNonExpired = true; // 账号权限未过期

    public Long getTenantOrgIdByType(TenantOrganizationTypeEnum type) {
        String dataSourceKey = null;
        List<SimpDataSource> sourceList = null;
        if (TenantOrganizationTypeEnum.DINING_HALL.equals(type)) {
            dataSourceKey = this.sourceInfo.getMenu();
            sourceList = this.sourceInfo.getAllMenu();
        } else if (TenantOrganizationTypeEnum.BACK_KITCHEN.equals(type)) {
            dataSourceKey = this.sourceInfo.getKitchen();
            sourceList = this.sourceInfo.getAllKitchen();
        } else if (TenantOrganizationTypeEnum.INVENTORY.equals(type)) {
            dataSourceKey = this.sourceInfo.getStock();
            sourceList = this.sourceInfo.getAllStock();
        }

        return getTenantOrgIdByType(dataSourceKey, sourceList);
    }

    private Long getTenantOrgIdByType(String dataSourceKey, List<SimpDataSource> sourceList) {
        if (null == dataSourceKey || CollectionUtils.isEmpty(sourceList)) {
            throw new BizException("数据错误");
        }

        return sourceList
                .stream()
                .filter(item -> item.getDataSourceKey().equals(dataSourceKey))
                .findFirst()
                .orElseThrow(() -> new BizException("数据错误")).getId();
    }
}
