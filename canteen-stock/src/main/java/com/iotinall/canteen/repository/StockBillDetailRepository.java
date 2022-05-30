package com.iotinall.canteen.repository;

import com.iotinall.canteen.dto.outbill.OrgStockOutDTO;
import com.iotinall.canteen.entity.StockBill;
import com.iotinall.canteen.entity.StockBillDetail;
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
 * 单据明细持久化类
 *
 * @author loki
 * @date 2021/06/03 11:58
 */
public interface StockBillDetailRepository extends JpaRepository<StockBillDetail, Long>, JpaSpecificationExecutor<StockBillDetail> {
    /**
     * 根据商品查询单据明细
     */
    Long countByGoods(StockGoods goods);

    /**
     * 获取单据明细
     */
    StockBillDetail findByBillAndGoods(StockBill bill, StockGoods goods);

    /**
     * 退库申请统计商品已经退库的总数量
     */
    @Query(value = "select sum(detail.real_amount)" +
            " from stock_bill_detail detail " +
            " where detail.bill_detail_id=:outDetailId and detail.acceptance = true"
            , nativeQuery = true)
    BigDecimal statGoodsOutBackTotal(Long outDetailId);

    /**
     * 库存汇总，获取商品明细
     */
    @Query(value = " select * from stock_bill_detail detail" +
            " left outer join stock_bill bill on(bill.id = detail.bill_id)" +
            " where bill.bill_type = :billType" +
            " and bill.bill_date = :date" +
            " and detail.goods_id =:goodsId" +
            " and bill.status = :status "
            , nativeQuery = true)
    List<StockBillDetail> findDetailList(@Param("billType") String billType,
                                         @Param("date") LocalDate date,
                                         @Param("goodsId") Long goodsId,
                                         @Param("status") Integer status);

    /**
     * 库存汇总，获取单据退库或者退货单据明细
     */
    List<StockBillDetail> findByBillDetail(StockBillDetail inBillDetail);

    /**
     * 超级查询 出/入/退库/退货明细
     */
    @Query(value = "select detail.* from stock_bill_detail detail " +
            " left outer join stock_bill bill on(detail.bill_id = bill.id)" +
            " left outer join stock_goods goods on(detail.goods_id = goods.id)" +
            " where 1=1 " +
            " and bill.status = 3" +
            " and if(:beginDate!='null',bill.bill_date >= :beginDate,1=1)" +
            " and if(:endDate!='null',bill.bill_date <= :endDate,1=1)" +
            " and if(:billNo!=''!='null',bill.bill_no = :billNo,1=1)" +
            " and if(:supplierId!='null',bill.supplier_id = :supplierId,1=1)" +
            " and if(:goodsId!='null',detail.goods_id = :goodsId,1=1)" +
            " and if(CONCAT('',:goodsTypeIds) is not null,goods.type_id in (:goodsTypeIds),1=1)" +
            " and if(CONCAT('',:warehouseIds) is not null,goods.warehouse_id in (:warehouseIds),1=1)" +
            " and if(CONCAT('',:billTypeList) is not null,bill.bill_type in (:billTypeList),1=1)" +
            " and if(CONCAT('',:orgIds) is not null,bill.apply_user_org_id in (:orgIds),1=1)" +
            " and if(:keyword!='',goods.name like CONCAT('%',:keyword,'%') or bill.bill_no like CONCAT('%',:keyword,'%'),1=1)", nativeQuery = true, countProjection = "1")
    Page<StockBillDetail> search(
            @Param("beginDate") LocalDate beginDate,
            @Param("endDate") LocalDate endDate,
            @Param("goodsTypeIds") Set<Long> goodsTypeIds,
            @Param("billNo") String billNo,
            @Param("supplierId") Long supplierId,
            @Param("warehouseIds") Set<Long> warehouseIds,
            @Param("billTypeList") List<String> billTypeList,
            @Param("orgIds") Set<Long> orgIds,
            @Param("goodsId") Long goodsId,
            @Param("keyword") String keyword,
            Pageable page);

    /**
     * 入库和出库汇总
     */
    @Query(value = "select detail.goods_id as goodsId," +
            "goods.name as goodsName," +
            "goods.unit as unit," +
            "detail.price as price," +
            "any_value(bill.supplier_id) as supplierId," +
            "any_value(supplier.name) as supplierName," +
            "sum(detail.real_amount) as quantity " +
            " from stock_bill_detail detail " +
            " join stock_goods goods on(detail.goods_id = goods.id) " +
            " join stock_bill bill on(bill.id = detail.bill_id) " +
            " left outer join stock_supplier supplier on(bill.supplier_id = supplier.id) " +
            " where 1=1 and bill.status = 3" +
            " and if(CONCAT('',:billTypeList) is not null,bill.bill_type in (:billTypeList),1=1)" +
            " and if(:begin!='null',DATE_FORMAT(bill.bill_date,'%Y-%m-%d') >= :begin,1=1)" +
            " and if(:end!='null',DATE_FORMAT(bill.bill_date,'%Y-%m-%d')<= :end,1=1)" +
            " and if(CONCAT('',:orgIdList) is not null,bill.apply_user_org_id in (:orgIdList) ,1=1)" +
            " and if(:supplierId!='' and :supplierId!='null',bill.supplier_id= :supplierId ,1=1)" +
            " and if(:keywords!='' ,goods.name like concat('%',:keywords,'%') ,1=1)" +
            " GROUP BY detail.goods_id,detail.price", nativeQuery = true, countProjection = "detail.id")
    Page<OrgStockOutDTO> statStockInOutDetail(@Param("orgIdList") Set<Long> orgIdList,
                                              @Param("billTypeList") Set<String> billTypeList,
                                              @Param("supplierId") Long supplierId,
                                              @Param("keywords") String keywords,
                                              @Param("begin") LocalDate begin,
                                              @Param("end") LocalDate end,
                                              Pageable page);
}
