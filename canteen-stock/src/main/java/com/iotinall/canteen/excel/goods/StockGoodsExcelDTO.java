package com.iotinall.canteen.excel.goods;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class StockGoodsExcelDTO {
    /**
     * 类别
     */
    @ExcelProperty(index = 0)
    private String goodsType;
    /**
     * 编码
     */
    @ExcelProperty(index = 1)
    private String code;
    /**
     * 品名
     */
    @ExcelProperty(index = 2)
    private String name;
    /**
     * 别名
     */
    @ExcelProperty(index = 3)
    private String nickname;
    /**
     * 规格
     */
    @ExcelProperty(index = 4)
    private String specs;
    /**
     * 单位
     */
    @ExcelProperty(index = 5)
    private String unit;
    /**
     * 中心定价
     */
    @ExcelProperty(index = 6)
    private String price;

}
