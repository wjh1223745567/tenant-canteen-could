package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 光盘行动违规识别记录
 *
 * @author loki
 * @date 10/10/2019 20:46
 */
@Data
@Entity
@Table(name = "empty_plate_analysis_record")
@EqualsAndHashCode(callSuper = false)
public class EmptyPlateAnalysisRecord extends BaseEntity {

    private Long targetId;

    /**
     * 冗余对象名称，target对象不存在，显示targetName
     */
    private String targetName;

    /**
     * 比分
     */
    private Double score;


    /**
     * 违规记录
     */
    @OneToOne
    @JoinColumn(name = "img_record_id")
    private EmptyPlateImgRecord imgRecord;
}
