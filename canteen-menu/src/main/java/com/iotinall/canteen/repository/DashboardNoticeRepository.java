package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.DashBoardNotice;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 看板消息
 *
 * @author loki
 * @date 2020/04/27 16:02
 */
public interface DashboardNoticeRepository extends JpaRepositoryEnhance<DashBoardNotice, Long>, JpaSpecificationExecutor<DashBoardNotice> {
}
