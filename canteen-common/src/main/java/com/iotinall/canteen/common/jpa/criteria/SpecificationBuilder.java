package com.iotinall.canteen.common.jpa.criteria;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 先做一版简单的实现
 * 只实现简单的where条件的拼接，以及fetch
 *
 * @author xinbing
 * @date 2020-06-30
 */
public class SpecificationBuilder {
    private final List<CriterionUnion> criterionUnions;
    private final List<FetchUnion> fetchUnions;

    private SpecificationBuilder(int criteriaSize, int fetchUnionSize) {
        this.criterionUnions = new ArrayList<>(criteriaSize);
        this.fetchUnions = new ArrayList<>(fetchUnionSize);
    }

    public static SpecificationBuilder builder() {
        return builder(4, 2);
    }

    /**
     * 定义了一个builder方法，builder方法不做点啥又不太好
     *
     * @param criteriaSize   预估的查询条件数量
     * @param fetchUnionSize 需要fetch几个
     * @return SpecificationBuilder
     */
    public static SpecificationBuilder builder(int criteriaSize, int fetchUnionSize) {
        return new SpecificationBuilder(criteriaSize, fetchUnionSize);
    }

    /**
     * fetch查询，该fetch会fetch整个路径上的所有的值
     *
     * @param attribute 属性，多级属性以.分割，例如org.employees，指的就是fetch org下面的employees，org是root的一个属性
     * @return SpecificationImpl
     */
    public SpecificationBuilder fetch(String attribute) {
        return fetch(attribute, JoinType.INNER);
    }

    public SpecificationBuilder fetch(String attribute, JoinType joinType) {
        FetchUnion union = new FetchUnion(joinType, attribute);
        fetchUnions.add(union);
        return this;
    }

    /**
     * 可以多次调用where，多次where用and作为拼接
     *
     * @param criterias 多个表达式，这多个表达式之间用and连接
     * @return SpecificationImpl
     */
    public SpecificationBuilder where(Criterion... criterias) {
        CriterionUnion union = new CriterionUnion(ConjunctionType.AND, criterias);
        criterionUnions.add(union);
        return this;
    }

    /**
     * 可以多次调用where，多次where之间用and作为拼接
     *
     * @param criterion 多个表达式，这多个表达式用or进行连接
     * @return SpecificationImpl
     */
    public SpecificationBuilder whereByOr(Criterion... criterion) {
        CriterionUnion union = new CriterionUnion(ConjunctionType.OR, criterion);
        criterionUnions.add(union);
        return this;
    }

    /**
     * 可以多次调用where，多次where之间用and作为拼接
     *
     * @param criterion 多个表达式，这多个表达式用or进行连接
     * @return SpecificationImpl
     */
    public SpecificationBuilder whereByOr(List<Criterion> criterion) {
        Criterion[] criteria = new Criterion[criterion.size()];
        CriterionUnion union = new CriterionUnion(ConjunctionType.OR, criterion.toArray(criteria));
        criterionUnions.add(union);
        return this;
    }

//    public SpecificationBuilder whereByOr(CriterionUnion... unions) {
//        criterionUnions.addAll(Arrays.asList(unions));
//        return this;
//    }


    public <T> Specification<T> build() {
        return build(true);
    }

    /**
     * build Specification
     *
     * @param distinct 是否唯一
     * @return Specification<T>
     */
    public <T> Specification<T> build(boolean distinct ) {
        final SpecificationBuilder builder = this;
        Specification<T> specification = (Specification<T>) (root, query, criteriaBuilder) -> {
            if (query.getResultType() == Long.class) { // count 查询
                query.distinct(distinct);
                return builder.buildForCount(root, query, criteriaBuilder);
            } else {
                query.distinct(distinct);
                return builder.buildForEntity(root, query, criteriaBuilder);
            }
        };
        return specification;
    }

    /**
     * 构建buildForSelectEntity
     *
     * @param root
     * @param query
     * @param criteriaBuilder
     * @param <T>
     * @return
     */
    private <T> Predicate buildForEntity(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        // build fetch
        Map<String, Join> joinCache = new HashMap<>(fetchUnions.size() + criterionUnions.size());
        if (!CollectionUtils.isEmpty(fetchUnions)) {
            for (FetchUnion union : fetchUnions) {
                String[] split = union.attribute.split("\\.");
                String tmpPath = split[0];
                Join fetch =  joinCache.get(split[0]);
                if (fetch == null) {
                    fetch = root.join(tmpPath, union.joinType);
                    joinCache.put(tmpPath, fetch);
                }
                for (int i = 1; i < split.length; i++) {
                    tmpPath = tmpPath + "." + split[i];
                    if (joinCache.containsKey(tmpPath)) {
                        fetch = joinCache.get(tmpPath);
                    } else {
                        fetch = fetch.join(split[i], union.joinType);
                        joinCache.put(tmpPath, fetch);
                    }
                }
            }
        }
        Predicate predicate = criteriaBuilder.conjunction();
        // build predicate
        if (!CollectionUtils.isEmpty(criterionUnions)) {
            for (CriterionUnion union : criterionUnions) {
                ConjunctionType type = union.type;
                List<Predicate> predicates = new ArrayList<>(2);
                for (Criterion criterion : union.criterions) {
                    Predicate build = criterion.buildPredicate(root, criteriaBuilder, joinCache);
                    if (build != null) {
                        predicates.add(build);
                    }
                }
                if (!CollectionUtils.isEmpty(predicates)) {
                    Predicate p1;
                    if (type == ConjunctionType.AND) {
                        p1 = criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                    } else {
                        p1 = criteriaBuilder.or(predicates.toArray(new Predicate[0]));
                    }
                    predicate = criteriaBuilder.and(predicate, p1);
                }
            }
        }
        return predicate;
    }

    /**
     * 构建给count查询
     *
     * @param root
     * @param query
     * @param criteriaBuilder
     * @param <T>
     * @return
     */
    private <T> Predicate buildForCount(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Map<String, Join> joinCache = new HashMap<>(fetchUnions.size() + criterionUnions.size());
        Predicate predicate = criteriaBuilder.conjunction();
        // build predicate
        if (!CollectionUtils.isEmpty(criterionUnions)) {
            for (CriterionUnion union : criterionUnions) {
                ConjunctionType type = union.type;
                List<Predicate> predicates = new ArrayList<>(2);
                for (Criterion criterion : union.criterions) {
                    Predicate build = criterion.buildCountPredicate(root, criteriaBuilder, joinCache);
                    if (build != null) {
                        predicates.add(build);
                    }
                }
                if (!CollectionUtils.isEmpty(predicates)) {
                    Predicate p1;
                    if (type == ConjunctionType.AND) {
                        p1 = criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                    } else {
                        p1 = criteriaBuilder.or(predicates.toArray(new Predicate[0]));
                    }
                    predicate = criteriaBuilder.and(predicate, p1);
                }
            }
        }
        return predicate;
    }

    public static class CriterionUnion {
        private final ConjunctionType type;
        private final Criterion[] criterions;

        private CriterionUnion(ConjunctionType type, Criterion[] criterions) {
            this.type = type;
            this.criterions = criterions;
        }

        public static CriterionUnion of(Criterion... criteria) {
            return new CriterionUnion(ConjunctionType.AND, criteria);
        }

        public static CriterionUnion of(ConjunctionType type, Criterion... criteria) {
            return new CriterionUnion(type, criteria);
        }
    }

    private static class FetchUnion {
        private final JoinType joinType;
        private final String attribute;

        private FetchUnion(JoinType joinType, String attribute) {
            this.joinType = joinType;
            this.attribute = attribute;
        }
    }

    private enum ConjunctionType {
        AND, OR
    }
}
