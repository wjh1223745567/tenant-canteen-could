package com.iotinall.canteen.controller;


import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.dto.messprod.MessProductSimpleDTO;
import com.iotinall.canteen.dto.messprod.SysMessProductCommentDTO;
import com.iotinall.canteen.dto.recommend.MessProductCommentAddReq;
import com.iotinall.canteen.service.SysMessProductCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 菜品 Controller
 *
 * @author xin-bing
 * @date 2019-10-21 16:07:57
 */
@Api(tags = SwaggerModule.MODULE_PRODUCT_COMMENT)
@RestController
@RequestMapping("mess/products/comment")
public class MessProductCommentController {
    @Resource
    private SysMessProductCommentService messProductCommentService;

    @ApiOperation(value = "菜品评论分页列表", notes = "菜品评论分页列表", response = SysMessProductCommentDTO.class)
    @GetMapping
    public ResultDTO page(@RequestParam Long productId, @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable page) {
        return ResultDTO.success(this.messProductCommentService.page(productId, page));
    }

    @ApiOperation(value = "菜品评论 菜品详情", notes = "菜品评论 菜品详情", response = MessProductSimpleDTO.class)
    @GetMapping("detail")
    public ResultDTO detail(@RequestParam Long productId, @RequestParam Integer mealType) {
        return ResultDTO.success(this.messProductCommentService.detail(productId, mealType));
    }

    @ApiOperation(value = "新增评论", notes = "添加菜品评论", httpMethod = "POST")
    @PostMapping
    public ResultDTO<?> addComment(@Valid @RequestBody MessProductCommentAddReq req) {
        this.messProductCommentService.addComment(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "赞", notes = "赞，匿名，每隔4s可点击一次", httpMethod = "POST")
    @PostMapping(value = "favor/add/{id}")
    public ResultDTO addFavor(@PathVariable("id") Long id) {
        this.messProductCommentService.addFavor(id);
        return ResultDTO.success();
    }

    @ApiOperation(value = "评论页面 取消赞", notes = "取消赞，匿名，每隔4s可点击一次", httpMethod = "POST")
    @PostMapping(value = "favor/cancel/{id}")
    public ResultDTO subFavor(@PathVariable("id") Long id) {
        this.messProductCommentService.cancelFavor(id);
        return ResultDTO.success();
    }

    @ApiOperation(value = "评论页面 踩", notes = "踩 ,匿名，每隔4s可点击一次", httpMethod = "POST")
    @PostMapping(value = "opposite/add/{id}")
    public ResultDTO<?> addOpposite(@PathVariable("id") Long id) {
        this.messProductCommentService.addOpposite(id);
        return ResultDTO.success();
    }

    @ApiOperation(value = "评论页面 踩接口", notes = "点击踩 ,匿名，每隔4s可点击一次", httpMethod = "POST")
    @PostMapping(value = "opposite/cancel/{id}")
    public ResultDTO<?> cancelOpposite(@PathVariable("id") Long id) {
        this.messProductCommentService.cancelOpposite(id);
        return ResultDTO.success();
    }
}