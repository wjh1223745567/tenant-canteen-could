package com.iotinall.canteen.common.protocol;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库信息
 */
@Data
@Accessors(chain = true)
public class DataSourceInfoDTO {
    /**
     * 后厨
     */
    private String kitchen;

    private List<SimpDataSource> allKitchen = new ArrayList<>();

    /**
     * 库存
     */
    private String stock;

    private List<SimpDataSource> allStock = new ArrayList<>();

    /**
     * 餐厅
     */
    private String menu;

    private List<SimpDataSource> allMenu = new ArrayList<>();
}
