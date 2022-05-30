package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.dto.information.InformationTypeReq;
import com.iotinall.canteen.service.SysInformationTypeService;
import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 咨询类型
 *
 * @author WJH
 * @date 2019/11/110:27
 */
@Api(tags = SwaggerModule.MODULE_INFO)
@RestController
@RequestMapping(value = "information_type")
public class InformationTypeController {

    @Resource
    private SysInformationTypeService informationTypeService;

    @GetMapping(value = "findAll")
    @ResponseBody
    public ResultDTO findAll() {
        return ResultDTO.success(this.informationTypeService.findAll());
    }

    @PostMapping(value = "save")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ADMIN','INTERACTIVE_MANAGEMENT','INFORMATION_TYPE_EDIT')")
    public ResultDTO save(@Valid @RequestBody InformationTypeReq req) {
        return ResultDTO.success(this.informationTypeService.save(req));
    }

    @GetMapping(value = "deleted")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ADMIN','INTERACTIVE_MANAGEMENT','INFORMATION_TYPE_EDIT')")
    public ResultDTO deleted(Long id) {
        return ResultDTO.success(this.informationTypeService.deleted(id));
    }
}
