package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.dto.sysdictdetail.SysDictDetailDTO;
import com.iotinall.canteen.service.SysDictDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author WJH
 * @date 2019/11/1415:37
 */
@Api(tags = SwaggerModule.MODULE_FEEDBACK)
@RestController
@RequestMapping(value = "sys_dict")
public class SysDictDetailController {

    @Resource
    private SysDictDetailService sysDictDetailService;

    @ApiOperation(value = "评论 字典接口", response = SysDictDetailDTO.class)
    @GetMapping(value = "commentTags")
    public ResultDTO findComment(){
        return ResultDTO.success(this.sysDictDetailService.listAll("taste_tag,env_tag"));
    }

    @ApiOperation(value = "意见反馈类型" , response = SysDictDetailDTO.class)
    @GetMapping(value = "findFeedBack")
    public ResultDTO findFeedBack(){
        return  ResultDTO.success(this.sysDictDetailService.listAll("feed_back"));
    }

}
