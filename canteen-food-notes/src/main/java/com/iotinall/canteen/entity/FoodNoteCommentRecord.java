package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * @description: 笔记评论记录
 * @author: JoeLau
 * @time: 2021年07月06日 10:30:39
 */

@Data
@Entity
@Table(name = "food_note_comment_record")
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = {"foodNoteCommentLikeRecordList", "foodNote"})
public class FoodNoteCommentRecord extends BaseEntity {
    /**
     * 评论内容
     */
    private String comment;

    /**
     * 评论人ID
     */
    private Long userId;

    /**
     * 评论人姓名
     */
    private String userName;

    /**
     * 评论人头像
     */
    private String userAvatar;

    /**
     * 上条评论ID
     */
    private Long lastId;

//    /**
//     * 上条评论人ID
//     */
//    private Long lastUserId;

    /**
     * 评论点赞记录
     */
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "foodNoteCommentRecord")
    private List<FoodNoteCommentLikeRecord> foodNoteCommentLikeRecordList;

    /**
     * 美食笔记
     */
    @ManyToOne
    @JoinColumn(name = "food_note_id")
    private FoodNote foodNote;
}
