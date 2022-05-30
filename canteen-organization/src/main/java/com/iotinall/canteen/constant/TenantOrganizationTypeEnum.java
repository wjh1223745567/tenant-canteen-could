package com.iotinall.canteen.constant;

import com.iotinall.canteen.common.exception.BizException;
import lombok.Getter;

import java.util.Objects;

@Getter
public enum TenantOrganizationTypeEnum {

    FOOD_DEPARTMENT(0, "膳食部"),
    DINING_HALL(1, "食堂"),
    BACK_KITCHEN(2, "后厨"),
    INVENTORY(3, "库存"),
    OTHER(4, "其他");

    TenantOrganizationTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    final String name;

    final Integer code;

    public static TenantOrganizationTypeEnum findByCode(Integer code){
        for (TenantOrganizationTypeEnum value : TenantOrganizationTypeEnum.values()) {
            if(Objects.equals(code, value.getCode())){
                return value;
            }
        }
        throw new BizException("", "未找到租户组织类型");
    }
}
