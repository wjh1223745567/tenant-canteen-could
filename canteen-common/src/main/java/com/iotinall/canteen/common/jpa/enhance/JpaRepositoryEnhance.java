package com.iotinall.canteen.common.jpa.enhance;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Map;

/**
 * @author xin-bing
 * @date 10/23/2019 10:57
 */
@NoRepositoryBean
public interface JpaRepositoryEnhance<T, ID> extends JpaRepository<T, ID> {

    /**
     * 将一个对象置为游离态
     * @param entity
     */
    void detach(T entity);
    /**
     * 批量插入
     * @param iterable
     */
    void insertAll(Iterable<T> iterable);

    /**
     * jpql/hql分页查询
     * @param jpql 要执行的jpql
     * @param params 参数
     * @param pageable 分页参数
     * @return
     */
    Page<T> pageQuery(String jpql, Map<String, Object> params, Pageable pageable, Sort... sort);

    /**
     * jpql/hql 分页查询
     * @param jpql 要执行的jpql
     * @param countJpql 查询数量的jpql
     * @param params 参数，该参数会设置给jpql和countJpql，所以要求jpql和countJpql拥有相同的参数列表，如果二者参数列表不同，那么可自己先查询出数量，然后调用另一个pageQuery
     * @param pageable 分页参数
     * @return
     */
    Page<T> pageQuery(String jpql, String countJpql, Map<String, Object> params, Pageable pageable, Sort... sort);

    /**
     * jpql/hql 分页查询
     * @param jpql 执行查询的jpql
     * @param count 数量
     * @param params 参数
     * @param pageable 分页参数
     * @return
     */
    Page<T> pageQuery(String jpql, long count, Map<String, Object> params, Pageable pageable, Sort... sort);

    List<T> list(String jpql, Map<String, Object> params, Sort... sorts);

    /**
     * 根据sql查询
     * @param sql
     * @param params
     * @return
     */
    List<Map<String, Object>> listBySql(String sql, Map<String, Object> params, Sort... sorts);

    /**
     *
     * @param sql 查询的sql
     * @param params 参数
     * @return
     */
    <V> List<V> listBySql(String sql, Map<String, Object> params, Class<V> result, Sort... sorts);
}
