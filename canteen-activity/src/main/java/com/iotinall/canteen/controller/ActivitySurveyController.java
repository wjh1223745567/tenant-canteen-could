package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.protocol.ActivitySurveyAddReq;
import com.iotinall.canteen.protocol.ActivitySurveyEditReq;
import com.iotinall.canteen.protocol.ActivitySurveyQueryCriteria;
import com.iotinall.canteen.protocol.StickyAndEnableReq;
import com.iotinall.canteen.service.ActivitySurveyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * 投票活动、问卷调查控制器
 *
 * @author joelau
 * @date 2021/05/29 16:33
 */
@Api
@RestController
@RequestMapping("/activity_survey")
public class ActivitySurveyController {
    @Resource
    private ActivitySurveyService activitySurveyService;

    @GetMapping
    @ApiOperation(value = "查询", notes = "查询所有的投票活动或者问卷调查")
    public ResultDTO list(ActivitySurveyQueryCriteria criteria, @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultDTO.success(activitySurveyService.page(criteria, pageable));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','INTERACTIVE_MANAGEMENT','QS_EDIT','VOTE_EDIT')")
    @ApiOperation(value = "添加", notes = "添加新的投票活动或问卷调查")
    public ResultDTO create(@Valid @RequestBody ActivitySurveyAddReq req) {
        this.activitySurveyService.create(req);
        return ResultDTO.success();
    }

    @PutMapping
    @ApiOperation(value = "编辑", notes = "编辑新的投票活动或问卷调查")
    @PreAuthorize("hasAnyRole('ADMIN','INTERACTIVE_MANAGEMENT','QS_EDIT','VOTE_EDIT')")
    public ResultDTO update(@Valid @RequestBody ActivitySurveyEditReq req) {
        this.activitySurveyService.update(req);
        return ResultDTO.success();
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','INTERACTIVE_MANAGEMENT','QS_EDIT','VOTE_EDIT')")
    @ApiOperation(value = "删除", notes = "删除指定的投票活动或者问卷调查")
    public ResultDTO delete(@RequestParam String id) {
        this.activitySurveyService.delete(id);
        return ResultDTO.success();
    }

    @GetMapping("/statistic")
    @ApiOperation(value = "统计", notes = "统计概括")
    public ResultDTO statisticList(@RequestParam Long id) {
        return ResultDTO.success(this.activitySurveyService.statisticList(id));
    }

    @GetMapping("/statistic/subject")
    @ApiOperation(value = "选择题选项统计", notes = "统计投票活动中的选择题答题数据")
    public ResultDTO statisticSubjectList(@RequestParam Long id) {
        return ResultDTO.success(this.activitySurveyService.statisticSubjectList(id));
    }

    @GetMapping("/statistic/textAnswer")
    @ApiOperation(value = "问答题统计", notes = "统计投票活动中的问答题答题数据")
    public ResultDTO statisticTextAnswer(@RequestParam Long id) {
        return ResultDTO.success(this.activitySurveyService.statisticTextAnswer(id));
    }

    @PutMapping("sticky")
    @ApiOperation(value = "置顶/取消置顶", notes = "置顶/取消置顶")
    @PreAuthorize("hasAnyRole('ADMIN','INTERACTIVE_MANAGEMENT','QS_EDIT','VOTE_EDIT')")
    public ResultDTO sticky(@Valid @RequestBody StickyAndEnableReq req) {
        this.activitySurveyService.sticky(req);
        return ResultDTO.success();
    }

    @PutMapping("enable")
    @ApiOperation(value = "启用/禁用", notes = "启用/禁用")
    @PreAuthorize("hasAnyRole('ADMIN','INTERACTIVE_MANAGEMENT','QS_EDIT','VOTE_EDIT')")
    public ResultDTO enable(@Valid @RequestBody StickyAndEnableReq req) {
        this.activitySurveyService.enable(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "导出答题统计合计", notes = "导出答题统计合计", httpMethod = "POST")
    @PostMapping("/statistic/export/{id}")
    public void exportSum(HttpServletResponse response, @PathVariable("id")  Long id) throws IOException {
        this.activitySurveyService.statisticExport(response, id);
    }
}
