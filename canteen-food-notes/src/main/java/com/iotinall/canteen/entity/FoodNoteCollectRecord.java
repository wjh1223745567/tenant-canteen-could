package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @description: 美食笔记收藏记录
 * @author: JoeLau
 * @time: 2021年07月06日 11:11:07
 */

@Data
@Entity
@Table(name = "food_note_collect_record")
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = {"foodNote"})
public class FoodNoteCollectRecord extends BaseEntity {

    /**
     * 收藏人ID
     */
    private Long userId;

    /**
     * 美食笔记
     */
    @ManyToOne
    @JoinColumn(name = "food_note_id")
    private FoodNote foodNote;
}
