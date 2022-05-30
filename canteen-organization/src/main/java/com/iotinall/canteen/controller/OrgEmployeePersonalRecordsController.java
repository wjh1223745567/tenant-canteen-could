package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.personalrecords.OrgEmployeePersonalRecordsEmpReq;
import com.iotinall.canteen.dto.personalrecords.OrgEmployeePersonalRecordsResp;
import com.iotinall.canteen.service.OrgEmployeePersonalRecordsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 个人档案
 */
@RestController
@RequestMapping(value = "org_employee_personal_records")
public class OrgEmployeePersonalRecordsController {

    @Resource
    private OrgEmployeePersonalRecordsService recordsService;

    /**
     * 查询个人档案
     * @param id
     * @return
     */
    @GetMapping(value = "find_by_employee_id")
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_EMP_EDIT')")
    public ResultDTO<List<OrgEmployeePersonalRecordsResp>> findByEmployeeId(@RequestParam Long id){
        return ResultDTO.success(this.recordsService.findByEmployeeId(id));
    }

    /**
     * 更新个人档案
     * @param req
     * @return
     */
    @PostMapping(value = "update")
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_EMP_EDIT')")
    public ResultDTO update(@Valid @RequestBody OrgEmployeePersonalRecordsEmpReq req){
        this.recordsService.update(req);
        return ResultDTO.success();
    }

}
