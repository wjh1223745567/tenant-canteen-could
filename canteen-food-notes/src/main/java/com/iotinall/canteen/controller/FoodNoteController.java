package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.foodnote.FoodNoteAddReq;
import com.iotinall.canteen.dto.foodnote.FoodNoteAppQueryCriteria;
import com.iotinall.canteen.dto.foodnote.FoodNoteDTO;
import com.iotinall.canteen.dto.foodnote.FoodNoteEditReq;
import com.iotinall.canteen.dto.foodnote.FoodNoteQueryCriteria;
import com.iotinall.canteen.dto.foodnotetype.FoodNoteTypeAddReq;
import com.iotinall.canteen.service.FoodNoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @description: 美食笔记控制器
 * @author: JoeLau
 * @time: 2021年07月06日 19:38:36
 */
@Api
@RestController
@RequestMapping("/food_notes/notes")
public class FoodNoteController {
    @Resource
    FoodNoteService foodNoteService;

    @GetMapping
    @ApiOperation(value = "查询分页", notes = "获取美食笔记分页", response = FoodNoteDTO.class)
    public ResultDTO page(FoodNoteQueryCriteria criteria, @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return ResultDTO.success(foodNoteService.page(criteria, pageable));
    }

    @GetMapping("/app")
    @ApiOperation(value = "查询分页", notes = "获取美食笔记list", response = FoodNoteDTO.class)
    public ResultDTO appPage(FoodNoteAppQueryCriteria criteria, @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultDTO.success(foodNoteService.appPage(criteria, pageable));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "查看详情", notes = "查看笔记详情,不加阅读量")
    public ResultDTO detail(@RequestParam Long id) {
        return ResultDTO.success(this.foodNoteService.detail(id));
    }

    @GetMapping("/author")
    @ApiOperation(value = "查询分页", notes = "个人作者获取美食笔记分页", response = FoodNoteDTO.class)
    public ResultDTO authorPage(Boolean status, @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultDTO.success(foodNoteService.authorPage(status, pageable));
    }

    @PostMapping
    @ApiOperation(value = "添加", notes = "添加新的美食笔记")
    public ResultDTO create(@Valid @RequestBody FoodNoteAddReq req) {
        this.foodNoteService.create(req);
        return ResultDTO.success();
    }

    @PutMapping
    @ApiOperation(value = "编辑", notes = "编辑美食笔记")
    public ResultDTO update(@Valid @RequestBody FoodNoteEditReq req) {
        this.foodNoteService.update(req);
        return ResultDTO.success();
    }

    @DeleteMapping
    @ApiOperation(value = "批量删除", notes = "删除美食笔记")
    public ResultDTO delete(@ApiParam(name = "id", value = "需要删除的id，逗号分隔", required = true) @RequestParam(value = "id") Long[] id) {
        this.foodNoteService.delete(id);
        return ResultDTO.success();
    }

    @PutMapping("/read")
    @ApiOperation(value = "阅读", notes = "阅读接口，阅读量加一")
    public ResultDTO read(@RequestParam Long id) {
        return ResultDTO.success(this.foodNoteService.read(id));
    }

    @PostMapping("/follow")
    @ApiOperation(value = "关注", notes = "关注或取消关注该美食笔记作者")
    public ResultDTO follow(@RequestParam Long id, @RequestParam Boolean status) {
        this.foodNoteService.follow(id, status);
        return ResultDTO.success();
    }

    @PostMapping("/collect")
    @ApiOperation(value = "收藏", notes = "收藏或取消收藏该美食笔记")
    public ResultDTO collect(@RequestParam Long id, @RequestParam Boolean status) {
        this.foodNoteService.collect(id, status);
        return ResultDTO.success();
    }

    @PostMapping("/like")
    @ApiOperation(value = "点赞", notes = "点赞或取消点赞该美食笔记")
    public ResultDTO like(@RequestParam Long id, @RequestParam Boolean status) {
        this.foodNoteService.like(id, status);
        return ResultDTO.success();
    }

}
