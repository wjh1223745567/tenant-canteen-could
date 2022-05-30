package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.OrgEmployeeCard;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 卡
 *
 * @author loki
 * @date 2020/04/27 16:02
 */
public interface OrgEmployeeCardRepository extends JpaRepositoryEnhance<OrgEmployeeCard, Long>, JpaSpecificationExecutor<OrgEmployeeCard> {
    /**
     * 根据卡号获取卡片
     */
    OrgEmployeeCard findByCardNo(String cardNo);
}
