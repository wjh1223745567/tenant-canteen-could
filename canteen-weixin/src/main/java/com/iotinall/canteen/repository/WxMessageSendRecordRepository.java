package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.WxMessageSendRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

/**
 * 微信消息发送记录明细
 *
 * @author loki
 * @date 2021/04/16 10:22
 */
public interface WxMessageSendRecordRepository extends JpaRepository<WxMessageSendRecord, Long>, JpaSpecificationExecutor<WxMessageSendRecord> {

    @Query(value = "select r.* from wx_message_send_record r " +
            " left outer join wx_message_content_config config on(r.config_id = config.id)" +
            " where config.type = :type " +
            " and date_format(r.create_time,'%Y-%m-%d')=:date limit 1", nativeQuery = true)
    WxMessageSendRecord findSendRecord(@Param("type") Integer type, @Param("date") LocalDate date);
}
