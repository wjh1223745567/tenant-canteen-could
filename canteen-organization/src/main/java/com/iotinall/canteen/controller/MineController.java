package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.constant.SwaggerModule;
import com.iotinall.canteen.dto.mine.UserInfoDto;
import com.iotinall.canteen.dto.sms.UpdatePasswordBySmsReq;
import com.iotinall.canteen.service.MineService;
import com.iotinall.canteen.service.OrgEmployeeService;
import com.iotinall.canteen.service.SerMoneybagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;


@Api(tags = SwaggerModule.MODULE_MINE)
@RestController
@RequestMapping("sys/mine")
public class MineController {

    @Resource
    private MineService mineService;

    @Resource
    private SerMoneybagService serMoneybagService;

    @Resource
    private OrgEmployeeService orgEmployeeService;

    @ApiOperation(value = "查询个人信息", notes = "我的页面信息展示", response = UserInfoDto.class)
    @GetMapping("getUserViewData")
    public ResultDTO<UserInfoDto> getUserViewData() {
        return ResultDTO.success(this.mineService.getUserViewData(SecurityUtils.getUserId()));
    }

    //修改密码
    @GetMapping(value = "updatePassword")
    public ResultDTO updatePassword(@RequestParam() String password, @RequestParam String oldPassword) {
        this.mineService.updatePassword(password, oldPassword);
        return ResultDTO.success();
    }

    @ApiOperation(value = "获取手机验证码")
    @GetMapping(value = "updatePassword/mobile/code")
    public ResultDTO getUpdatePasswordCode(@RequestParam("mobile") String mobile, @RequestParam Integer type) {
        return ResultDTO.success(this.mineService.getUpdatePasswordCode(mobile, type));
    }

    @ApiOperation(value = "短信验证码修改密码", response = UpdatePasswordBySmsReq.class)
    @PostMapping(value = "updatePassword/mobile/")
    public ResultDTO changePasswordByPhone(@Valid @RequestBody UpdatePasswordBySmsReq smsReq) {
        this.mineService.updatePasswordByPhone(smsReq);
        return ResultDTO.success();
    }

    @ApiOperation(value = "短信验证码修改支付密码", response = UpdatePasswordBySmsReq.class)
    @PostMapping(value = "updatePayPassword/mobile/")
    public ResultDTO changePayPasswordByPhone(@Valid @RequestBody UpdatePasswordBySmsReq smsReq) {
        this.mineService.updatePayPasswordByPhone(smsReq);
        return ResultDTO.success();
    }

    @GetMapping(value = "update_name")
    public ResultDTO updateName(@RequestParam String name) {
        this.orgEmployeeService.updateName(name);
        return ResultDTO.success();
    }

    //修改支付密码
    @GetMapping(value = "haveTackOutWxPayPayPassword")
    public ResultDTO haveTackOutWxPayPayPassword() {
        return ResultDTO.success(this.serMoneybagService.haveTackOutWxPayPayPassword());
    }

    /**
     * @return
     */
    @GetMapping(value = "persmissions")
    public ResultDTO<?> getPermission() {
        return ResultDTO.success(mineService.getPermissions());
    }

}
