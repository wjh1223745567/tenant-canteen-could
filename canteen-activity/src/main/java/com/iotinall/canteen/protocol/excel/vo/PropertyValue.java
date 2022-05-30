package com.iotinall.canteen.protocol.excel.vo;

import lombok.Data;

@Data
public class PropertyValue {

    private String value;

    private String label;

    private String dataType;

    private Boolean isNull;

    private String propertyId;

    //applicationType = 0:填写租户id
    //applicationType = 1:填写分类id
    //applicationType = 2:填写设备id
    private String applicationId;

    private Integer applicationType;
}
