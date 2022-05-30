package com.iotinall.canteen.repository;

import com.iotinall.canteen.constants.TakeoutStatus;
import com.iotinall.canteen.dto.tackout.ITakeoutProductDTO;
import com.iotinall.canteen.entity.MessTakeoutOrderDetail;
import com.iotinall.canteen.entity.MessTakeoutProductStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessTakeoutOrderDetailRepository extends JpaRepository<MessTakeoutOrderDetail, Long>, JpaSpecificationExecutor<MessTakeoutOrderDetail> {


    @Query("select count(1) from MessTakeoutOrderDetail d, MessTakeoutOrder to where d.messTakeoutOrder = to and d.messProduct = :product and to.sourcingStatus in (:state)")
    Long findOrderCount(@Param("product") MessTakeoutProductStock product, @Param("state") List<TakeoutStatus> statusList);

    @Query(value = "select \n" +
            " detail.product_id as productId,\n" +
            " sum(detail.buy_amount) count,\n" +
            " any_value(pi.name) as name,\n" +
            " any_value(pi.img) as img\n" +
            " from mess_takeout_order_detail detail\n" +
            " left outer join mess_takeout_product_stock ps on(ps.id = detail.id)\n" +
            " left outer join mess_takeout_product_info pi on(ps.mess_product_id = pi.id)\n" +
            " group by detail.product_id\n" +
            " having name is not null\n" +
            " order by count desc\n" +
            " limit 8 ", nativeQuery = true)
    List<ITakeoutProductDTO> findTop8();
}
