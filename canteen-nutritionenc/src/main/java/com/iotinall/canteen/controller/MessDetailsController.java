package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.sysdictdetail.MessTagAddReq;
import com.iotinall.canteen.dto.sysdictdetail.SysDictDetailDTO;
import com.iotinall.canteen.entity.SysDictDetail;
import com.iotinall.canteen.service.SysDictDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Api(tags = "食堂标签字典表")
@RestController
@RequestMapping(value = "mess_details")
public class MessDetailsController {

    @Resource
    private SysDictDetailService sysDictDetailService;

    public final static String ENV_TAG = "env_tag";

    public final static String TASTE_TAG = "taste_tag";

    public final static String RECHARGE_TAG = "recharge_tag";

    @ApiOperation(value = "获取餐厅标签", notes = "获取餐厅标签")
    @GetMapping("/tags")
    public ResultDTO getMessDict() {
        List<SysDictDetailDTO> env = sysDictDetailService.listAll(ENV_TAG);
        List<SysDictDetailDTO> taste = sysDictDetailService.listAll(TASTE_TAG);
        List<SysDictDetailDTO> recharge = sysDictDetailService.listAll(RECHARGE_TAG);
        Map<String, Object> map = new HashMap<>(3);
        map.put(ENV_TAG, env);
        map.put(TASTE_TAG, taste);
        map.put(RECHARGE_TAG, recharge);
        return ResultDTO.success(map);
    }

    @ApiOperation(value = "获取餐厅标签", notes = "获取餐厅标签")
    @GetMapping("/tags/{type}")
    public ResultDTO getMessDictByType(@PathVariable("type") String type) {
        return ResultDTO.success(sysDictDetailService.listAll(type));
    }

    @ApiOperation(value = "编辑标签", notes = "编辑标签")
    @PostMapping(value = "tags")
    @PreAuthorize("hasAnyRole('ADMIN','CANTEEN_ALL', 'CANTEEN_SETTING')")
    public ResultDTO addTasteTag(@Valid @RequestBody MessTagAddReq messTagAddReq) {
        switch (messTagAddReq.getTagType()) {
            case ENV_TAG:
            case TASTE_TAG:
                SysDictDetail add = sysDictDetailService.add(messTagAddReq.getLabel(), messTagAddReq.getTagType());
                return ResultDTO.success(add);
            default:
                return ResultDTO.failed("", "不支持的标签类型");
        }
    }

    @ApiOperation(value = "删除标签", notes = "删除标签")
    @DeleteMapping(value = "tags")
    @PreAuthorize("hasAnyRole('ADMIN','CANTEEN_ALL', 'CANTEEN_SETTING')")
    public ResultDTO deleteTag(@RequestParam(value = "id") Long id) {
        sysDictDetailService.delete(id);
        return ResultDTO.success();
    }

}
