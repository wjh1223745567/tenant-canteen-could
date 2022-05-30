package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.dto.mess.MessCookListDTO;
import com.iotinall.canteen.entity.MessProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 菜品 Repository
 *
 * @author xin-bing
 * @date 2019-10-21 16:07:57
 */
public interface MessProductRepository extends JpaRepositoryEnhance<MessProduct, Long>, JpaSpecificationExecutor<MessProduct> {

    /**
     * 批量删除
     *
     * @param ids
     * @return int
     */
    @Modifying
    @Query(value = "delete from MessProduct where id in :ids")
    int deleteByIdIn(@Param(value = "ids") Long[] ids);

    MessProduct findFirstByName(String name);

    @Modifying
    @Query(value = "update MessProduct p set p.enabled = :enabled, p.updateTime = :now where p.id = :id")
    void toggleProduct(@Param(value = "id") Long id, @Param(value = "enabled") Boolean enabled, @Param(value = "now") LocalDateTime now);

    @Query("from MessProduct p where p.catalog like :catalog and p.useFor like :useFor and p.enabled = true")
    List<MessProduct> listForMenu(@Param(value = "catalog") String catalog, @Param(value = "useFor") String useFor);

    /**
     * 统计分类下面菜谱数
     *
     * @author loki
     * @date 2020/04/22 10:58
     */
    @Query(value = "select count(mc.product_id) from mess_product_cuisine mc where mc.cuisine_id = :cuisineId", nativeQuery = true)
    Integer countCuisineMessProduct(@Param("cuisineId") String cuisineId);

    /**
     * 统计分类下面菜谱数
     *
     * @author loki
     * @date 2020/04/22 10:58
     */
    @Query(value = "select mc.product_id from mess_product_cuisine mc where mc.cuisine_id in ( :cuisineIds )", nativeQuery = true)
    Set<Long> queryCuisineMessProduct(@Param("cuisineIds") Set<String> cuisineIds);

    @Override
    Page<MessProduct> findAll(Specification<MessProduct> spec, Pageable pageable);

    @Query(value = "select * from mess_product where enabled = 1",nativeQuery = true)
    List<MessProduct> selectOnShelf();

    @Modifying
    @Query("update MessProduct o set o.recommendedCount = o.recommendedCount+:count where o.id= :id")
    void addRecommendedCount(@Param("id") Long id, @Param("count")int count);

    @Query("from MessProduct where catalog like :catalog and useFor like :useFor")
    List<MessProduct> findByCatalogLikeAndUseForLike(@Param("catalog")String catalog, @Param("useFor")String useFor);

    @Query(value = "select p.id as id, p.name as name, p.img as img " +
            "from MessProduct p where p.id > :cursor and p.useFor like :useFor")
    Page<MessCookListDTO> pageByUseForLike(@Param("useFor") String useFor, @Param("cursor") Long cursor, Pageable pq);

    @Query(value = "select p.id as id, p.name as name, p.img as img " +
            "from MessProduct p where p.id > :cursor and p.name like :name and p.useFor like :useFor")
    Page<MessCookListDTO> pageByNameLikeAndUseForLike(@Param("name") String name, @Param("useFor") String useFor, @Param("cursor") Long cursor, Pageable pq);

    List<MessProduct> queryByIdIn(@Param("ids") Set<Long> ids);
}
