package com.iotinall.canteen.constants;

import lombok.Getter;

/**
 * @author WJH
 * @date 2019/11/2817:35
 */
@Getter
public enum AreaType {

    TAKE(0,"取餐区"), EAT(1,"就餐区"), KITCHEN(2, "后厨区");

    private final String name;

    private final Integer code;

    AreaType(Integer code, String name) {
        this.name = name;
        this.code = code;
    }

    /**
     * 根据code 获取餐位信息
     * @param code
     * @return
     */
    public static AreaType  findByCode(Integer code){
        return AreaType.values()[code];
    }
}
