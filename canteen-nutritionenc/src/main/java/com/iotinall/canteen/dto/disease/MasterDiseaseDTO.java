package com.iotinall.canteen.dto.disease;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 慢性疾病
 *
 * @author loki
 * @date 2020/12/09 19:41
 */
@Data
public class MasterDiseaseDTO implements Serializable {
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer seq;

    /**
     * 是否适用男性
     */
    private Boolean male;

    /**
     * 是否适用女性
     */
    private Boolean female;

    /**
     * 对应食谱
     */
    private List<ChronicDiseaseDTO> chronicDisease;
}
