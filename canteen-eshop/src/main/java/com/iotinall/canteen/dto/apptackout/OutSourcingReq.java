package com.iotinall.canteen.dto.apptackout;

import lombok.Data;

import java.util.List;

/**
 * @author WJH
 * @date 2019/11/2214:27
 */
@Data
public class OutSourcingReq {

    /**
     * 购买人
     */
    private Long empId;

    private List<OutSourcingProductReq> productReqs;

}
