package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.protocol.app.AnswerRecordAddReq;
import com.iotinall.canteen.service.AnswerRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 投票活动、问卷调查控制器
 *
 * @author joelau
 * @date 2021/05/31 20:33
 */
@Api
@RestController
@RequestMapping("/activity_record")
public class AnswerRecordController {
    @Resource
    private AnswerRecordService answerRecordService;

    @GetMapping("/subject")
    @ApiOperation(value = "查询问题", notes = "查询具体的问题详情")
    public ResultDTO getNextSubject(Long surveyId, Integer subjectSeq) {
        return ResultDTO.success(answerRecordService.getNextSubject(surveyId, subjectSeq));
    }

    @PostMapping
    @ApiOperation(value = "添加", notes = "添加新的答案记录")
    public ResultDTO createAnswerRecord(@Valid @RequestBody AnswerRecordAddReq req) {
        this.answerRecordService.createAnswerRecord(req);
        return ResultDTO.success();
    }

    @GetMapping
    @ApiOperation(value = "小程序获取问卷调查或者活动投票列表", notes = "小程序获取问卷调查或者活动投票列表")
    public ResultDTO list(@RequestParam Integer type, @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultDTO.success(answerRecordService.list(SecurityUtils.getUserId(), type, pageable));
    }

    @PostMapping("/finish")
    @ApiOperation(value = "完成答题", notes = "完成答题")
    public ResultDTO finishAnswerRecord(@RequestParam Long id) {
        this.answerRecordService.finishAnswerRecord(id, SecurityUtils.getUserId());
        return ResultDTO.success();
    }

}
