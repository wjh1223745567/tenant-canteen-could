package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.WxMessageSendRecord;
import com.iotinall.canteen.entity.WxMessageSendRecordDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 微信消息发送记录
 *
 * @author loki
 * @date 2021/04/16 10:22
 */
public interface WxMessageSendRecordDetailRepository extends JpaRepository<WxMessageSendRecordDetail, Long>, JpaSpecificationExecutor<WxMessageSendRecordDetail> {

    WxMessageSendRecordDetail findByRecordAndOpenId(WxMessageSendRecord record, String openId);
}
