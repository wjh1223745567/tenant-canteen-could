package com.iotinall.canteen.repository;

import com.iotinall.canteen.dto.mess.CommentsDTO;
import com.iotinall.canteen.dto.mess.IMessProductCommentDTO;
import com.iotinall.canteen.entity.MessProductComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author WJH
 * @date 2019/11/516:50
 */
public interface MessProductCommentRepository extends JpaRepository<MessProductComment, Long>, JpaSpecificationExecutor<MessProductComment> {

    long countAllByProductId(Long productId);

    long countAllByProductIdAndScoreGreaterThanEqual(Long productId, BigDecimal score);

    List<MessProductComment> findAllByProductId(Long productId);

    @Query(value = "select avg(distinct t.score) from mess_product_comment t where t.product_id = ?1", nativeQuery = true)
    BigDecimal avgStarByProduct(Long productId);

    /**
     * 赞
     *
     * @param commid
     * @param empId
     */
    @Modifying
    @Query("update MessProductComment o set o.favorCount = o.favorCount+1,o.favorEmpId = concat(coalesce(o.favorEmpId,''), ',', :empId) where o.id=:commId ")
    void addFavor(@Param("commId") Long commid, @Param("empId") Long empId);

    /**
     * 踩
     *
     * @param commid
     * @param empId
     */
    @Modifying
    @Query("update MessProductComment o set o.oppositeCount = o.oppositeCount+1, o.oppositeEmpId = concat(coalesce(o.oppositeEmpId, ''), ',', :empId) where o.id = :commId ")
    void addOpposite(@Param("commId") Long commid, @Param("empId") Long empId);

    @Query(value = "SELECT\n" +
            "mc.product_id," +
            "sum( CASE WHEN mc.score = 5 THEN 1 ELSE 0 END ) as fiveStar,\n" +
            "sum( CASE WHEN mc.score = 4 THEN 1 ELSE 0 END ) as fourStar,\n" +
            "sum( CASE WHEN mc.score = 3 THEN 1 ELSE 0 END ) as threeStar,\n" +
            "sum( CASE WHEN mc.score = 2 THEN 1 ELSE 0 END ) as twoStar,\n" +
            "sum( CASE WHEN mc.score = 1 THEN 1 ELSE 0 END ) as oneStar\n" +
            "FROM mess_product_comment mc " +
            "WHERE mc.product_id = :id and mc.create_time >= :begin and mc.create_time <= :end", nativeQuery = true)
    CommentsDTO findByProductIdAndTime(Long id, LocalDateTime begin, LocalDateTime end);

    @Query("select p.score as score, count(p.score) as count from MessProductComment p where p.productId = :productId group by p.score order by p.score desc")
    List<Map<String, Number>> statScoreCounts(@Param(value = "productId") Long productId);

    @Query("select tags from MessProductComment where productId = :productId")
    List<String> findAllCommentTag(@Param(value = "productId") Long productId);

    /**
     * 数据统计，每日点评统计
     *
     * @author loki
     * @date 2020/05/05 14:16
     */
    @Query(value = "SELECT\n" +
            "mp.id as id,mp.name as name,mp.img as img,mp.catalog as catalog, \n" +
            "sum( CASE WHEN mc.score = 5 THEN 1 ELSE 0 END ) as fiveStar,\n" +
            "sum( CASE WHEN mc.score = 4 THEN 1 ELSE 0 END ) as fourStar,\n" +
            "sum( CASE WHEN mc.score = 3 THEN 1 ELSE 0 END ) as threeStar,\n" +
            "sum( CASE WHEN mc.score = 2 THEN 1 ELSE 0 END ) as twoStar,\n" +
            "sum( CASE WHEN mc.score = 1 THEN 1 ELSE 0 END ) as oneStar\n" +
            "FROM mess_product_comment mc JOIN mess_product mp ON ( mc.product_id = mp.id ) \n" +
            "WHERE DATE_FORMAT(mc.create_time, '%Y-%m-%d')= :date  GROUP BY mp.id", nativeQuery = true, countProjection = "1")
    Page<CommentsDTO> queryMessProductCommentsPage(@Param("date") String date, Pageable page);


    @Query(value = "SELECT AVG(c.score) from mess_product_comment c where product_id =:productId", nativeQuery = true)
    Long messProductAvgStar(@Param("productId") Long productId);


    @Query(value = "SELECT\n" +
            " record.product_id productId,\n" +
            " sum( IF ( record.score > 3, 1, 0 )) count,\n" +
            " mp.`name`,\n" +
            " mp.img \n" +
            " FROM\n" +
            " `mess_product_comment` record\n" +
            " LEFT OUTER JOIN mess_product mp ON ( record.product_id = mp.id ) \n" +
            " GROUP BY  record.product_id \n" +
            " ORDER BY count DESC \n" +
            " LIMIT 8", nativeQuery = true)
    List<IMessProductCommentDTO> findTop5Good();

    @Query(value = "SELECT\n" +
            " record.product_id productId,\n" +
            " sum( IF ( record.score <= 3, 1, 0 )) count,\n" +
            " mp.`name`,\n" +
            " mp.img \n" +
            " FROM\n" +
            " `mess_product_comment` record\n" +
            " LEFT OUTER JOIN mess_product mp ON ( record.product_id = mp.id ) \n" +
            " GROUP BY  record.product_id \n" +
            " ORDER BY count DESC \n" +
            " LIMIT 8", nativeQuery = true)
    List<IMessProductCommentDTO> findTop5Bad();
}
