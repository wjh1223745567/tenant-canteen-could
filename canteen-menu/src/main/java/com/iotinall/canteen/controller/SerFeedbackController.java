package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.CursorPageDTO;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.dto.messfeedback.AppMessFeedbackDto;
import com.iotinall.canteen.dto.messfeedback.MessFeedbackReq;
import com.iotinall.canteen.service.SerFeedBackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author WJH
 * @date 2019/11/613:40
 */

@Api(tags = SwaggerModule.MODULE_FEEDBACK)
@RestController
@RequestMapping(value = "ser/feedback")
public class SerFeedbackController {

    @Resource
    private SerFeedBackService serFeedBackService;

    @ApiOperation(value = "添加意见反馈接口")
    @PostMapping(value = "addFeedback")
    public ResultDTO addFeedback(@Valid @RequestBody MessFeedbackReq req){
        Long empid = SecurityUtils.getUserId();
        this.serFeedBackService.addFeedback(req, empid);
        return ResultDTO.success();
    }

    @ApiOperation(value = "查询意见反馈列表信息 带分页",httpMethod = "GET", response = AppMessFeedbackDto.class)
    @GetMapping(value = "listFeedback")
    public ResultDTO<CursorPageDTO<AppMessFeedbackDto>> listFeedback(Long cursor){
        Long empid = SecurityUtils.getUserId();
        return ResultDTO.success(this.serFeedBackService.listFeedback(cursor, empid));
    }
}
