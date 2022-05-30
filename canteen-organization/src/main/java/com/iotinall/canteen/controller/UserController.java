package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.common.security.SecurityUserDetails;
import com.iotinall.canteen.dto.sms.MobileLoginReq;
import com.iotinall.canteen.dto.sms.UpdatePasswordBySmsReq;
import com.iotinall.canteen.dto.wxdata.WxDataReq;
import com.iotinall.canteen.service.BaseWxService;
import com.iotinall.canteen.service.UserService;
import com.iotinall.canteen.utils.AesEncryptUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import weixin.popular.bean.sns.Jscode2sessionResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WJH
 * @date 2019/11/910:17
 */
@Slf4j
@Api(tags = "用户登陆 验证码等接口")
@RestController
@RequestMapping(value = "user")
@RefreshScope
public class UserController {

    @Resource
    private BaseWxService baseWxService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private UserService userService;

    @Value("${canteen.mini.login-by-pwd:true}")
    private Boolean loginByPwd;

    @ApiOperation(value = "获取验证码")
    @GetMapping(value = "getRandomCodeImage")
    public ResultDTO<?> getRandomCodeImage(HttpServletRequest request, HttpServletResponse response) {
        return ResultDTO.success(this.userService.getRandomCodeImage(request, response));
    }

    @ApiOperation(value = "登陆接口")
    @GetMapping(value = "login")
    public ResultDTO<SecurityUserDetails> login(@RequestParam("phone") String phone,
                                                @RequestParam("password") String password,
                                                @RequestParam("validCode") String validCode,
                                                @RequestParam("key") String key,
                                                @RequestParam(value = "openid", required = false) String openId) {
        //测试账号
        if (phone.equals("18304040404") || loginByPwd) {
            Object histValidCode = redisTemplate.opsForValue().get(key);
            if(histValidCode == null){
                throw new BizException("", "验证码以过期，请点击刷新");
            }
            return ResultDTO.success(this.userService.login(validCode, histValidCode.toString(), phone, password, openId));
        }
        throw new BizException("", "暂不支持密码登录，请使用短信登录！");
    }

    @CrossOrigin
    @ApiOperation(value = "登陆接口")
    @GetMapping(value = "login/no-code")
    public ResultDTO<SecurityUserDetails> loginWithoutValidCode(@RequestParam("phone") String phone, @RequestParam("password") String password) {
        return ResultDTO.success(this.userService.login(phone, password));
    }

    @ApiOperation(value = "微信小程序授权 返回值openid    used 为true 表示已经登陆过  false 跳转登陆页面")
    @GetMapping(value = "wxAuthorize")
    public ResultDTO<?> wxAuthorize(@RequestParam String code) {
        Jscode2sessionResult result = baseWxService.getUserOpenId(code);
        SecurityUserDetails dto = this.userService.loadDataByOpenId(result.getOpenid());
        Map<String, Object> map = new HashMap<>();
        map.put("openid", result.getOpenid());
        map.put("userInfo", dto);
        map.put("sessionKey", result.getSession_key());
        return ResultDTO.success(map);
    }

    /**
     * 解密微信数据
     *
     * @return
     */
    @PostMapping(value = "decrypt_we_chat_data")
    public ResultDTO<?> decryptWeChatData(@Valid @RequestBody WxDataReq req) {
        return ResultDTO.success(AesEncryptUtils.decrypt(req.getEncryptedData(), req.getCode(), req.getIv()));
    }

    @ApiOperation(value = "登出接口")
    @GetMapping(value = "signOut")
    public ResultDTO<?> signOut(HttpServletRequest request) {
        this.userService.signOut(request);
        return ResultDTO.success();
    }

    @ApiOperation(value = "获取手机验证码")
    @GetMapping(value = "mobile/code")
    public ResultDTO<?> getLoginMobileCode(@RequestParam("mobile") String mobile, @RequestParam Integer type) {
        return ResultDTO.success(this.userService.getLoginMobileCode(mobile, type));
    }

    @ApiOperation(value = "短信验证码修改密码")
    @PostMapping(value = "mobile/changepassword")
    public ResultDTO<?> changePasswordByPhone(@Valid @RequestBody UpdatePasswordBySmsReq smsReq) {
        this.userService.changePasswordByPhone(smsReq);
        return ResultDTO.success();
    }

    @ApiOperation(value = "通过手机号登录")
    @PostMapping(value = "login/mobile")
    public ResultDTO<?> loginByMobile(@RequestBody MobileLoginReq req) {
        return ResultDTO.success(this.userService.loginByMobile(req));
    }
}
