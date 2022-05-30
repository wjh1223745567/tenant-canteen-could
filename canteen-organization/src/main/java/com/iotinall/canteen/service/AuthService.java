package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.protocol.DataSourceInfoDTO;
import com.iotinall.canteen.common.security.SecurityUserDetails;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.common.util.SpringContextUtil;
import com.iotinall.canteen.constant.ErrCode;
import com.iotinall.canteen.dto.auth.EditPwdReq;
import com.iotinall.canteen.dto.auth.LoginReq;
import com.iotinall.canteen.entity.OrgEmployee;
import com.iotinall.canteen.entity.SysPermission;
import com.iotinall.canteen.entity.SysRole;
import com.iotinall.canteen.repository.OrgEmployeeRepository;
import com.iotinall.canteen.repository.SysPermissionRepository;
import com.iotinall.canteen.repository.SysRoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 鉴权相关
 *
 * @author xin-bing
 * @date 10/26/2019 14:07
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AuthService implements UserDetailsService {

    @Resource
    private OrgEmployeeRepository orgEmployeeRepository;

    @Resource
    private SysRoleRepository sysRoleRepository;

    @Resource
    private SysPermissionRepository sysPermissionRepository;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${login-timeout:30}")
    private Integer timeout; // 分钟

    @Override
    public UserDetails loadUserByUsername(String username) {
        OrgEmployee orgEmployee = orgEmployeeRepository.queryByMobileAndDeletedIsFalse(username);
        if (orgEmployee == null) {
            throw new BizException(ErrCode.SYS_USERNAME_NOT_EXISTS, "用户名不存在");
        }
        return buildUserDetails(orgEmployee);
    }

    /**
     * 登录方法
     *
     * @param loginReq 登录请求
     * @return SecurityUserDetails
     */
    @Transactional(rollbackFor = Exception.class)
    public SecurityUserDetails login(LoginReq loginReq) {
        PasswordEncoder passwordEncoder = SpringContextUtil.getBean(PasswordEncoder.class);
        OrgEmployee orgEmployee = isSuperAdmin(loginReq.getUsername()) || isQuartzSys(loginReq.getUsername())
                ? orgEmployeeRepository.queryByMobile(loginReq.getUsername())
                : orgEmployeeRepository.queryByMobileAndDeletedIsFalse(loginReq.getUsername());
        if (orgEmployee == null) {
            throw new BizException("", "用户名或密码错误");
        }

        if (!passwordEncoder.matches(loginReq.getPwd(), orgEmployee.getPwd())) {
            throw new BizException(ErrCode.SYS_USER_PWD_MISS_MATCH, "用户名或密码错误");
        }
        return buildUserDetails(orgEmployee);
    }

    @Transactional(rollbackFor = Exception.class)
    public void editPwd(EditPwdReq req) {
        PasswordEncoder passwordEncoder = SpringContextUtil.getBean(PasswordEncoder.class);
        SecurityUserDetails currUser = SecurityUtils.getCurrentUser();
        OrgEmployee orgEmployee = orgEmployeeRepository.findById(currUser.getEmpId()).orElseThrow(() -> new BizException("", "当前用户不存在"));
        if (!passwordEncoder.matches(req.getOldPwd(), orgEmployee.getPwd())) {
            throw new BizException("", "原密码错误");
        }
        String newPwd = passwordEncoder.encode(req.getPwd());
        orgEmployee.setPwd(newPwd);
        orgEmployeeRepository.save(orgEmployee);
    }

    private SecurityUserDetails buildUserDetails(OrgEmployee employee) {
        List<SysRole> roles = sysRoleRepository.findByEmployeesIn(Collections.singletonList(employee));
        if (roles.isEmpty() && !isSuperAdmin(employee.getMobile()) && !isQuartzSys(employee.getMobile())) {
            throw new BizException("", "未配置后台权限");
        }
        Set<String> permissions = new HashSet<>();

        List<String> strRoles = roles.stream().map(SysRole::getName).collect(Collectors.toList());

        roles.forEach(item -> {
            Set<String> result = item.getPermissions().stream().map(SysPermission::getPermission).collect(Collectors.toSet());
            permissions.addAll(result);
        });

        if (isSuperAdmin(employee.getMobile())) {
            permissions.add("ADMIN");
        }

        SecurityUserDetails userDetails = new SecurityUserDetails();
        userDetails.setSourceInfo(new DataSourceInfoDTO());

        userDetails.setRoleTenantOrgIds(roles.stream().map(SysRole::getTenantOrgId).collect(Collectors.toSet()));

        userDetails.setUsername(employee.getMobile());
        userDetails.setNickName(employee.getName());
        userDetails.setEmpId(employee.getId());
        if (null != employee.getOrg()) {
            userDetails.setOrgId(employee.getOrg().getId());
            userDetails.setOrgName(employee.getOrg().getName());
        }
        userDetails.setAvatar(employee.getAvatar());
        userDetails.setRoles(strRoles);
        userDetails.setSupperAdmin(isSuperAdmin(employee.getMobile()));
        userDetails.setPermissions(new ArrayList<>(permissions));

        String token = UUID.randomUUID().toString().replace("-", "");
        userDetails.setToken(token);
        redisTemplate.opsForValue().set(token, userDetails, timeout, TimeUnit.MINUTES);
        return userDetails;
    }

    private boolean isSuperAdmin(String loginName) {
        return "admin".equals(loginName);
    }

    private boolean isQuartzSys(String loginName) {
        return "quartzuser".equals(loginName);
    }
}
