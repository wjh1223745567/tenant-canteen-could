package com.iotinall.canteen.controller;


import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.dto.feedback.FeignFeedbackDTO;
import com.iotinall.canteen.dto.messfeedback.MessFeedbackDTO;
import com.iotinall.canteen.dto.messfeedback.MessFeedbackHandleReq;
import com.iotinall.canteen.dto.messfeedback.MessFeedbackQueryCriteria;
import com.iotinall.canteen.service.SysMessFeedbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 意见反馈 Controller
 *
 * @author xin-bing
 * @date 2019-10-26 17:23:08
 */
@Api(tags = SwaggerModule.MODULE_FEEDBACK)
@RestController
@RequestMapping("mess/feedbacks")
public class MessFeedbackController {

    @Resource
    private SysMessFeedbackService messFeedBackService;

    /**
     * 查询意见列表
     *
     * @author xin-bing
     * @date 2019-10-26 17:23:08
     */
    @ApiOperation(value = "意见反馈列表", notes = "根据条件查询列表")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','FEEDBACK_ALL','FEEDBACK')")
    public ResultDTO<PageDTO<MessFeedbackDTO>> list(MessFeedbackQueryCriteria criteria, Pageable pageable) {
        return ResultDTO.success(messFeedBackService.pageMessFeedback(criteria, pageable));
    }

    /**
     * 修改mess_feedback
     *
     * @author xin-bing
     * @date 2019-10-26 17:23:08
     */
    @ApiOperation(value = "处理", notes = "处理意见反馈")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','FEEDBACK_ALL','FEEDBACK_EDIT')")
    public ResultDTO handle(@Valid @RequestBody MessFeedbackHandleReq req) {
        messFeedBackService.handle(req);
        return ResultDTO.success();
    }

    /**
     * 删除mess_feedback
     *
     * @author xin-bing
     * @date 2019-10-26 17:23:08
     */
    @ApiOperation(value = "删除", notes = "删除意见反馈")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FEEDBACK_ALL','FEEDBACK_DELETE')")
    public ResultDTO delete(@ApiParam(name = "id", value = "需要删除的id", required = true) @PathVariable Long id) {
        messFeedBackService.delete(id);
        return ResultDTO.success();
    }

    /**
     * 批量删除mess_feedback
     *
     * @author xin-bing
     * @date 2019-10-26 17:23:08
     */
    @ApiOperation(value = "批量删除", notes = "批量删除意见反馈")
    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','FEEDBACK_ALL','FEEDBACK_DELETE')")
    public ResultDTO deleteBatch(@ApiParam(name = "batch", value = "需要删除的id，逗号分隔", required = true) @RequestParam(value = "batch") Long[] ids) {
        messFeedBackService.batchDelete(ids);
        return ResultDTO.success();
    }

    @GetMapping(value = "/feign/getFeedbackList")
    public List<FeignFeedbackDTO> getFeedbackList() {
        return this.messFeedBackService.getFeedbackList();
    }
}