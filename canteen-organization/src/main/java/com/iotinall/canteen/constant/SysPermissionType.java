package com.iotinall.canteen.constant;

import lombok.Getter;

/**
 * 权限类型
 */
@Getter
public enum SysPermissionType {

    CLIENT(0, "小程序"),
    SERVER(1, "PC后台");

    SysPermissionType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public SysPermissionType getByCode(Integer code){
        if(code == null){
            return null;
        }
        return values()[code];
    }

    private final Integer code;

    private final String name;

}
