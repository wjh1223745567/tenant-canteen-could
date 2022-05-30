package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.CursorPageDTO;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.dto.information.*;
import com.iotinall.canteen.service.AppInformationService;
import com.iotinall.canteen.service.SysInformationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 咨询列表信息
 *
 * @author WJH
 * @date 2019/11/110:26
 */
@Api(tags = SwaggerModule.MODULE_INFO)
@RestController
@RequestMapping(value = "information")
public class InformationController {
    @Resource
    private SysInformationService informationService;

    /**
     * 微信小程序
     */
    @Resource
    private AppInformationService appInformationService;


    @GetMapping(value = "findByPage")
    public ResultDTO findByPage(InformationCondition condition, Pageable pageable) {
        return ResultDTO.success(this.informationService.findByPage(condition, pageable));
    }

    @PostMapping(value = "add")
    @PreAuthorize("hasAnyRole('ADMIN','INTERACTIVE_MANAGEMENT','INFORMATION_EDIT')")
    public ResultDTO add(@Valid @RequestBody InformationAddReq req) {
        return ResultDTO.success(this.informationService.add(req));
    }

    @PutMapping(value = "edit")
    @PreAuthorize("hasAnyRole('ADMIN','INTERACTIVE_MANAGEMENT','INFORMATION_EDIT')")
    public ResultDTO edit(@Valid @RequestBody InformationEditReq req) {
        return ResultDTO.success(this.informationService.edit(req));
    }

    @GetMapping(value = "stickyInfo")
    @PreAuthorize("hasAnyRole('ADMIN','INTERACTIVE_MANAGEMENT','INFORMATION_EDIT')")
    public ResultDTO stickyInfo(@RequestParam Long id) {
        return ResultDTO.success(this.informationService.stickyInfo(id));
    }

    @GetMapping(value = "deleted")
    @PreAuthorize("hasAnyRole('ADMIN','INTERACTIVE_MANAGEMENT','INFORMATION_EDIT')")
    public ResultDTO deleted(Long[] id) {
        return ResultDTO.success(this.informationService.deleted(id));
    }



    /**
     * 获取information types
     *
     * @return
     */
    @GetMapping(value = "types")
    public ResultDTO<?> listInformationTypes() {
        return ResultDTO.success(appInformationService.listInformationTypes());
    }

    @GetMapping("listByType")
    public ResultDTO<?> listByType(@RequestParam(value = "typeId") Long typeId,
                                   @RequestParam(value = "pageNum") int pageNum,
                                   @RequestParam(value = "pageSize") int pageSize) {
        return ResultDTO.success(appInformationService.pageByInformationType(typeId, pageNum, pageSize));
    }

    @ApiOperation(value = "获取指定的记录", response = InformationDetailDTO.class)
    @GetMapping("top")
    public ResultDTO<?> listTop5() {
        return ResultDTO.success(this.appInformationService.listTop());
    }

    @ApiOperation(value = "获取最新的10条", response = InformationDetailDTO.class)
    @GetMapping("newest")
    public ResultDTO<?> listNewest10() {
        return ResultDTO.success(appInformationService.listNewest());
    }

    @ApiOperation(value = "咨询评论分页查询接口", response = InformationCommentDto.class)
    @GetMapping(value = "pageByInfoComment")
    public ResultDTO<CursorPageDTO<InformationCommentDto>> pageByInfoComment(Long cursor, @ApiParam(value = "咨询ID", name = "infoId") @RequestParam Long infoId) {
        return ResultDTO.success(this.appInformationService.pageInformationComment(cursor, infoId, SecurityUtils.getUserId()));
    }

    @ApiOperation(value = "赞咨询评论")
    @GetMapping(value = "praiseInfoComment")
    public ResultDTO<?> praiseInfoComment(@ApiParam(value = "评论ID", name = "infoCommentId") @RequestParam Long infoCommentId) {
        this.appInformationService.praiseInfoComment(infoCommentId);
        return ResultDTO.success();
    }

    @ApiOperation(value = "取消咨询评论赞")
    @GetMapping(value = "cancelPraiseInfoComment")
    public ResultDTO<?> cancelPraiseInfoComment(@ApiParam(value = "评论ID", name = "infoCommentId") @RequestParam Long infoCommentId) {
        this.appInformationService.cancelPraiseInfo(infoCommentId);
        return ResultDTO.success();
    }

    @ApiOperation(value = "赞咨询")
    @GetMapping(value = "praiseInfo")
    public ResultDTO<?> praiseInfo(@ApiParam(value = "咨询ID", name = "infoId") @RequestParam Long infoId) {
        this.appInformationService.praiseInfo(infoId);
        return ResultDTO.success();
    }

    @ApiOperation(value = "取消赞咨询")
    @GetMapping(value = "cancelPraiseInfo")
    public ResultDTO<?> cancelPraiseInfo(@ApiParam(value = "咨询ID", name = "infoId") @RequestParam Long infoId) {
        this.appInformationService.cancelAddPraiseInfo(infoId);
        return ResultDTO.success();
    }


    @ApiOperation(value = "添加评论 json格式")
    @PostMapping(value = "addInfoComment")
    public ResultDTO<?> addInfoComment(@Valid @RequestBody InformationCommentAdd add) {
        this.appInformationService.addInfoComment(add);
        return ResultDTO.success();
    }

    /**
     * 移除评论
     *
     * @param id
     * @return
     */
    @PostMapping(value = "removeInfoComment/{id}")
    public ResultDTO<?> removeInfoComment(@PathVariable Long id) {
        this.appInformationService.removeInfoComment(id);
        return ResultDTO.success();
    }

    @ApiOperation(value = "根据ID查询咨询详情", response = InformationDetailDTO.class)
    @GetMapping(value = "findById")
    public ResultDTO<?> findById(@RequestParam Long infoId) {
        return ResultDTO.success(this.appInformationService.findById(infoId));
    }
}
