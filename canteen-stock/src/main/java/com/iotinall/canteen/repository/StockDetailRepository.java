package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.StockBillDetail;
import com.iotinall.canteen.entity.StockDetail;
import com.iotinall.canteen.entity.StockGoods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * 库存明细持久化类
 *
 * @author loki
 * @date 2021/06/03 11:58
 */
public interface StockDetailRepository extends JpaRepository<StockDetail, Long>, JpaSpecificationExecutor<StockDetail> {
    /**
     * 根据入库明细获取库存明细
     */
    StockDetail findByBillDetail(StockBillDetail billDetail);

    /**
     * 获取商品库存数大于0的入库明细
     */
    @Query(value = "select * from stock_detail sd" +
            " left outer join stock_bill_detail sbd on(sd.bill_detail_id = sbd.id)" +
            " left outer join stock_bill sb on(sbd.bill_id = sb.id)" +
            " where sd.goods_id =:goodsId and sd.amount > 0" +
            " order by sb.bill_date asc", nativeQuery = true)
    List<StockDetail> findGoodsLeftStock(@Param("goodsId") Long goodsId);

    /**
     * 超级查询 获取所有商品库存明细
     */
    @Query(value = "select detail.* from stock_detail detail\n" +
            " left outer join stock_bill_detail billDetail on(billDetail.id = detail.bill_detail_id)\n" +
            " left outer join stock_goods goods on(goods.id = detail.goods_id)\n" +
            " left outer join stock_bill bill on(billDetail.bill_id=bill.id)\n" +
            " where 1=1 and bill.status =:status" +
            " and if(:beginDate!='null',bill.bill_date >= :beginDate,1=1)" +
            " and if(:endDate!='null',bill.bill_date <= :endDate,1=1)" +
            " and if(:billNo!=''!='null',bill.bill_no = :billNo,1=1)" +
            " and if(:supplierId!='null',bill.supplier_id = :supplierId,1=1)" +
            " and if(:goodsId!='null',detail.goods_id = :goodsId,1=1)" +
            " and if(CONCAT('',:goodsTypeIds) is not null,goods.type_id in (:goodsTypeIds),1=1)" +
            " and if(:keyword!='',goods.name like CONCAT('%',:keyword,'%'),1=1)" +
            " and if(CONCAT('',:warehouseIds) is not null,goods.warehouse_id in (:warehouseIds),1=1)" +
            " order by detail.goods_id,bill.finish_date desc", nativeQuery = true, countProjection = "1")
    Page<StockDetail> search(@Param("beginDate") LocalDate beginDate,
                             @Param("endDate") LocalDate endDate,
                             @Param("billNo") String billNo,
                             @Param("status") Integer status,
                             @Param("goodsId") Long goodsId,
                             @Param("warehouseIds") Set<Long> warehouseIds,
                             @Param("goodsTypeIds") Set<Long> goodsTypeIds,
                             @Param("supplierId") Long supplierId,
                             @Param("keyword") String keyword,
                             Pageable page);

    /**
     * 获取最近入库的库存明细
     *
     * @author loki
     * @date 2020/09/10 17:46
     */
    @Query(value = "select detail.* from stock_detail detail" +
            " where detail.goods_id = :goodsId order by detail.create_time desc limit 1", nativeQuery = true)
    StockDetail findLatestStockInDetail(@Param("goodsId") Long goodsId);

    /**
     * 获取商品库存数>0的库存明细
     *
     * @author loki
     * @date 2020/09/10 17:46
     */
    List<StockDetail> findByGoodsAndAmountGreaterThanOrderByCreateTimeAsc(StockGoods goods, BigDecimal amount);

    /**
     * 获取即将过期的库存明细
     *
     * @author loki
     * @date 2020/09/11 16:28
     */
    @Query(value = "select count(detail.id) " +
            " from stock_detail detail " +
            " left outer join stock_bill_detail billDetail on(billDetail.id=detail.bill_detail_id)" +
            " where detail.goods_id = :goodsId " +
            " and billDetail.max_shelf_life <= now() " +
            " and detail.amount > 0", nativeQuery = true)
    Integer countExpiredStock(@Param("goodsId") Long goodsId);
}
