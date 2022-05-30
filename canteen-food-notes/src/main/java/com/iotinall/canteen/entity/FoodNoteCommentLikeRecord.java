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
 * @description: 评论点赞记录
 * @author: JoeLau
 * @time: 2021年07月06日 10:59:18
 */

@Data
@Entity
@Table(name = "food_note_comment_like_record")
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = {"foodNoteCommentRecord"})
public class FoodNoteCommentLikeRecord extends BaseEntity {

    /**
     * 点赞用户ID
     */
    private Long userId;

    /**
     * 评论
     */
    @ManyToOne
    @JoinColumn(name = "food_note_comment_record_id")
    private FoodNoteCommentRecord foodNoteCommentRecord;

}
