package com.iotinall.canteen.dto.messcookdetail;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MessCookDetailDTO {
    private Long id;
    private String name;
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String img;
    private String nutrition; // 营养
    private List<MessCookMaterialDTO> materials; // 原料列表
    private List<MessCookStepDTO> steps;
    private String practiceTips; // 制作提示


    @Data
    public static class MessCookMaterialDTO { // 原材料
        private String id;
        private String name;
        private BigDecimal amount;
        private Integer master;
    }

    @Data
    public static class MessCookStepDTO { // 制作方法
        private String description;
        private Integer fileType;
        @JsonSerialize(using = ImgPair.ImgSerializer.class)
        @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
        private String filePath;
    }
}
