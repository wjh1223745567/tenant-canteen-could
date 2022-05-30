package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.dto.dashboard.ConsumeStatisItem;
import com.iotinall.canteen.entity.FinTransactionRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * fin_transaction_record Repository
 *
 * @author xin-bing
 * @date 2019-10-30 10:20:51
 */
public interface FinTransactionRecordRepository extends JpaRepositoryEnhance<FinTransactionRecord, Long>, JpaSpecificationExecutor<FinTransactionRecord> {

    @Query(" select " +
            " sum(ftr.amount) as amount, " +
            " count(ftr.id) as count, " +
            " ftr.eatType as eatType " +
            " from FinTransactionRecord ftr " +
            " where ftr.transactionTime >= :start " +
            " and ftr.transactionTime <= :end " +
            " and ftr.state = 1 " +
            " group by ftr.eatType")
    List<ConsumeStatisItem> statisConsumeInfo(@Param(value = "start") LocalDateTime start, @Param(value = "end") LocalDateTime end);


    @Query("select count(ftr.id) as count from FinTransactionRecord ftr " +
            "where ftr.transactionTime >= :start and ftr.transactionTime <= :end and ftr.state = 1 ")
    Long statisCountInfo(@Param(value = "start") LocalDateTime start, @Param(value = "end") LocalDateTime end);

    @Query(value = "select sum(record.amount)  from fin_transaction_record record where record.state = 1", nativeQuery = true)
    BigDecimal statTransactionAmount();

    FinTransactionRecord queryByConsumeRecordId(Long syncRecordId);

    @Query(value = "SELECT\n" +
            " DATE_FORMAT( ftr.transaction_time, '%Y-%m-%d' ) date,\n" +
            " sum( ftr.amount ) AS amount,\n" +
            " count( ftr.id ) AS count,\n" +
            " ftr.eat_type AS eatType \n" +
            " FROM fin_transaction_record ftr  WHERE\n" +
            " DATE_FORMAT( ftr.transaction_time, '%Y-%m-%d' ) >= :begin \n" +
            " AND DATE_FORMAT( ftr.transaction_time, '%Y-%m-%d' ) <= :end \n" +
            " AND ftr.state = 1  GROUP BY date,ftr.eat_type", nativeQuery = true)
    List<ConsumeStatisItem> statConsume(@Param(value = "begin") LocalDate begin, @Param(value = "end") LocalDate end);

    @Query(value = "select r.* from fin_transaction_record r where DATE_FORMAT( r.transaction_time, '%Y-%m-%d' ) = :date" +
            " and r.eat_type = :eatType" +
            " and r.emp_id = :employeeId order by transaction_time desc limit 1", nativeQuery = true)
    FinTransactionRecord findTransactionRecord(@Param("date") LocalDate date, @Param("eatType") Integer eatType, @Param("employeeId") Long employeeId);


    FinTransactionRecord findByTackOutOrderId(Long Id);
}