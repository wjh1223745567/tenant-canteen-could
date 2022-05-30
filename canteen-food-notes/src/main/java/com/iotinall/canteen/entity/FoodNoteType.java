package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * @description: 美食笔记类型实体
 * @author: JoeLau
 * @time: 2021年07月05日 18:00:38
 */

@Data
@Entity
@Table(name = "food_note_type")
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = {"foodNoteList"})
public class FoodNoteType extends BaseEntity {

    /**
     * 类型名
     */
    private String name;

    /**
     * 状态 true-启用 false-禁用
     */
    private Boolean status;

//    /**
//     * 笔记数
//     */
//    private Integer noteNumber;

    /**
     * 美食笔记
     */
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "foodNoteType")
    private List<FoodNote> foodNoteList;


}
