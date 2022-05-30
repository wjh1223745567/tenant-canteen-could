package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.WalletBillDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 钱包余额变动记录
 *
 * @author loki
 * @date 2020/04/27 16:02
 */
public interface WalletBillDetailRepository extends JpaRepositoryEnhance<WalletBillDetail, Long>, JpaSpecificationExecutor<WalletBillDetail> {

    WalletBillDetail queryByRecordIdAndRecordTypeAndSource(Long recordId,Integer recordType,Integer source);
}
