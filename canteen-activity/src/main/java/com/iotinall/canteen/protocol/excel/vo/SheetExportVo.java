package com.iotinall.canteen.protocol.excel.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author loki
 * @date 2019-10-18 20:30
 **/
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class SheetExportVo implements Serializable {
    /**
     * sheet显示名
     */
    private String sheetName;

    /**
     * sheet对应的类
     */
    private Class<?> sheetClass;

    /**
     * sheet对应的数据
     */
    private Object data;

    public SheetExportVo(String sheetName, Class<?> sheetClass, Object data) {
        this.sheetClass = sheetClass;
        this.sheetName = sheetName;
        this.data = data;
    }
}
