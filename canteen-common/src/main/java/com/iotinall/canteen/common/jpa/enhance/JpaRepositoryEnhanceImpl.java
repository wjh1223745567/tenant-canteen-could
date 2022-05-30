package com.iotinall.canteen.common.jpa.enhance;

import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * jpa repository增强
 * @author xin-bing
 * @date 10/23/2019 10:27
 */
public class JpaRepositoryEnhanceImpl<T, ID> extends SimpleJpaRepository<T, ID> implements JpaRepositoryEnhance<T,ID> {
    private final JpaEntityInformation<T, ?> entityInformation;
    private final EntityManager em;
    private final PersistenceProvider provider;

    public JpaRepositoryEnhanceImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.em = entityManager;
        this.provider = PersistenceProvider.fromEntityManager(entityManager);
    }


    public JpaRepositoryEnhanceImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        this.entityInformation = JpaEntityInformationSupport.getEntityInformation(domainClass, em);
        this.em  = em;
        this.provider = PersistenceProvider.fromEntityManager(em);
    }

    /**
     * 将一个对象转换为游离态
     * 用于给实体设置了属性，并不想entityManager自动save/merge，希望自己手动写sql去控制
     * @param entity
     */
    @Override
    public void detach(T entity) {
        this.em.detach(entity);
    }

    @Override
    public void insertAll(Iterable<T> iterable) {
    }

    @Override
    public Page<T> pageQuery(String jpql, Map<String, Object> params, Pageable pageable, Sort... sort) {
        return pageQuery(jpql, getCountSql(jpql), params, pageable, sort);
    }

    @Override
    public Page<T> pageQuery(String jpql, String countJpql, Map<String, Object> params, Pageable pageable, Sort... sort) {
        long count = countByJPQL(countJpql, params);
        return pageQuery(jpql, count, params, pageable, sort);
    }

    @Override
    public Page<T> pageQuery(String jpql, long count, Map<String, Object> params, Pageable pageable, Sort... sort) {
        int offset = pageable.getPageNumber() * pageable.getPageSize();
        if(count < offset) { // 不查询
            return new PageImpl<>(Collections.emptyList(), pageable, count);
        }
        jpql = buildSortSql(jpql, pageable, sort);
        TypedQuery<T> query = this.em.createQuery(jpql, this.getDomainClass());
        setParameters(query, params);
        List<T> resultList = query.setFirstResult(offset).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(resultList, pageable, count);
    }

    @Override
    public List<T> list(String jpql, Map<String, Object> params, Sort... sorts) {
        if(sorts.length > 0) {
            jpql = buildSortSql(jpql, sorts);
        }
        TypedQuery<T> query = this.em.createQuery(jpql, this.getDomainClass());
        setParameters(query, params);
        return query.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> listBySql(String sql, Map<String, Object> params, Sort... sorts) {
        if(sorts.length > 0) {
            sql = buildSortSql(sql);
        }
        NativeQuery<?> unwrap = em.createNativeQuery(sql).unwrap(NativeQuery.class);
        setParameters(unwrap, params);
        return (List<Map<String, Object>>) unwrap.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> List<V> listBySql(String sql, Map<String, Object> params, Class<V> result, Sort... sorts) {
        if(sorts.length > 0) {
            sql = buildSortSql(sql);
        }
        NativeQuery<?> unwrap = em.createNativeQuery(sql).unwrap(NativeQuery.class);
        setParameters(unwrap, params);
        return (List<V>) unwrap.setResultTransformer(Transformers.aliasToBean(result))
                .getResultList();
    }

    private long countByJPQL(String jpql, Map<String, Object> params) {
        TypedQuery<Long> query = this.em.createQuery(jpql, Long.class);
        setParameters(query, params);
        return query.getSingleResult();
    }

    private void setParameters(Query query, Map<String, Object> params) {
        if(!CollectionUtils.isEmpty(params)) {
            for(Map.Entry<String, Object> entry : params.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 拼接sort条件，jpql, sql都可以使用
     * @param baseSql 基础sql
     * @param sorts 查询条件
     * @return String
     */
    private String buildSortSql(String baseSql, Sort ...sorts) {
        if(sorts.length == 0) {
            return baseSql;
        }
        StringBuilder sb = new StringBuilder(baseSql).append(" order by ");
        for(int i=0; i<sorts.length; i++) {
            sorts[i].get().forEach(item-> sb.append(item.getProperty()).append(' ').append(item.getDirection().name()).append(','));
        }
        return sb.deleteCharAt(sb.length()-1).toString();
    }

    private String buildSortSql(String baseSql, Pageable pageable, Sort ...sorts) {
        if(pageable.getSort().isSorted() || sorts.length > 0) {
            StringBuilder sb = new StringBuilder(baseSql).append(" order by ");
            if(pageable.getSort().isSorted()) {
                pageable.getSort().get().forEach(item ->
                        sb.append(item.getProperty()).append(' ').append(item.getDirection().name()).append(",")
                );
            }
            for(int i=0; i<sorts.length; i++) {
                sorts[i].get().forEach(item-> sb.append(item.getProperty()).append(' ').append(item.getDirection().name()).append(','));
            }
            return sb.deleteCharAt(sb.length() - 1).toString();
        }
        return baseSql;
    }

    private Pattern JOIN_FETCH_PATTERN = Pattern.compile("join\\s+fetch");
    private ConcurrentHashMap<String, String> COUNT_CACHE_MAP = new ConcurrentHashMap<>();
    private String getCountSql(String jpql) {
        String countHql = COUNT_CACHE_MAP.get(jpql);
        if(countHql == null) {
            int index = jpql.toLowerCase().indexOf("from");
            if(index != 0) {
                countHql = "select count(*) " + jpql.substring(index);
            } else {
                countHql = "select count(*) " + jpql;
            }
            countHql = JOIN_FETCH_PATTERN.matcher(countHql).replaceAll("join");
            COUNT_CACHE_MAP.put(jpql, countHql);
        }
        return countHql;
    }

    private static final int dbType; // 数据库类型，0-mysql
    static {
        dbType = __getDBType();
    }
    private static final int __getDBType() {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            String className = drivers.nextElement().getClass().getName().toLowerCase();
            if(className.contains("mysql")) {
                return 0;
            }
        }
        return -1;
    }
}
