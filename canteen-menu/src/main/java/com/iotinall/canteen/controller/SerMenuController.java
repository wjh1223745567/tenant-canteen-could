package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.CursorPageDTO;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.dto.menu.MenuMainDto;
import com.iotinall.canteen.dto.messdaily.MessDailyMenuItemListDTO;
import com.iotinall.canteen.dto.messdaily.ProductEvaluationDetailsDto;
import com.iotinall.canteen.dto.messdailymenu.AppMessDailyMenuAddReq;
import com.iotinall.canteen.dto.recommend.MessMenuRecommendReq;
import com.iotinall.canteen.dto.recommend.MessProductCommentAddReq;
import com.iotinall.canteen.dto.recommend.RecommendDto;
import com.iotinall.canteen.service.AppSerMenuService;
import com.iotinall.canteen.service.FeignEmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * 服务页面相关接口
 *
 * @author WJH
 * @date 2019/11/510:32
 */
@Api(tags = SwaggerModule.MODULE_PRODUCT)
@RestController
@RequestMapping(value = "ser/menu")
public class SerMenuController {
    @Resource
    private AppSerMenuService serMenuService;

    @Resource
    private FeignEmployeeService feignEmployeeService;

    /**
     * 新增mess_daily_menu
     *
     * @author xin-bing
     * @date 2019-10-22 16:10:53
     */
    @ApiOperation(value = "安排每日菜谱", notes = "安排每日菜谱", httpMethod = "POST")
    @PostMapping(value = "daily/create")
    public ResultDTO addDailyMenu(@Valid @RequestBody AppMessDailyMenuAddReq req) {
        serMenuService.addDailyMenu(req);
        return ResultDTO.success();
    }

    /**
     * 查询厨师
     *
     * @return
     */
    @ApiOperation(value = "安排菜谱", notes = "查询厨师")
    @GetMapping(value = "find_cook")
    public ResultDTO<?> findCook() {
        return ResultDTO.success(this.feignEmployeeService.findAllCook());
    }

    /**
     * 新增mess_daily_menu
     *
     * @author xin-bing
     * @date 2019-10-22 16:10:53
     */
    @ApiOperation(value = "移除每日菜谱", notes = "移除每日菜谱", httpMethod = "POST")
    @PostMapping(value = "daily/delete")
    public ResultDTO delDailyMenu(@ApiParam(name = "ids", value = "需要删除的id，逗号分隔", required = true) @RequestParam(value = "ids") Set<Long> ids) {
        serMenuService.delDailyMenu(ids);
        return ResultDTO.success();
    }

    @ApiOperation(value = "每周菜谱查询", notes = "根据时间yyyy-MM-dd 查询菜品", response = MenuMainDto.class)
    @GetMapping(value = "listByDate")
    public ResultDTO<MenuMainDto> listMenu(@RequestParam LocalDate date) {
        return ResultDTO.success(this.serMenuService.listMenu(date));
    }

    @ApiOperation(value = "获取所有的菜谱")
    @GetMapping(value = "all")
    public ResultDTO<List<MessDailyMenuItemListDTO>> listData(@RequestParam(value = "date") LocalDate date,
                                                              @ApiParam(value = "餐次", allowableValues = "1,2,4")
                                                              @RequestParam(value = "mealType", required = false) Integer mealType) {
        List<MessDailyMenuItemListDTO> list = serMenuService.listProductForKitchen(date, mealType);
        return ResultDTO.success(list);
    }

    /**
     * 推荐
     *
     * @param date
     * @return
     */
    @GetMapping(value = "listProductsForRecommend")
    public ResultDTO<?> getAllProducts(@RequestParam(value = "date") String date) {
        Long currUserID = SecurityUtils.getUserId();
        return ResultDTO.success(serMenuService.listForRecommend(date, currUserID));
    }

    @ApiOperation(value = "推荐菜品", notes = "点击推荐按钮推荐菜品,已推荐过菜品不能再次推荐")
    @PostMapping(value = "toggleRecommend")
    public ResultDTO<?> toggleRecommend(@Valid @RequestBody MessMenuRecommendReq req) {
        return ResultDTO.success(serMenuService.toggleRecommend(req));
    }

    @ApiOperation(value = "点击菜品查看详情", notes = "根据 用户ID 菜品ID 查询评论", response = ProductEvaluationDetailsDto.class)
    @GetMapping(value = "getProductView")
    public ResultDTO<ProductEvaluationDetailsDto> getProductView(@RequestParam Long productId) {
        return ResultDTO.success(this.serMenuService.getProductView(productId));
    }

    @ApiOperation(value = "菜品详情 查询评论 带分页", notes = "用户 ID 及 菜品ID 分页数字（第一次可以为空）", response = RecommendDto.class)
    @GetMapping(value = "pageCommentByProduct")
    public ResultDTO<CursorPageDTO<RecommendDto>> pageCommentByProduct(@RequestParam Long productId, Long cursor) {
        Long empid = SecurityUtils.getUserId();
        return ResultDTO.success(this.serMenuService.pageCommentByProduct(empid, productId, cursor));
    }

    @ApiOperation(value = "评论页面 赞接口", notes = "点击赞 加一 不能重复赞")
    @GetMapping(value = "addFavor")
    public ResultDTO<?> addFavor(@RequestParam Long commid) {
        Long empid = SecurityUtils.getUserId();
        this.serMenuService.addFavor(commid, empid);
        return ResultDTO.success();
    }

    @ApiOperation(value = "评论页面 取消赞接口")
    @GetMapping(value = "subFavor")
    public ResultDTO<?> subFavor(@RequestParam Long commid) {
        this.serMenuService.subFavor(commid);
        return ResultDTO.success();
    }

    @ApiOperation(value = "评论页面 踩接口", notes = "点击踩 不能重复踩")
    @GetMapping(value = "addOpposite")
    public ResultDTO<?> addOpposite(@RequestParam Long commid) {
        Long empid = SecurityUtils.getUserId();
        this.serMenuService.addOpposite(commid, empid);
        return ResultDTO.success();
    }

    @ApiOperation(value = "新增评论", notes = "添加菜品评论")
    @PostMapping(value = "addComment")
    @PreAuthorize("hasAnyRole('ADMIN','APP_MRCP','APP_MRCP_DP')")
    public ResultDTO<?> addComment(@Valid @RequestBody MessProductCommentAddReq req) {
        Long empid = SecurityUtils.getUserId();
        this.serMenuService.addComment(req, empid);
        return ResultDTO.success();
    }

    /**
     * 删除评论
     *
     * @return
     */
    @PostMapping(value = "subComment/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','APP_MRCP','APP_MRCP_DP')")
    public ResultDTO<?> subComment(@PathVariable(value = "id") Long id) {
        this.serMenuService.subComment(id);
        return ResultDTO.success();
    }


    @ApiOperation(value = "厨师详情", notes = "厨师详情")
    @GetMapping(value = "cook_view")
    public ResultDTO<?> cookView(@RequestParam("itemId") Long id) {
        return ResultDTO.success(this.serMenuService.cookView(id));
    }


    @ApiOperation(value = "添加饮食记录获取早/中/晚餐菜谱列表")
    @GetMapping("type_list")
    public ResultDTO getMenuList(@RequestParam("type") Integer type, @RequestParam("date") LocalDate date) {
        return ResultDTO.success(serMenuService.findMenuList(type, date));
    }
}
