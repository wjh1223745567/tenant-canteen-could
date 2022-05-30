package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.foodnotecommentrecord.FoodNoteCommentRecordAddReq;
import com.iotinall.canteen.service.FoodNoteCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @description: 美食笔记评论Controller
 * @author: JoeLau
 * @time: 2021年07月08日 11:36:51
 */
@Api
@RestController
@RequestMapping("/food_notes/comments")
public class FoodNoteCommentController {
    @Resource
    FoodNoteCommentService foodNoteCommentService;

    @PostMapping
    @ApiOperation(value = "新建",notes = "新建一个美食笔记评论")
    public ResultDTO create(@Valid @RequestBody FoodNoteCommentRecordAddReq req){
        this.foodNoteCommentService.create(req);
        return ResultDTO.success();
    }

    @DeleteMapping
    @ApiOperation(value = "批量删除",notes = "删除一个美食笔记评论")
    public ResultDTO delete(@ApiParam(name = "id", value = "需要删除的id，逗号分隔", required = true) @RequestParam(value = "id") Long[] id){
        this.foodNoteCommentService.delete(id);
        return ResultDTO.success();
    }

    @PostMapping("/like")
    @ApiOperation(value = "点赞", notes = "点赞该美食笔记评论")
    public ResultDTO like(@RequestParam Long id, @RequestParam Boolean status) {
        this.foodNoteCommentService.like(id,status);
        return ResultDTO.success();
    }

}
