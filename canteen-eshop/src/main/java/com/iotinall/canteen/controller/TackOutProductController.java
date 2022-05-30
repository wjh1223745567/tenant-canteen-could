package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.dto.tackout.TackOutProductAddReq;
import com.iotinall.canteen.dto.tackout.TackOutProductCondition;
import com.iotinall.canteen.dto.tackout.TackOutProductEditReq;
import com.iotinall.canteen.service.SysTackOutProductService;
import io.swagger.annotations.Api;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 发布外带外购信息
 *
 * @author WJH
 * @date 2019/11/2617:46
 */
@Api(tags = SwaggerModule.MODULE_PRODUCT)
@RestController
@RequestMapping(value = "takeout/products")
public class TackOutProductController {

    @Resource
    private SysTackOutProductService tackOutProductService;

    /**
     * 查询外带外购信息
     *
     * @return
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TAKE_OUT_ALL','TAKEOUT_PRODUCT_LIST')")
    public ResultDTO list(TackOutProductCondition condition, Pageable pageable) {
        return ResultDTO.success(this.tackOutProductService.findByPage(condition, pageable));
    }

    @GetMapping(value = "toggle")
    @PreAuthorize("hasAnyRole('ADMIN', 'TAKE_OUT_ALL', 'TAKEOUT_PRODUCT_EDIT')")
    public ResultDTO toggleProduct(@RequestParam Long id, @RequestParam Boolean state) {
        this.tackOutProductService.toggleProduct(id, state);
        return ResultDTO.success();
    }

    /**
     * 清空所有已售出数量
     * @return
     */
    @GetMapping(value = "clear_all_sell")
    @PreAuthorize("hasAnyRole('ADMIN', 'TAKE_OUT_ALL', 'TAKEOUT_PRODUCT_EDIT')")
    public ResultDTO clearAllSell(){
        this.tackOutProductService.clearAllSell();
        return ResultDTO.success();
    }

    /**
     * 发布外带外购商品
     *
     * @param reqs
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TAKE_OUT_ALL', 'TAKEOUT_PRODUCT_EDIT')")
    public ResultDTO addProduct(@Valid @RequestBody TackOutProductAddReq reqs) {
        this.tackOutProductService.addProducts(reqs);
        return ResultDTO.success();
    }


    /**
     * 发布外带外购商品
     *
     * @param reqs
     * @return
     */
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','TAKE_OUT_ALL', 'TAKEOUT_PRODUCT_EDIT')")
    public ResultDTO editProduct(@Valid @RequestBody TackOutProductEditReq reqs) {
        this.tackOutProductService.editProducts(reqs);
        return ResultDTO.success();
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','TAKE_OUT_ALL', 'TAKEOUT_PRODUCT_EDIT')")
    public ResultDTO remove(@RequestParam(name = "ids") List<Long> ids) {
        this.tackOutProductService.remove(ids);
        return ResultDTO.success();
    }

}
