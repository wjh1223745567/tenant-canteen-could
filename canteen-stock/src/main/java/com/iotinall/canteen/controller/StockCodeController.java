package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constant.SwaggerModule;
import com.iotinall.canteen.utils.CodeGeneratorUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 采购入库，采购退货，领用出库，领用退库
 *
 * @author loki
 * @date 2020/08/25 19:20
 */
@Api(tags = SwaggerModule.MODULE_STOCK)
@RestController
@RequestMapping("stock/code")
public class StockCodeController {
    @ApiOperation(value = "生成单据号(0-采购入库,1 - 领用出库,2-采购退货,3 - 领用退库 ，4-库存盘点 5-商品编号", httpMethod = "GET")
    @GetMapping("/{platform}/{type}")
    public ResultDTO create(@PathVariable("platform") Integer platform, @PathVariable Integer type) {
        return ResultDTO.success(CodeGeneratorUtil.buildCode(platform, type));
    }
}
