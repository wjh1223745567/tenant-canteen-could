package com.iotinall.canteen.common.constant;

import com.iotinall.canteen.common.exception.BizException;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 有冗余，须搜索修改两次
 */

@Getter
public enum TenantOrganizationTypeEnum {

    FOOD_DEPARTMENT(0, "膳食部", ""),
    DINING_HALL(1, "食堂", "canteen-menu"),
    BACK_KITCHEN(2, "后厨", "canteen-kitchen"),
    INVENTORY(3, "库存", "canteen-stock"),
    OTHER(4, "其他", "");

    TenantOrganizationTypeEnum(Integer code, String name, String sysName) {
        this.name = name;
        this.sysName = sysName;
        this.code = code;
    }

    final String name;

    final String sysName;

    final Integer code;

    public static TenantOrganizationTypeEnum findByCode(Integer code){
        for (TenantOrganizationTypeEnum value : TenantOrganizationTypeEnum.values()) {
            if(Objects.equals(code, value.getCode())){
                return value;
            }
        }
        throw new BizException("", "未找到租户组织类型");
    }

    public static TenantOrganizationTypeEnum findBySysName(String sysName){
        for (TenantOrganizationTypeEnum value : TenantOrganizationTypeEnum.values()) {
            if(StringUtils.isNotBlank(sysName) && StringUtils.isNotBlank(value.getSysName()) && Objects.equals(sysName, value.getSysName())){
                return value;
            }
        }
        return null;
    }
}
