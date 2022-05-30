package com.iotinall.canteen.dto.disease;

import lombok.Data;

import java.io.Serializable;

/**
 * 慢性疾病
 *
 * @author loki
 * @date 2020/12/09 19:41
 */
@Data
public class ChronicDiseaseDTO implements Serializable {
    private Long id;
    private String name;
    /**
     * 是否适用男性
     */
    private Boolean male;

    /**
     * 是否适用女性
     */
    private Boolean female;
}
