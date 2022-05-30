package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @description: 美食笔记关注表
 * @author: JoeLau
 * @time: 2021年07月06日 11:15:26
 */

@Data
@Entity
@Table(name = "food_note_follow_record")
@EqualsAndHashCode(callSuper = true)
public class FoodNoteFollowRecord extends BaseEntity {

    /**
     * 作者ID
     */
    private Long authorId;

    /**
     * 用户ID
     */
    private Long userId;

}
