package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constant.SwaggerModule;
import com.iotinall.canteen.service.StockTodoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 待办控制器
 *
 * @author loki
 * @date 2021/06/02 20:18
 */
@Api(tags = SwaggerModule.MODULE_TODO_LIST)
@RestController
@RequestMapping("stock/todo-list")
public class StockTodoController {
    @Resource
    private StockTodoService stockTodoListService;

    @ApiOperation(value = "待办列表", httpMethod = "GET")
    @GetMapping
    public ResultDTO page(@RequestParam(required = false) String billType,
                          @RequestParam(required = false) String optType,
                          @RequestParam(required = false) String keyword,
                          @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable page) {
        return ResultDTO.success(stockTodoListService.page(billType, optType, keyword, page));
    }

    @ApiOperation(value = "更新待办为已办", httpMethod = "PUT")
    @PostMapping("/{todoId}")
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_TODO')")
    public ResultDTO update(@PathVariable("todoId") Long todoId) {
        stockTodoListService.update(todoId);
        return ResultDTO.success();
    }
}
