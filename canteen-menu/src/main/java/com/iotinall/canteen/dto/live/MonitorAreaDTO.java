package com.iotinall.canteen.dto.live;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MonitorAreaDTO {
    private Long id;
    private String name;
    private String areaName;
    private String streamPath;
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String img;
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String defaultImg;
    private LocalDateTime detectTime;
}
