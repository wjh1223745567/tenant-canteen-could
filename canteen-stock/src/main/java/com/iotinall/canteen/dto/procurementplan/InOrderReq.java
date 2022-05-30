package com.iotinall.canteen.dto.procurementplan;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class InOrderReq {

    /**
     * 供应商ID
     */
    @NotNull
    private Long supplierId;

    @NotEmpty
    private List<InOrderProdReq> prod;

}
