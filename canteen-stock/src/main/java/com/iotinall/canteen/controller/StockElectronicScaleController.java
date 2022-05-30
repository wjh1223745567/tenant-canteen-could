package com.iotinall.canteen.controller;

import com.iotinall.canteen.constant.SwaggerModule;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 电子秤控制器
 *
 * @author loki
 * @date 2021/06/02 20:18
 */
@Api(tags = SwaggerModule.MODULE_ELECTRONIC_SCALE)
@RestController
@RequestMapping("stock/in-back")
public class StockElectronicScaleController {
}
