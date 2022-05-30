package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.MsgSendRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 消息发送客户端
 *
 * @author loki
 * @date 2021/04/14 14:37
 */
public interface MsgSendRepository extends JpaRepositoryEnhance<MsgSendRecord, Long>, JpaSpecificationExecutor<MsgSendRecord> {

}
