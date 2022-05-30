package com.iotinall.canteen.dto.orgemployee;

import lombok.Data;

import java.util.List;

@Data
public class OrgEmpTreeDTO {
    private Long id;
    private String name;
    private Long parentId;
    private Boolean isEmp; //
    private List<OrgEmpTreeDTO> children;
}
