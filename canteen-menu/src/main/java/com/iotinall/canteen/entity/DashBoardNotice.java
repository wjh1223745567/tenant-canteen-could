package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 看板通知
 *
 * @author bingo
 * @date 1/3/2020 16:18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "dashboard_notice")
public class DashBoardNotice extends BaseEntity {

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    @Column(columnDefinition = "text")
    private String content;

    private String img;
}
