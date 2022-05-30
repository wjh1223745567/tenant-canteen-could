package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.messfeedback.MessFeedbackTypeReq;
import com.iotinall.canteen.dto.sysdictdetail.SysDictDetailAddReq;
import com.iotinall.canteen.dto.sysdictdetail.SysDictDetailDTO;
import com.iotinall.canteen.dto.sysdictdetail.SysDictDetailEditReq;
import com.iotinall.canteen.service.SysDictDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "意见反馈标签字典表")
@RestController
@RequestMapping(value = "mess/feedbacks_details")
public class FeedbackDetailsController {

    @Resource
    private SysDictDetailService sysDictDetailService;

    private static final String FEED_BACK_TYPE_GROUP = "feed_back";

    /**
     * 获取反馈类型
     *
     * @author xin-bing
     * @date 2019-10-26 17:23:08
     */
    @ApiOperation(value = "反馈类型", notes = "查看反馈类型")
    @GetMapping("types")
    @PreAuthorize("hasAnyRole('ADMIN','FEEDBACK_ALL','FEEDBACK_TYPE')")
    public ResultDTO getFeedBackTypes() {
        List<SysDictDetailDTO> sysDictDetails = sysDictDetailService.listAll(FEED_BACK_TYPE_GROUP);
        return ResultDTO.success(sysDictDetails);
    }

    /**
     * 添加反馈类型
     *
     * @author xin-bing
     * @date 2019-10-26 17:23:08
     */
    @ApiOperation(value = "新增反馈类型", notes = "新增反馈类型")
    @PostMapping("types")
    @PreAuthorize("hasAnyRole('ADMIN','FEEDBACK_ALL','FEEDBACK_TYPE_EDIT')")
    public ResultDTO addFeedBackTypes(@Valid @RequestBody MessFeedbackTypeReq.AddReq req) {
        SysDictDetailAddReq dictReq = new SysDictDetailAddReq();
        dictReq.setLabel(req.getLabel());
        dictReq.setValue(req.getLabel());
        dictReq.setSort(0);
        dictReq.setGroupCode(FEED_BACK_TYPE_GROUP);
        dictReq.setRemark(req.getRemark());
        sysDictDetailService.add(dictReq);
        return ResultDTO.success();
    }

    /**
     * 编辑反馈类型
     *
     * @author xin-bing
     * @date 2019-10-26 17:23:08
     */
    @ApiOperation(value = "编辑反馈类型", notes = "编辑反馈类型")
    @PutMapping("types")
    @PreAuthorize("hasAnyRole('ADMIN','FEEDBACK_ALL','FEEDBACK_TYPE_EDIT')")
    public ResultDTO editFeedBackTypes(@Valid @RequestBody MessFeedbackTypeReq.EditReq req) {
        SysDictDetailEditReq dictReq = new SysDictDetailEditReq();
        dictReq.setId(req.getId());
        dictReq.setLabel(req.getLabel());
        dictReq.setValue(req.getLabel());
        dictReq.setSort(0);
        dictReq.setGroupCode(FEED_BACK_TYPE_GROUP);
        dictReq.setRemark(req.getRemark());
        sysDictDetailService.update(dictReq);
        return ResultDTO.success();
    }

    /**
     * 删除反馈类型
     *
     * @author xin-bing
     * @date 2019-10-26 17:23:08
     */
    @ApiOperation(value = "删除反馈类型", notes = "删除反馈类型")
    @DeleteMapping("types/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FEEDBACK_ALL','FEEDBACK_TYPE_EDIT')")
    public ResultDTO deleteFeedBackType(@ApiParam(name = "id", value = "需要删除的id", required = true) @PathVariable Long id) {
        sysDictDetailService.delete(id);
        return ResultDTO.success();
    }
}
