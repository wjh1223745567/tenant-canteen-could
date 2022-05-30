package com.iotinall.canteen.common.jpa.criteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.Collection;

/**
 * 操作符，如果还需要什么条件，在此自定义就行
 * @author xinbing
 * @date 2020-06-30
 */
enum Op {
    /**
     * 相等，如果值为空，且不忽略空，那么会改成 is null
     */
    EQ(){
        @Override
        Predicate build(CriteriaBuilder criteriaBuilder, Path path, Criterion criterion) {
            if(criterion.isBlankValue()) { // 走到这一步，肯定是不忽略空
                return criteriaBuilder.isNull(path);
            }
            return criteriaBuilder.equal(path, criterion.value);
        }
    },
    /**
     * 不等
     */
    NE(){
        @Override
        Predicate build(CriteriaBuilder criteriaBuilder, Path path, Criterion criterion) {
            return criteriaBuilder.notEqual(path, criterion.value);
        }
    },
    /**
     * 大于
     */
    GT(){
        @Override
        Predicate build(CriteriaBuilder criteriaBuilder, Path path, Criterion criterion) {
            return criteriaBuilder.greaterThan(path, (Comparable)criterion.value);
        }
    },
    /**
     * 大于等于
     */
    GTE(){
        @Override
        Predicate build(CriteriaBuilder criteriaBuilder, Path path, Criterion criterion) {
            return criteriaBuilder.greaterThanOrEqualTo(path, (Comparable)criterion.value);
        }
    },
    /**
     * 小于
     */
    LT(){
        @Override
        Predicate build(CriteriaBuilder criteriaBuilder, Path path, Criterion criterion) {
            return criteriaBuilder.lessThan(path, (Comparable)criterion.value);
        }
    },
    /**
     * 小于等于
     */
    LTE() {
        @Override
        Predicate build(CriteriaBuilder criteriaBuilder, Path path, Criterion criterion) {
            return criteriaBuilder.lessThanOrEqualTo(path, (Comparable)criterion.value);
        }
    },
    /**
     * in 查询
     */
    IN(){
        @Override
        Predicate build(CriteriaBuilder criteriaBuilder, Path path, Criterion criterion) {
            return path.in((Collection) criterion.value);
        }
    },
    /**
     * 全模糊
     */
    LIKE(){
        @Override
        Predicate build(CriteriaBuilder criteriaBuilder, Path path, Criterion criterion) {
            String fuzzy = "%" + criterion.value + "%";
            return criteriaBuilder.like(path, fuzzy);
        }
    },
    /**
     * 前缀模糊查询
     */
    PREFIX_LIKE(){
        @Override
        Predicate build(CriteriaBuilder criteriaBuilder, Path path, Criterion criterion) {
            String fuzzy = criterion.value + "%";
            return criteriaBuilder.like(path, fuzzy);
        }
    },
    /**
     * 后缀模糊查询
     */
    SUFFIX_LIKE(){
        @Override
        Predicate build(CriteriaBuilder criteriaBuilder, Path path, Criterion criterion) {
            String fuzzy = "%" + criterion.value;
            return criteriaBuilder.like(path, fuzzy);
        }
    },
    /**
     * 自己负责拼接的模糊查询
     */
    RAW_LIKE() {
        @Override
        Predicate build(CriteriaBuilder criteriaBuilder, Path path, Criterion criterion) {
            return criteriaBuilder.like(path, (String) criterion.value);
        }
    };

    abstract Predicate build(CriteriaBuilder criteriaBuilder, Path path,Criterion criterion);
}
