package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.protocol.DataSourceInfoDTO;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.common.protocol.VCodeDTO;
import com.iotinall.canteen.common.security.AuthorizationTokenFilter;
import com.iotinall.canteen.common.security.SecurityUserDetails;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.dto.auth.EditPwdReq;
import com.iotinall.canteen.dto.auth.LoginReq;
import com.iotinall.canteen.service.AuthService;
import com.iotinall.canteen.service.UserService;
import com.iotinall.canteen.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Security;

/**
 * 登录、登出、获取当前登录用户信息
 *
 * @author xin-bing
 * @date 10/26/2019 11:43
 */
@Api(tags = "登录相关接口")
@RestController
@RequestMapping("auth")
public class AuthController {
    @Resource
    private AuthService authService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 登录接口
     *
     * @author xin-bing
     * @date 2019-10-26 11:39:49
     */
    @CrossOrigin
    @ApiOperation(value = "登录", notes = "根据用户名、密码登录")
    @PostMapping(value = "login")
    public ResultDTO<SecurityUserDetails> login(@Valid @RequestBody LoginReq loginReq) {
        SecurityUserDetails userDetails = authService.login(loginReq);
        return ResultDTO.success(userDetails);
    }

    /**
     * 登出接口
     *
     * @author xin-bing
     * @date 2019-10-26 11:39:49
     */
    @ApiOperation(value = "登出", notes = "登出接口")
    @PostMapping(value = "logout")
    public ResultDTO<?> logout(HttpServletRequest request) {
        String token = request.getHeader(AuthorizationTokenFilter.TOKEN_KEY);
        redisTemplate.delete(token);
        return ResultDTO.success();
    }

    /**
     * 登录接口
     *
     * @author xin-bing
     * @date 2019-10-26 11:39:49
     */
    @ApiOperation(value = "获取当前登录用户", notes = "获取当前登录用户信息")
    @GetMapping(value = "info")
    public ResultDTO<SecurityUserDetails> getUserInfo() {
        String token = SecurityUtils.getCurrentUser().getToken();
        SecurityUserDetails userDetails = (SecurityUserDetails) redisTemplate.opsForValue().get(token);
        UserService.addDataSources(userDetails.getSourceInfo(), userDetails.getRoleTenantOrgIds());
        redisTemplate.opsForValue().set(token, userDetails);
        return ResultDTO.success(userDetails);
    }

    /**
     * 获取所有餐厅
     * @return
     */
    @GetMapping(value = "canteen_info")
    public ResultDTO<SecurityUserDetails> getAllCanteenInfo(String menu){
        String token = SecurityUtils.getCurrentUser().getToken();
        SecurityUserDetails userDetails = (SecurityUserDetails) redisTemplate.opsForValue().get(token);
        UserService.addCanteenDataSources(userDetails.getSourceInfo(), menu);
        redisTemplate.opsForValue().set(token, userDetails);
        return ResultDTO.success(userDetails);
    }

    /**
     * 切换数据源
     * @return
     */
    @PostMapping(value = "change_canteen")
    public ResultDTO<?> changeCanteen(@RequestParam String menu, HttpServletRequest request){
        if(StringUtils.isBlank(menu)){
            throw new BizException("", "请选择要切换的餐厅");
        }
        String token = AuthorizationTokenFilter.getToken(request);
        SecurityUserDetails userDetails = (SecurityUserDetails) redisTemplate.opsForValue().get(token);
        userDetails.getSourceInfo().setMenu(menu);
        redisTemplate.opsForValue().set(token, userDetails);
        return ResultDTO.success(userDetails);
    }

    /**
     * 切换数据源
     * @return
     */
    @PostMapping(value = "change_datasource")
    public ResultDTO<?> changeDataSource(@RequestBody DataSourceInfoDTO req, HttpServletRequest request){
        String token = AuthorizationTokenFilter.getToken(request);
        SecurityUserDetails userDetails = (SecurityUserDetails) redisTemplate.opsForValue().get(token);
        DataSourceInfoDTO dataSourceInfoDTO = userDetails.getSourceInfo();
        if(StringUtils.isNotBlank(req.getStock())){
            dataSourceInfoDTO.setStock(req.getStock());
        }
        if(StringUtils.isNotBlank(req.getKitchen())){
            dataSourceInfoDTO.setKitchen(req.getKitchen());
        }
        if(StringUtils.isNotBlank(req.getMenu())){
            dataSourceInfoDTO.setMenu(req.getMenu());
        }
        redisTemplate.opsForValue().set(token, userDetails);
        return ResultDTO.success(userDetails);
    }

    /**
     * 获取验证码接口
     *
     * @author xin-bing
     * @date 2019-10-26 11:39:49
     */
    @ApiOperation(value = "获取验证码", notes = "获取验证码")
    @GetMapping(value = "verify-code")
    public ResultDTO<VCodeDTO> getVCode() {
        return ResultDTO.failed("-1", "暂时不做验证码登录");
    }

    @PostMapping(value = "edit-pwd")
    public ResultDTO<?> editPwd(@RequestBody EditPwdReq req) {
        authService.editPwd(req);
        return ResultDTO.success();
    }
}

