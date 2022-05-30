package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.item.ItemAddReq;
import com.iotinall.canteen.dto.item.ItemDTO;
import com.iotinall.canteen.dto.item.ItemEditReq;
import com.iotinall.canteen.service.KitchenItemService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xinbing
 * @date 2020-07-02
 */
@RestController
@RequestMapping("kitchen/item")
public class KitchenItemController {
    @Resource
    private KitchenItemService kitchenItemService;

    @GetMapping
    @ApiOperation(value = "考核标准、制度类型、晨检项、消毒项、留样标准列表")
    public ResultDTO<List<ItemDTO>> list(
            @ApiParam(value = "组编码", allowableValues = "assess_level,rule_type,morning_inspect,disinfect_item,sample_item,wash_type,chop_type,fire_protect" +
                    ",facility_item,kitchen_garbage,env_item,food_additives")
            @RequestParam(value = "groupCode") String groupCode) {
        return ResultDTO.success(kitchenItemService.list(groupCode));
    }

    @PostMapping
    @ApiOperation(value = "添加项目", response = ItemAddReq.class)
    public ResultDTO<?> add(@Validated @RequestBody ItemAddReq req) {
        return ResultDTO.success(kitchenItemService.add(req));
    }

    @PutMapping
    @ApiOperation(value = "修改项目")
    public ResultDTO<?> edit(@Validated @RequestBody ItemEditReq req) {
        kitchenItemService.edit(req);
        return ResultDTO.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除项目")
    public ResultDTO del(@ApiParam(value = "多个id以,分割") @RequestParam(value = "batch") Long[] batch) {
        kitchenItemService.del(batch);
        return ResultDTO.success();
    }
}
