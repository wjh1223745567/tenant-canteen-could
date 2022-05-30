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
 * @description: 笔记点赞记录
 * @author: JoeLau
 * @time: 2021年07月06日 10:15:43
 */

@Data
@Entity
@Table(name = "food_note_like_record")
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = {"foodNote"})
public class FoodNoteLikeRecord extends BaseEntity {

    /**
     * 点赞人ID
     */
    private Long userId;

    /**
     * 美食笔记
     */
    @ManyToOne
    @JoinColumn(name = "food_note_id")
    private FoodNote foodNote;
}
