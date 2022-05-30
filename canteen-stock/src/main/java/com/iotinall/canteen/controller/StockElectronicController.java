package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constant.SwaggerModule;
import com.iotinall.canteen.dto.electronic.ElectronicAcceptReq;
import com.iotinall.canteen.service.StockElectronicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 电子秤接口
 *
 * @author loki
 * @date 2020/08/25 19:20
 */
@Slf4j
@Api(tags = SwaggerModule.MODULE_ELECTRONIC_SCALE)
@RestController
@RequestMapping(value = "stock/electronic")
public class StockElectronicController {
    @Resource
    private StockElectronicService stockElectronicService;

    /**
     * 获取入库 退货 出库 退库 未完成的单据列表
     *
     * @author loki
     * @date 2021/04/29 10:22
     */
    @ApiOperation(value = "电子秤验收获取单据列表，只返回未完成的单据", tags = "电子秤验收获取单据列表，只返回未完成的单据", httpMethod = "GET")
    @GetMapping("/{billType}")
    public ResultDTO billList(@PathVariable("billType") String billType) {
        return ResultDTO.success(stockElectronicService.billList(billType));
    }

    /**
     * 电子秤验收
     *
     * @author loki
     * @date 2021/04/29 10:22
     */
    @ApiOperation(value = "电子秤验收", tags = "电子秤验收", httpMethod = "POST")
    @PostMapping("accept")
    public ResultDTO accept(@RequestBody @Valid ElectronicAcceptReq req) {
        stockElectronicService.accept(req);
        return ResultDTO.success();
    }

    /**
     * 电子秤验收上传图片
     *
     * @author loki
     * @date 2021/04/29 10:22
     */
    @ApiOperation(value = "电子秤验收上传图片", tags = "电子秤验收上传图片", httpMethod = "POST")
    @PostMapping("img/{billNo}/{goodsId}")
    public ResultDTO upload(@RequestParam("file") MultipartFile file,
                            @PathVariable("billNo") String billNo,
                            @PathVariable("goodsId") Long goodsId) {
        stockElectronicService.upload(file, billNo, goodsId);
        return ResultDTO.success();
    }
}
