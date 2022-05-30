package com.iotinall.canteen.dto.procurementplan;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 溯源链
 */
@Data
@Accessors(chain = true)
public class ApplyChainDto {

    private String createName;

    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String createAvatar;

    /**
     * 操作日期
     */
    private LocalDateTime operationDate;

    /**
     * 操作
     */
    private String operation;

    private List<ApplyChainProdDto> prodDtoList;

    /**
     * 供应商
     */
    private String supplier;
}
