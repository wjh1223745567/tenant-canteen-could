package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.dto.messprod.MessProductDetailDTO;
import com.iotinall.canteen.dto.messprod.ProductPicSearchCon;
import com.iotinall.canteen.service.AppMessProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;

/**
 * 外带外购商品
 *
 * @author WJH
 * @date 2019/11/2315:08
 */
@Api(tags = SwaggerModule.MODULE_PRODUCT)
@RestController
@RequestMapping(value = "mess-product")
public class AppMessProductController {
    @Resource
    private AppMessProductService messProductService;

    @ApiOperation(value = "获取所有的菜谱")
    @GetMapping(value = "page")
    public ResultDTO page(@RequestParam("keyword") String keyword, @RequestParam(value = "catalog", required = false) Integer catalog, Pageable pageable) {
        return ResultDTO.success(messProductService.page(keyword, catalog, pageable));
    }

    /**
     * 获取烹饪教程
     *
     * @return
     */
    @GetMapping(value = "cook-practices")
    public ResultDTO<?> pageCookPractice(@RequestParam(value = "name") String name, @RequestParam(value = "cursor") Long cursor) {
        return ResultDTO.success(messProductService.pageCooks(name, cursor));
    }

    /**
     * 烹饪详情
     *
     * @param id
     * @return
     */
    @GetMapping(value = "cook-details")
    public ResultDTO<?> cookDetails(@RequestParam(value = "id") Long id) {
        return ResultDTO.success(messProductService.getCookDetails(id));
    }

    /**
     * 菜品详情
     *
     * @author loki
     * @date 2020/08/18 16:24
     */
    @GetMapping(value = "/details")
    public ResultDTO<MessProductDetailDTO> detail(@ApiParam(name = "id", value = "id", required = true) @RequestParam(value = "id") Long id) {
        return ResultDTO.success(messProductService.detail(id));
    }

    /**
     * 获取当前时间属于什么时间
     *
     * @return
     */
    @GetMapping(value = "get_meal_type")
    public ResultDTO<?> getMealType() {
        return ResultDTO.success(this.messProductService.getMealType());
    }

    @PostMapping(value = "search_by_pic")
    public ResultDTO<?> searchByPic(@Valid @RequestBody ProductPicSearchCon searchCon) {
        return ResultDTO.success(messProductService.searchByPic(searchCon));
    }

    /**
     * 根据图片搜索
     *
     * @return
     */
    @PostMapping(value = "search_menu")
    public ResultDTO searChMenu(@RequestBody Map<String, String> data) {
        return ResultDTO.success(this.messProductService.searchMenu(data.get("base64")));
    }
}
