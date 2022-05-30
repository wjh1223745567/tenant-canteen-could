package com.iotinall.canteen.dto.messdailymenu;

import com.iotinall.canteen.dto.stock.MasterMaterialDTO;
import lombok.Data;

import java.util.List;

/**
 * 菜谱溯源
 *
 * @author loki
 * @date 2021/02/26 15:15
 */
@Data
public class MenuChain {
    private String productName;
    private String cookName;
    private List<MasterMaterialDTO> materialList;
}
