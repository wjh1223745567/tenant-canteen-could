package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constant.SwaggerModule;
import com.iotinall.canteen.dto.bill.WalletBillDetailDTO;
import com.iotinall.canteen.service.WalletBillDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * 钱包账单处理类
 *
 * @author loki
 * @date 2020/05/03 20:23
 */
@Api(tags = SwaggerModule.MODULE_WALLET)
@RestController
@RequestMapping("wallet/bill")
public class WalletBillDetailController {
    @Resource
    private WalletBillDetailService walletBillDetailService;

    @ApiOperation(value = "获取账单分页列表", response = WalletBillDetailDTO.class)
    @GetMapping
    public ResultDTO getGoodsList(@RequestParam("billDate") LocalDate billDate, @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable page) {
        return ResultDTO.success(this.walletBillDetailService.page(billDate, page));
    }
}
