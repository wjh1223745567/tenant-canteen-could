package com.iotinall.canteen.controller;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.dto.finconsumesetting.FinConsumeSettingDTO;
import com.iotinall.canteen.dto.finconsumesetting.FinConsumeSettingReq;
import com.iotinall.canteen.service.SysFinConsumeSettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * fin_consume_setting Controller
 *
 * @author xin-bing
 * @date 2019-10-23 17:57:09
 */
@Api(tags = SwaggerModule.MODULE_FIN)
@RestController
@RequestMapping("fin/consume-settings")
public class FinConsumeSettingController {

    @Resource
    private SysFinConsumeSettingService finConsumeSettingService;

    /**
     * 查询fin_consume_setting列表
     *
     * @author xin-bing
     * @date 2019-10-23 17:57:09
     */
    @ApiOperation(value = "查询消费设置", notes = "根据条件查询fin_consume_setting列表")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','CANTEEN_ALL','CANTEEN_SETTING')")
    public ResultDTO<List<FinConsumeSettingDTO>> list() {
        return ResultDTO.success(finConsumeSettingService.listAll());
    }


    /**
     * 修改fin_consume_setting
     *
     * @author xin-bing
     * @date 2019-10-23 17:57:09
     */
    @ApiOperation(value = "修改消费设置", notes = "修改fin_consume_setting")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','CANTEEN_ALL','CANTEEN_SETTING')")
    public ResultDTO update(@RequestBody List<FinConsumeSettingReq> list) throws BindException {
        finConsumeSettingService.update(list);
        return ResultDTO.success();
    }

    /**
     * 获取当前餐厅时间配置信息
     *
     * @param dataSource
     * @return
     */
    @GetMapping(value = "feign/consume_setting")
    public String mealTime(@RequestParam(value = "dataSource") String dataSource) {
        DynamicDataSourceContextHolder.push(dataSource);
        return finConsumeSettingService.mealTime();
    }
}