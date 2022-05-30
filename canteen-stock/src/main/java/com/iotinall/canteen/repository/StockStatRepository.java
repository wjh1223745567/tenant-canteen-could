package com.iotinall.canteen.repository;

import com.iotinall.canteen.dto.stock.StockInOutMoneyStatDTO;
import com.iotinall.canteen.entity.StockStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * 库存统计汇总
 *
 * @author loki
 * @date 2020/09/14 20:54
 */
public interface StockStatRepository extends JpaRepository<StockStat, Long>, JpaSpecificationExecutor<StockStat> {

    /**
     * 获取库存统计
     *
     * @author loki
     * @date 2020/09/14 20:57
     */
    @Query(value = "select s.* from stock_stat s " +
            " where DATE_FORMAT(s.date,'%Y%m') = DATE_FORMAT(:date,'%Y%m') " +
            " and s.goods_id = :goodsId", nativeQuery = true)
    List<StockStat> findGoodsStockStat(@Param("date") LocalDate date, @Param("goodsId") Long goodsId);

    /**
     * 获取库存统计
     *
     * @author loki
     * @date 2020/09/14 20:57
     */
    @Query(value = "select s.* from stock_stat s " +
            " where DATE_FORMAT(s.date,'%Y%m') = :date " +
            " and s.goods_id = :goodsId " +
            " and s.type =:type", nativeQuery = true)
    List<StockStat> findGoodsStockStat(@Param("date") LocalDate date, @Param("goodsId") Long goodsId, @Param("type") Integer type);

    /**
     * 根据单据类型统计入库/出库/退库/退货金额
     *
     * @author loki
     * @date 2021/7/13 10:23
     **/
    @Query(value = "select ROUND(sum(detail.real_amount * detail.price),0)" +
            " from stock_bill_detail detail" +
            " left outer join stock_bill bill on(detail.bill_id=bill.id)" +
            " where bill.bill_type=:billType and bill.finish_date=:date", nativeQuery = true)
    BigDecimal statStockAmount(@Param("billType") String billType, @Param("date") LocalDate date);

    /**
     * @author loki
     * @date 2021/7/13 11:06
     **/
    @Query(value = "SELECT\n" +
            " ROUND( sum( detail.amount * detail.price ), 0 ) AS money,\n" +
            " ANY_VALUE(bill.bill_type) AS billType,\n" +
            " bill.bill_date date \n" +
            " FROM stock_bill_detail detail " +
            " LEFT OUTER JOIN stock_bill bill ON(detail.bill_id=bill.id)" +
            " WHERE bill.finish_date IS NOT NULL " +
            " AND bill.bill_date >= :begin \n" +
            " AND bill.bill_date <= :end \n" +
            " GROUP BY date", nativeQuery = true)
    List<StockInOutMoneyStatDTO> statStockMoney(@Param("begin") LocalDate begin, @Param("end") LocalDate end);

    @Query(value = "select  ROUND(sum(detail.amount*detail.price),0) as amount\n" +
            " from stock_bill_detail detail\n" +
            " left outer join stock_bill bill on(bill.id = detail.bill_id)" +
            " left outer join stock_goods goods on(detail.goods_id = goods.id)\n" +
            " where bill.bill_type = :billType \n" +
            " and bill.bill_date>=:begin and bill.bill_date<=:end \n" +
            " and goods.type_id in(:typeIds)", nativeQuery = true)
    BigDecimal statStockAmountByType(@Param("begin") LocalDate begin,
                                     @Param("end") LocalDate end,
                                     @Param("typeIds") Set<Long> typeIds,
                                     @Param("billType") String billType);
}
