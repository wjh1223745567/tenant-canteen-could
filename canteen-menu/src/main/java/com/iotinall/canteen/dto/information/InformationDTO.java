package com.iotinall.canteen.dto.information;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author WJH
 * @date 2019/11/19:49
 */
@Setter
@Getter
@Accessors(chain = true)
public class InformationDTO {

    private Long id;

    private String title;

    private Type type;

    private String content;

    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String cover;

    private Integer praiseCount;

    private Integer status;
    /**
     * 是否置顶
     */
    private Boolean sticky;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Boolean edit;

    private Integer seq;

    @Setter
    @Getter
    @Accessors(chain = true)
    public class Type {

        private Long id;

        private String name;

    }
}
