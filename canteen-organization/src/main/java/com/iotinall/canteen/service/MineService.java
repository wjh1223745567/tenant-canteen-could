package com.iotinall.canteen.service;


import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.security.SecurityUserDetails;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.common.util.RedisCacheUtil;
import com.iotinall.canteen.constant.DateTimeFormatters;
import com.iotinall.canteen.constant.PhoneSmsType;
import com.iotinall.canteen.constant.RedisConstants;
import com.iotinall.canteen.dto.mine.UserInfoDto;
import com.iotinall.canteen.dto.sms.UpdatePasswordBySmsReq;
import com.iotinall.canteen.entity.EmployeeWallet;
import com.iotinall.canteen.entity.OrgEmployee;
import com.iotinall.canteen.entity.SysPermission;
import com.iotinall.canteen.entity.SysRole;
import com.iotinall.canteen.repository.EmployeeWalletRepository;
import com.iotinall.canteen.repository.OrgEmployeeRepository;
import com.iotinall.canteen.repository.SysRoleRepository;
import com.iotinall.canteen.utils.LocalDateUtil;
import com.iotinall.canteen.utils.sms.QCloudSmsUtil;
import com.iotinall.canteen.utils.sms.SmsConfig;
import com.iotinall.canteen.utils.sms.SmsConstants;
import com.iotinall.canteen.utils.sms.SmsForm;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author WJH
 * @date 2019/11/69:19
 */
@Service
public class MineService {
    @Resource
    private OrgEmployeeRepository employeeRepository;
    @Resource
    private SysRoleRepository sysRoleRepository;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    EmployeeWalletRepository walletRepository;

    @Cacheable(value = RedisCacheUtil.APP_USER_INFO, key = "#id")
    public UserInfoDto getUserViewData(Long id) {
        OrgEmployee employee = employeeRepository.findById(id).orElse(null);
        if (employee != null) {
            UserInfoDto infoDto = new UserInfoDto()
                    .setId(employee.getId())
                    .setActualName(employee.getName())
                    .setGender(employee.getGender())
                    .setEmployeeCode(employee.getCardNo())
                    .setImgHead(employee.getAvatar())
                    .setOrgName(employee.getOrg() != null ? employee.getOrg().getName() : null)
                    .setTelephone(employee.getTelephone())
                    .setPhone(employee.getMobile());
            return infoDto;
        }
        return null;
    }

    /**
     * 修改用户密码
     *
     * @param password
     * @param oldPassword
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(String password, String oldPassword) {
        Long empid = SecurityUtils.getUserId();
        OrgEmployee orgEmployee = this.employeeRepository.findById(empid).orElse(null);
        if (orgEmployee == null) {
            throw new BizException("", "当前用户不存在");
        }
        if (passwordEncoder.matches(oldPassword, orgEmployee.getPwd())) {
            orgEmployee.setPwd(passwordEncoder.encode(password));
        } else {
            throw new BizException("", "请输入正确旧密码！");
        }
    }

    /**
     * 获取验证码
     *
     * @return
     */
    public Object getUpdatePasswordCode(String mobile, Integer type) {
        if (StringUtils.isBlank(mobile)) {
            throw new BizException("", "请输入手机号");
        }

        OrgEmployee orgEmployee = this.employeeRepository.queryByMobileAndDeletedIsFalse(mobile);
        if (orgEmployee == null) {
            throw new BizException("", "用户不存在");
        }

        String preKey = RedisConstants.LOGIN_MOBILE_CODE + mobile + RedisConstants.REDIS_KEY_SPLIT;
        String latestKey = preKey + RedisConstants.LATEST;
        Object value = redisTemplate.opsForValue().get(latestKey);
        if (null != value) {
            throw new BizException("", "获取短信验证码过于频繁，请稍后再试！");
        }

        String timesKey = preKey + LocalDateUtil.format(LocalDate.now(), DateTimeFormatters.STANDARD_DATE_FORMATTER) + RedisConstants.REDIS_KEY_SPLIT + RedisConstants.TODAY_TIMES;
        value = redisTemplate.opsForValue().get(timesKey);
        Integer times;
        if (null != value) {
            times = Integer.valueOf(value.toString());
            if (times >= RedisConstants.TODAY_MAX_TIMES) {
                throw new BizException("", "今日获取短信验证码已超过" + RedisConstants.TODAY_MAX_TIMES + "次,请更换其他修改方式");
            }
        } else {
            times = 0;
        }

        //生成验证码
        String code = RandomStringUtils.randomNumeric(4);
        //发送验证码
        SmsForm sms = new SmsForm()
                .setMobile(mobile);
        SmsConfig config = new SmsConfig()
                .setAppId(SmsConstants.APP_ID)
                .setAppKey(SmsConstants.APP_KEY)
                .setSign(SmsConstants.SIGN);

        switch (type) {
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

    //验证码修改密码
    public void updatePasswordByPhone(UpdatePasswordBySmsReq req) {
        OrgEmployee employee = employeeRepository.findById(SecurityUtils.getUserId()).orElseThrow(() -> new BizException("用户不存在"));

        String smsCodeKey = RedisConstants.LOGIN_MOBILE_CODE
                + employee.getMobile() + RedisConstants.REDIS_KEY_SPLIT + RedisConstants.CODE;
        String code = redisTemplate.opsForValue().get(smsCodeKey) != null
                ? Objects.requireNonNull(redisTemplate.opsForValue().get(smsCodeKey)).toString() : null;
        if (!req.getSmsCode().equals(code)) {
            throw new BizException("", "验证码错误");
        }

        employee.setPwd(this.passwordEncoder.encode(req.getPassword()));
        this.employeeRepository.save(employee);
        redisTemplate.delete(smsCodeKey);
    }

    //验证码修改支付密码
    public void updatePayPasswordByPhone(UpdatePasswordBySmsReq req) {
        OrgEmployee employee = employeeRepository.findById(SecurityUtils.getUserId()).orElseThrow(() -> new BizException("用户不存在"));

        String smsCodeKey = RedisConstants.LOGIN_MOBILE_CODE
                + employee.getMobile() + RedisConstants.REDIS_KEY_SPLIT + RedisConstants.CODE;
        String code = redisTemplate.opsForValue().get(smsCodeKey) != null
                ? Objects.requireNonNull(redisTemplate.opsForValue().get(smsCodeKey)).toString() : null;
        if (!req.getSmsCode().equals(code)) {
            throw new BizException("", "验证码错误");
        }

        EmployeeWallet wallet;
        if (null != employee.getWallet()) {
            wallet = employee.getWallet();
        } else {
            wallet = new EmployeeWallet(BigDecimal.ZERO);
        }

        wallet.setPayPassword(this.passwordEncoder.encode(req.getPassword()));
        this.walletRepository.save(wallet);

        employee.setWallet(wallet);
        this.employeeRepository.save(employee);

        redisTemplate.delete(smsCodeKey);
    }

    public Set<String> getPermissions() {
        SecurityUserDetails userDetail = SecurityUtils.getCurrentUser();
        List<SysRole> roles = sysRoleRepository.findByEmployeesId(userDetail.getEmpId());
        Set<String> permissions = new HashSet<>();
        for (SysRole role : roles) {
            Set<String> collect = role.getPermissions().stream().map(SysPermission::getPermission).collect(Collectors.toSet());
            permissions.addAll(collect);
        }
        return permissions;
    }
}
