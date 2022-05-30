package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import com.iotinall.canteen.constant.Constants;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 流程配置
 *
 * @author loki
 * @date 2020/04/30 18:17
 */
@Data
@Entity
@Table(name = "stock_flw_config")
@EqualsAndHashCode(callSuper = false)
public class StockFlwConfig extends BaseEntity {
    /**
     * 流程配置类型，详情请参考
     *
     * @see Constants.BILL_TYPE
     */
    private String type;

    /**
     * 版本号
     * 流程配置始终维最新的版本号
     * 流程任务对应多个版本号
     */
    private Long version;
}
