package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.foodnotetype.FoodNoteTypeAddReq;
import com.iotinall.canteen.dto.foodnotetype.FoodNoteTypeDTO;
import com.iotinall.canteen.dto.foodnotetype.FoodNoteTypeEditReq;
import com.iotinall.canteen.dto.foodnotetype.FoodNoteTypeQueryCriteria;
import com.iotinall.canteen.entity.FoodNoteType;
import com.iotinall.canteen.service.FoodNoteTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
import javax.validation.Valid;

/**
 * @description: 美食笔记类型Controller
 * @author: JoeLau
 * @time: 2021年07月06日 19:53:07
 */
@Api
@RestController
@RequestMapping("/food_notes/types")
public class FoodNoteTypeController {
    @Resource
    FoodNoteTypeService foodNoteTypeService;

    @GetMapping
    @ApiOperation(value = "查询分页", notes = "获取美食笔记类型分页", response = FoodNoteTypeDTO.class)
    public ResultDTO page(FoodNoteTypeQueryCriteria criteria, @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return ResultDTO.success(foodNoteTypeService.page(criteria, pageable));
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询列表", notes = "获取美食笔记类型列表", response = FoodNoteTypeDTO.class)
    public ResultDTO getAll() {
        return ResultDTO.success(foodNoteTypeService.getAll());
    }

    @PostMapping
    @ApiOperation(value = "添加", notes = "添加新的美食笔记类型")
    public ResultDTO create(@Valid @RequestBody FoodNoteTypeAddReq req) {
        this.foodNoteTypeService.create(req);
        return ResultDTO.success();
    }

    @PutMapping
    @ApiOperation(value = "编辑", notes = "编辑美食笔记类型")
    public ResultDTO update(@Valid @RequestBody FoodNoteTypeEditReq req) {
        this.foodNoteTypeService.update(req);
        return ResultDTO.success();
    }

    @DeleteMapping
    @ApiOperation(value = "批量删除", notes = "删除美食笔记类型")
    public ResultDTO delete(@ApiParam(name = "id", value = "需要删除的id，逗号分隔", required = true) @RequestParam(value = "id") Long[] id) {
        this.foodNoteTypeService.delete(id);
        return ResultDTO.success();
    }

}
