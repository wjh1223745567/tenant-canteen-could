package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * @description: 美食笔记实体
 * @author: JoeLau
 * @time: 2021年07月06日 09:47:58
 */

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "food_note")
@ToString(exclude = {"foodNoteType", "foodNoteLikeRecordList", "foodNoteCommentRecordList", "foodNoteCollectRecordList"})
public class FoodNote extends BaseEntity {

    /**
     * 标题
     */
    private String title;

    /**
     * 作者ID
     */
    private Long authorId;

    /**
     * 作者姓名
     */
    private String authorName;

    /**
     * 作者头像
     */
    private String authorAvatar;

    /**
     * 笔记类型
     */
    @ManyToOne
    @JoinColumn(name = "food_note_type_id")
    private FoodNoteType foodNoteType;

    /**
     * 笔记点赞记录
     */
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "foodNote")
    private List<FoodNoteLikeRecord> foodNoteLikeRecordList;

    /**
     * 笔记评论记录
     */
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "foodNote")
    private List<FoodNoteCommentRecord> foodNoteCommentRecordList;

    /**
     * 笔记收藏记录
     */
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "foodNote")
    private List<FoodNoteCollectRecord> foodNoteCollectRecordList;

    /**
     * 浏览阅读量
     */
    private Integer FoodNoteReadingNum;

    /**
     * 多张图片，最多九张，用逗号分隔
     */
    @Column(columnDefinition = "text")
    private String pictures;

    /**
     * 笔记内容
     */
    @Column(columnDefinition = "text")
    private String content;

    /**
     * 状态 false-草稿箱 true-已发布
     */
    private Boolean status;

}
