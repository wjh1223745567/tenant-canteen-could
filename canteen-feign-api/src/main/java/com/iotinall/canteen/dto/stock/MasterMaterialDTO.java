package com.iotinall.canteen.dto.stock;

import lombok.Data;

import java.io.Serializable;

/**
 * 菜谱溯源原材料
 *
 * @author loki
 * @date 2021/02/26 15:17
 */
@Data
public class MasterMaterialDTO implements Serializable {
    private Long materialId;
    private String materialName;
    private String outApplyer;
    private String outAccepter;
    private String inApplyer;
    private String inAccepter;
    private String supplierName;
}
