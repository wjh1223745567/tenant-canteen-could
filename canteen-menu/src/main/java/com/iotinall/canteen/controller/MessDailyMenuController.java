package com.iotinall.canteen.controller;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.dto.messdaily.MessDailyMenuItemListDTO;
import com.iotinall.canteen.dto.messdailymenu.*;
import com.iotinall.canteen.dto.stock.FeignProcurementDto;
import com.iotinall.canteen.service.FeignTenantOrganizationService;
import com.iotinall.canteen.service.SysMessDailyMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

/**
 * mess_daily_menu Controller
 *
 * @author xin-bing
 * @date 2019-10-22 16:10:53
 */
@Api(tags = SwaggerModule.MODULE_PRODUCT)
@RestController
@RequestMapping("mess/daily-menus")
public class MessDailyMenuController {
    @Resource
    private SysMessDailyMenuService sysMessDailyMenuService;

    @Resource
    private FeignTenantOrganizationService feignTenantOrganizationService;

    /**
     * 查询mess_daily_menu列表
     *
     * @author xin-bing
     * @date 2019-10-22 16:10:53listProducts
     */
    @ApiOperation(value = "根据查询条件查询菜谱", notes = "根据条件查询每日菜谱")
    @GetMapping
    public ResultDTO<MessDailyMenuDTO> list(@Valid MessDailyMenuQueryCriteria criteria) {
        return ResultDTO.success(sysMessDailyMenuService.getDailyMenu(criteria.getMenuDate()));
    }

    @ApiOperation(value = "获取所有的菜谱")
    @GetMapping(value = "all")
    public ResultDTO<List<MessDailyMenuItemListDTO>> listData(
            @ApiParam(name = "menuDate")
            @RequestParam(value = "menuDate") LocalDate menuDate,
            @ApiParam(name = "mealType", allowableValues = "1,2,4")
            @RequestParam(value = "mealType", required = false) Integer mealType) {
        return ResultDTO.success(sysMessDailyMenuService.listProductForKitchen(menuDate, mealType));
    }

    @ApiOperation(value = "查询菜品")
    @GetMapping(value = "listProducts")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_EDIT')")
    public ResultDTO<?> listForMenu(@RequestParam("date") String date, @RequestParam("catalog") Integer catalog) {
        return ResultDTO.success(sysMessDailyMenuService.listProductForMenu(date, catalog));
    }

    /**
     * 新增mess_daily_menu
     *
     * @author xin-bing
     * @date 2019-10-22 16:10:53
     */
    @ApiOperation(value = "新增菜谱", notes = "新增菜谱")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_EDIT')")
    public ResultDTO<?> create(@Valid @RequestBody MessDailyMenuAddReq messDailyMenuAddReq) {
        return ResultDTO.success(sysMessDailyMenuService.addMenuItem(messDailyMenuAddReq));
    }

    /**
     * 新增mess_daily_menu
     *
     * @author xin-bing
     * @date 2019-10-22 16:10:53
     */
    @ApiOperation(value = "菜谱排序", notes = "菜谱排序")
    @PostMapping(value = "sort")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_EDIT')")
    public ResultDTO sortMenuItem(@RequestParam("itemIds") List<Long> itemIds) {
        sysMessDailyMenuService.sortMenuItem(itemIds);
        return ResultDTO.success();
    }

    /**
     * 新增mess_daily_menu
     *
     * @author xin-bing
     * @date 2019-10-22 16:10:53
     */
    @ApiOperation(value = "删除排菜", notes = "删除排菜")
    @DeleteMapping(value = "/{itemId}")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_EDIT')")
    public ResultDTO<?> create(@PathVariable("itemId") Long itemId) {
        sysMessDailyMenuService.delMenuItem(itemId);
        return ResultDTO.success();
    }

    /**
     * 添加厨师
     *
     * @return
     */
    @ApiOperation(value = "添加/编辑厨师", notes = "添加/编辑厨师")
    @PostMapping(value = "edit_cook")
    @PreAuthorize("hasAnyRole('ADMIN', 'MENU_ALL', 'MENU_EDIT')")
    public ResultDTO<?> editCook(@Valid @RequestBody MessDailyMenuCookAddReq req) {
        this.sysMessDailyMenuService.addCook(req);
        return ResultDTO.success();
    }

    /**
     * 复制菜谱
     */
    @ApiOperation(value = "复制菜谱", notes = "复制菜谱")
    @PostMapping(value = "copy_menu")
    @PreAuthorize("hasAnyRole('ADMIN', 'MENU_ALL', 'MENU_EDIT')")
    public ResultDTO<?> copyMenu(@Valid @RequestBody MessDailyMenuCopyReq req) {
        this.sysMessDailyMenuService.copyMenu(req);
        return ResultDTO.success();
    }

    @GetMapping(value = "feign/findProductProcurement")
    public FeignProcurementDto findProductProcurement(@RequestParam(value = "date") LocalDate date) {
        //切换数据源
        String dataSource = feignTenantOrganizationService.findMenuDataSource();
        if (StringUtils.isBlank(dataSource)) {
            throw new BizException("", "未知餐厅数据源");
        }
        DynamicDataSourceContextHolder.push(dataSource);
        return this.sysMessDailyMenuService.findProductProcurement(date);
    }

    /**
     * 溯源
     *
     * @author loki
     * @date 2021/7/29 15:44
     **/
    @GetMapping(value = "source-chain/{id}")
    public ResultDTO sourceChain(@PathVariable(value = "id") Long menuId) {
        return ResultDTO.success(sysMessDailyMenuService.getMenuSourceChain(menuId));
    }
}