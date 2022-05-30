package com.iotinall.canteen.service;


import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.protocol.DataSourceInfoDTO;
import com.iotinall.canteen.common.protocol.SimpDataSource;
import com.iotinall.canteen.common.security.AuthorizationTokenFilter;
import com.iotinall.canteen.common.security.IUserService;
import com.iotinall.canteen.common.security.SecurityUserDetails;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.common.util.RedisCacheUtil;
import com.iotinall.canteen.common.util.SpringContextUtil;
import com.iotinall.canteen.common.util.ValidateCodeUtil;
import com.iotinall.canteen.constant.DateTimeFormatters;
import com.iotinall.canteen.constant.PhoneSmsType;
import com.iotinall.canteen.constant.RedisConstants;
import com.iotinall.canteen.constant.TenantOrganizationTypeEnum;
import com.iotinall.canteen.dto.sms.MobileLoginReq;
import com.iotinall.canteen.dto.sms.UpdatePasswordBySmsReq;
import com.iotinall.canteen.entity.OrgEmployee;
import com.iotinall.canteen.entity.SysPermission;
import com.iotinall.canteen.entity.SysRole;
import com.iotinall.canteen.repository.OrgEmployeeRepository;
import com.iotinall.canteen.repository.SysRoleRepository;
import com.iotinall.canteen.dto.storehouse.FeignTenantOrganizationDto;
import com.iotinall.canteen.utils.sms.QCloudSmsUtil;
import com.iotinall.canteen.utils.sms.SmsConfig;
import com.iotinall.canteen.utils.sms.SmsConstants;
import com.iotinall.canteen.utils.sms.SmsForm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author WJH
 * @date 2019/11/910:26
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserService implements IUserService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private OrgEmployeeRepository employeeRepository;
    @Resource
    private SysRoleRepository sysRoleRepository;
    @Resource
    private PasswordEncoder passwordEncoder;

    public static final String openIdKey = "-openid-";

    public Map<String, String> getRandomCodeImage(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> result = ValidateCodeUtil.getRandomCodeImage();
        if (result != null) {
            String key = UUID.randomUUID().toString().replace("-", "");
            String value = result.get("key");
            redisTemplate.opsForValue().set(key, value, 5 * 60, TimeUnit.SECONDS);
            result.put("key", key);
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public SecurityUserDetails login(String validCode, String sureValidCode, String phone, String password, String openId) {
        if (!validCode.equalsIgnoreCase(sureValidCode)) {
            log.error("验证码错误：{},{}", validCode, sureValidCode);
            throw new BizException("", "请输入正确验证码");
        }
        SecurityUserDetails userDetails = this.loadData(phone, password, openId);
        return userDetails;
    }

    @Transactional(rollbackFor = Exception.class)
    public SecurityUserDetails login(String phone, String password) {
        SecurityUserDetails userDetails = this.loadData(phone, password, null);
        return userDetails;
    }

    @Transactional(rollbackFor = Exception.class)
    public SecurityUserDetails loadData(String phone, String password, String openId) {
        OrgEmployee employee = employeeRepository.queryByMobileAndDeletedIsFalse(phone);
        if (employee == null) {
            throw new BizException("", "用户不存在");
        }

        if (!passwordEncoder.matches(password, employee.getPwd())) {
            throw new BizException("", "密码错误");
        }

        if (!employee.getEnabled()) {
            throw new BizException("", "当前用户已被禁用，请联系管理员开启");
        }
        return this.genLoginData(employee, openId);
    }

    public SecurityUserDetails loadDataByOpenId(String openId) {
        if (StringUtils.isBlank(openId)) {
            throw new BizException("", "微信自动登录失败");
        }
        OrgEmployee employee = employeeRepository.queryByOpenidAndDeletedIsFalse(openId);
        if (employee != null) {
            return genLoginData(employee, openId);
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
            @CacheEvict(value = RedisCacheUtil.APP_USER_INFO, key = "#empId"),
            @CacheEvict(value = RedisCacheUtil.SYS_ORG_EMP, allEntries = true)
    })
    public void updateHeadImage(String avatar, Long empId) {
        OrgEmployee employee = this.employeeRepository.findById(empId).orElse(null);
        employee.setAvatar(avatar);
        this.employeeRepository.save(employee);
    }

    @Transactional(rollbackFor = Exception.class)
    public void signOut(HttpServletRequest request) {
        String token = AuthorizationTokenFilter.getToken(request);
        OrgEmployee employee = this.employeeRepository.findById(Objects.requireNonNull(SecurityUtils.getUserId())).orElse(null);
        if (StringUtils.isNotBlank(token)) {
            redisTemplate.delete(token);
            if (employee != null) {
                employee.setOpenid(null);
                this.employeeRepository.save(employee);
            }
        }
    }

    public Object getLoginMobileCode(String mobile, Integer type) {
        if (StringUtils.isBlank(mobile)) {
            throw new BizException("", "请输入手机号");
        }

        //校验手机号是否存在
        OrgEmployee employee = employeeRepository.queryByMobileAndDeletedIsFalse(mobile);
        if (null == employee) {
            throw new BizException("", "用户不存在");
        }

        //校验是否操作频繁
        String preKey = RedisConstants.LOGIN_MOBILE_CODE + mobile + RedisConstants.REDIS_KEY_SPLIT;
        String latestKey = preKey + RedisConstants.LATEST;
        Object value = redisTemplate.opsForValue().get(latestKey);
        if (null != value) {
            throw new BizException("", "获取短信验证码过于频繁，请稍后再试!");
        }

        //校验今日获取验证码次数是否超过最大次数
        String timesKey = preKey + LocalDate.now().format(DateTimeFormatters.STANDARD_DATE_FORMATTER) + RedisConstants.REDIS_KEY_SPLIT + RedisConstants.TODAY_TIMES;
        value = redisTemplate.opsForValue().get(timesKey);
        Integer times;
        if (null != value) {
            times = Integer.valueOf(value.toString());
            if (times >= RedisConstants.TODAY_MAX_TIMES) {
                throw new BizException("", "今日获取短信验证码已超过" + RedisConstants.TODAY_MAX_TIMES + "次,请更换其他登入方式");
            }
        } else {
            times = 0;
        }

        //生成验证码
        String code = RandomStringUtils.randomNumeric(4);
        log.info("生成的CODE【{}】", code);
        //发送验证码
        SmsForm sms = new SmsForm()
                .setMobile(mobile);
        SmsConfig config = new SmsConfig()
                .setAppId(SmsConstants.APP_ID)
                .setAppKey(SmsConstants.APP_KEY)
                .setSign(SmsConstants.SIGN);

        switch (type) {
            case PhoneSmsType.SMS_LOGIN:
                config.setTemplateId(SmsConstants.TEMPLATE_ID);
                sms.setCaptcha(new String[]{code, "30"});
                break;
            case PhoneSmsType.SMS_PASSWORD:
                config.setTemplateId(SmsConstants.PASSWORD_TEMPLATE_ID);
                sms.setCaptcha(new String[]{code});
                break;
            default:
                throw new BizException("", "未知消息类型" + type);
        }

        try {
            QCloudSmsUtil.sendMessage(true, sms, config);
        } catch (BizException ex) {
            throw new BizException("", "发送短信验证码失败");
        }

        //更新短信验证码间隔
        redisTemplate.opsForValue().set(latestKey, code);
        redisTemplate.expire(latestKey, RedisConstants.LATEST_EXPIRE, TimeUnit.SECONDS);

        //更新今日获取短信验证码次数,24小时后过期
        redisTemplate.opsForValue().set(timesKey, times + 1);
        redisTemplate.expire(timesKey, 24, TimeUnit.HOURS);

        //更新验证码
        String codeKey = preKey + RedisConstants.CODE;
        redisTemplate.opsForValue().set(codeKey, code);
        redisTemplate.expire(codeKey, RedisConstants.LOGIN_MOBILE_CODE_EXPIRE, TimeUnit.SECONDS);

        return RedisConstants.TODAY_MAX_TIMES - times - 1;
    }

    /**
     * 根据短信验证码修改密码
     */
    public void changePasswordByPhone(UpdatePasswordBySmsReq smsReq) {
        String smsCodeKey = RedisConstants.LOGIN_MOBILE_CODE + smsReq.getPhone() + RedisConstants.REDIS_KEY_SPLIT + RedisConstants.CODE;
        String code = redisTemplate.opsForValue().get(smsCodeKey) != null ? Objects.requireNonNull(redisTemplate.opsForValue().get(smsCodeKey)).toString() : null;
        if (!smsReq.getSmsCode().equals(code)) {
            throw new BizException("", "验证码错误");
        }

        OrgEmployee employee = employeeRepository.queryByMobileAndDeletedIsFalse(smsReq.getPhone());
        if (null == employee) {
            throw new BizException("", "用户不存在");
        }
        employee.setPwd(this.passwordEncoder.encode(smsReq.getPassword()));
        this.employeeRepository.save(employee);
        redisTemplate.delete(smsCodeKey);
    }


    public Object loginByMobile(MobileLoginReq req) {
        //校验手机号是否存在
        OrgEmployee employee = employeeRepository.queryByMobileAndDeletedIsFalse(req.getMobile());
        if (null == employee) {
            throw new BizException("", "用户不存在");
        }

        //校验短信验证码是否过期
        String key = RedisConstants.LOGIN_MOBILE_CODE + req.getMobile() + RedisConstants.REDIS_KEY_SPLIT + RedisConstants.CODE;
        Object value = redisTemplate.opsForValue().get(key);
        if (null == value) {
            throw new BizException("", "短信验证码已过期");
        }

        if (!value.toString().equals(req.getCode())) {
            throw new BizException("", "短信验证码错误");
        }

        if (!employee.getEnabled()) {
            throw new BizException("", "当前用户已被禁用，请联系管理员开启");
        }

        SecurityUserDetails loginInfo = this.genLoginData(employee, req.getOpenId());

        //删除验证码
        redisTemplate.delete(key);
        return loginInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    public SecurityUserDetails genLoginData(OrgEmployee employee, String openId) {
        if (StringUtils.isNotBlank(openId) && !Objects.equals(openId, employee.getOpenid())) {
            employee.setOpenid(openId);
        }
        employee.setLastLoginTime(LocalDateTime.now());
        this.employeeRepository.save(employee);

        String token = UUID.randomUUID().toString().replace("-", "") + openIdKey + (StringUtils.isNotBlank(employee.getOpenid()) ? employee.getOpenid() : "");
        SecurityUserDetails userDetails = new SecurityUserDetails();
        userDetails.setEmpId(employee.getId());
        userDetails.setUsername(employee.getMobile());
        userDetails.setNickName(employee.getName());
        userDetails.setOpenId(openId);

        if (employee.getOrg() != null) {
            userDetails.setOrgId(employee.getOrg().getId());
            userDetails.setOrgName(employee.getOrg().getName());
        }

        List<SysRole> roles = sysRoleRepository.findByEmployeesIn(Collections.singletonList(employee));
        //数据源信息
        DataSourceInfoDTO dataSourceInfoDTO = new DataSourceInfoDTO();

        userDetails.setSourceInfo(dataSourceInfoDTO);

        if (CollectionUtils.isEmpty(roles)) {
            userDetails.setRoles(Collections.singletonList("CUSTOMER"));
            userDetails.setPermissions(Collections.singletonList("CUSTOMER"));
        } else {
            Set<String> r = new HashSet<>();
            Set<String> permissions = new HashSet<>();
            roles.forEach(item -> {
                r.add(item.getName());
                Set<String> p = item.getPermissions().stream().map(SysPermission::getPermission).collect(Collectors.toSet());
                permissions.addAll(p);
            });
            userDetails.setRoles(new ArrayList<>(r));
            userDetails.setPermissions(new ArrayList<>(permissions));
            Set<Long> sets = roles.stream().map(SysRole::getTenantOrgId).collect(Collectors.toSet());
            userDetails.setRoleTenantOrgIds(sets);
        }

        userDetails.setAuthorities(userDetails.getPermissions().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

        userDetails.setCardNo(employee.getCardNo());
        userDetails.setAvatar(employee.getAvatar());
        userDetails.setToken(token);
        redisTemplate.opsForValue().set(userDetails.getToken(), userDetails, 100, TimeUnit.MINUTES);
        return userDetails;
    }

    public static void addDataSources(DataSourceInfoDTO dataSourceInfoDTO, Set<Long> sets) {
        if (!CollectionUtils.isEmpty(sets) || SecurityUtils.getCurrentUser().isSupperAdmin()) {
            FeignTenantOrganizationService feignTenantOrganizationService = SpringContextUtil.getBean(FeignTenantOrganizationService.class);
            Map<Long, FeignTenantOrganizationDto> map;
            if (SecurityUtils.getCurrentUser().isSupperAdmin()) {
                map = feignTenantOrganizationService.findAll();
            } else {
                map = feignTenantOrganizationService.findByIds(sets);
            }
            dataSourceInfoDTO.setAllMenu(new ArrayList<>());
            dataSourceInfoDTO.setAllKitchen(new ArrayList<>());
            dataSourceInfoDTO.setAllStock(new ArrayList<>());

            for (FeignTenantOrganizationDto value : map.values()) {
                SimpDataSource simpDataSource = new SimpDataSource()
                        .setId(value.getId())
                        .setName(value.getName())
                        .setDataSourceKey(value.getDataSourceKey());

                switch (TenantOrganizationTypeEnum.findByCode(value.getType())) {
                    case DINING_HALL: {
                        if (StringUtils.isBlank(dataSourceInfoDTO.getMenu())) {
                            dataSourceInfoDTO.setMenu(value.getDataSourceKey());
                        }

                        if (dataSourceInfoDTO.getAllMenu().stream().noneMatch(item -> Objects.equals(item.getDataSourceKey(), value.getDataSourceKey()))) {
                            dataSourceInfoDTO.getAllMenu().add(simpDataSource);
                        }
                        break;
                    }

                    case BACK_KITCHEN: {
                        if (StringUtils.isBlank(dataSourceInfoDTO.getKitchen())) {
                            dataSourceInfoDTO.setKitchen(value.getDataSourceKey());
                        }

                        if (dataSourceInfoDTO.getAllKitchen().stream().noneMatch(item -> Objects.equals(item.getDataSourceKey(), value.getDataSourceKey()))) {
                            dataSourceInfoDTO.getAllKitchen().add(simpDataSource);
                        }
                        break;
                    }

                    case INVENTORY: {
                        if (StringUtils.isBlank(dataSourceInfoDTO.getStock())) {
                            dataSourceInfoDTO.setStock(value.getDataSourceKey());
                        }

                        if (dataSourceInfoDTO.getAllStock().stream().noneMatch(item -> Objects.equals(item.getDataSourceKey(), value.getDataSourceKey()))) {
                            dataSourceInfoDTO.getAllStock().add(simpDataSource);
                        }
                        break;
                    }

                    case FOOD_DEPARTMENT: {

                        break;
                    }

                }
            }
        }
        //处理切换数据源时消失的选中源
        if (!CollectionUtils.isEmpty(dataSourceInfoDTO.getAllMenu()) && dataSourceInfoDTO.getAllMenu().stream().noneMatch(item -> Objects.equals(item.getDataSourceKey(), dataSourceInfoDTO.getMenu()))) {
            dataSourceInfoDTO.setMenu(dataSourceInfoDTO.getAllMenu().get(0).getDataSourceKey());
        }
    }

    public static void addCanteenDataSources(DataSourceInfoDTO dataSourceInfoDTO, String menu) {
        FeignTenantOrganizationService feignTenantOrganizationService = SpringContextUtil.getBean(FeignTenantOrganizationService.class);
        List<FeignTenantOrganizationDto> list = feignTenantOrganizationService.findAllCanteen();
        dataSourceInfoDTO.setAllMenu(new ArrayList<>());
        for (FeignTenantOrganizationDto value : list) {
            SimpDataSource simpDataSource = new SimpDataSource()
                    .setId(value.getId())
                    .setName(value.getName())
                    .setDataSourceKey(value.getDataSourceKey());
            if (StringUtils.isBlank(menu) && Objects.equals(value.getDataSourceKey(), menu)) {
                dataSourceInfoDTO.setMenu(value.getDataSourceKey());
            }

            if (dataSourceInfoDTO.getAllMenu().stream().noneMatch(item -> Objects.equals(item.getDataSourceKey(), value.getDataSourceKey()))) {
                dataSourceInfoDTO.getAllMenu().add(simpDataSource);
            }
        }
    }
}
