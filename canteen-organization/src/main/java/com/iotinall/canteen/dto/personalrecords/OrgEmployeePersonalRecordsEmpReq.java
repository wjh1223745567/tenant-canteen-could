package com.iotinall.canteen.dto.personalrecords;

import lombok.Data;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
public class OrgEmployeePersonalRecordsEmpReq {

    @NotNull
    private Long employeeId;

    @NotEmpty(message = "档案不能为空")
    List<OrgEmployeePersonalRecordsReq> recordsReqs;

}
