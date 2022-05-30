package com.iotinall.canteen.common.jpa.criteria;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * 查询条件
 * @author xinbing
 * @date 2020-06-30
 */
public class Criterion {
    final Op op; // 操作
    final String[] attributes; // 这里使用数组存储，减少split
    final Object value; // 值
    final JoinType joinType; // 生成的join type类型

    private final boolean ignoreBlank; // 是否忽略空数据，事实上只有当eq的时候才允许自己设置，其它时候都不能自己设置

    /**
     *
     * @param op 操作符
     * @param attributes 属性值，多级可用.分割，所以当多级属性进行查询时，会自动使用fetch，使用的是inner fetch，不用额外指定了
     * @param value 值
     * @param ignoreBlank 是否忽略
     */
    private Criterion(Op op, String attributes, Object value, JoinType joinType, boolean ignoreBlank) {
        this.op = op;
        this.attributes = attributes.split("\\.");
        this.value = value;
        this.ignoreBlank = ignoreBlank;
        this.joinType = joinType;
    }

    private static Criterion of(Op op, String attributes, Object value) {
        return new Criterion(op, attributes, value, JoinType.INNER, true);
    }

    private static Criterion of(Op op, String attributes, Object value, Boolean ignoreBlank) {
        return new Criterion(op, attributes, value, JoinType.INNER, ignoreBlank);
    }

    private static Criterion of(Op op, String attributes, Object value, JoinType joinType) {
        return new Criterion(op, attributes, value, joinType, true);
    }
    private static Criterion of(Op op, String attributes, Object value, JoinType joinType, boolean ignoreBlank) {
        return new Criterion(op, attributes, value, joinType, ignoreBlank);
    }


    private Boolean isBlankValue;
    boolean isBlankValue() {
        if(isBlankValue != null) {
            return isBlankValue;
        }
        if(value == null) {
            isBlankValue = true;
        }else if(value instanceof CharSequence) {
            isBlankValue = StringUtils.isBlank((CharSequence) value);
        }else if(value instanceof Collection) {
            isBlankValue = ((Collection<?>) value).isEmpty();
        }else if(value.getClass().isArray()) { // 空数组
            isBlankValue = Array.getLength(value) == 0;
        }else {
            isBlankValue = false;
        }
        return isBlankValue;
    }

    /**
     * 是否有效的Criterion
     * @return boolean
     */
    private boolean effective() {
        return !ignoreBlank || !isBlankValue(); // 不忽略 或者 不为空
    }

    /**
     * 构建Predicate
     * @param <T> generics
     * @param root Root
     * @param criteriaBuilder CriteriaBuilder
     * @return Predicate
     */
    <T> Predicate buildPredicate(Root<T> root, CriteriaBuilder criteriaBuilder, Map<String, Join> joinCache) {
        if(!this.effective()) { // 如果没用的表达式，那么不就不拼接join
            return null;
        }
        Path path;
        if(attributes.length == 1) {
            path = root.get(attributes[0]);
        } else {
            Join join = joinCache.get(attributes[0]);
            if(join == null) {
                join = (Join)root.fetch(attributes[0], this.joinType);
                joinCache.put(attributes[0], join);
            }
            String tmpPath = attributes[0];
            for(int i=1; i<attributes.length - 1; i++) {
                tmpPath = tmpPath + "." + attributes[i];
                if(joinCache.containsKey(tmpPath)) {
                    join = joinCache.get(tmpPath);
                } else {
                    join = (Join) join.fetch(attributes[i], join.getJoinType());
                    joinCache.put(tmpPath, join);
                }
            }
            path = join.get(attributes[attributes.length - 1]);
        }

        return op.build(criteriaBuilder, path, this);
    }

    /**
     * build count predecate
     * @param root
     * @param criteriaBuilder
     * @param joinCache
     * @param <T>
     * @return
     */
    <T> Predicate buildCountPredicate(Root<T> root, CriteriaBuilder criteriaBuilder, Map<String, Join> joinCache) {
        if(!this.effective()) {
            return null;
        }
        Path path;
        if(attributes.length == 1) {
            path = root.get(attributes[0]);
        } else {
            Join join = joinCache.get(attributes[0]);
            if (join == null) {
                join = root.join(attributes[0]);
                joinCache.put(attributes[0], join);
            }
            String tmpPath = attributes[0];
            for(int i=1; i<attributes.length - 1; i++) {
                tmpPath = tmpPath + "." + attributes[i];
                if(joinCache.containsKey(tmpPath)) {
                    join = joinCache.get(tmpPath);
                } else {
                    join = join.join(attributes[i], join.getJoinType());
                    joinCache.put(tmpPath, join);
                }
            }
            path = join.get(attributes[attributes.length - 1]);
        }
        return op.build(criteriaBuilder, path, this);
    }

    /**
     * 等于
     * @param attribute 属性 多级属性用.分割，例如org.employee.role
     * @param value 值
     * @return Criterion
     */
    public static Criterion eq(String attribute, Object value) {
        return Criterion.of(Op.EQ, attribute, value);
    }

    /**
     * 等于
     * @param attribute 属性
     * @param value 值
     * @param ignoreBlank 如果设置位false，那么当value为null，空白字符串时，会使用is null
     * @return Criterion
     */
    public static Criterion eq(String attribute, Object value, JoinType joinType, boolean ignoreBlank) {
        return Criterion.of(Op.EQ, attribute, value, joinType, ignoreBlank);
    }

    /**
     * 不等于
     * @param attribute 属性 多级属性用.分割，例如org.employee.role
     * @param value 值
     * @return Criterion
     */
    public static Criterion ne(String attribute, Object value) {
        return Criterion.of(Op.NE, attribute, value);
    }
    public static Criterion ne(String attribute, Object value, JoinType joinType) {
        return Criterion.of(Op.NE, attribute, value, joinType);
    }

    /**
     * 大于
     * @param attribute 属性 多级属性用.分割，例如org.employee.role
     * @param value 值
     * @return Criterion
     */
    public static Criterion gt(String attribute, Object value) {
        return Criterion.of(Op.GT, attribute, value, JoinType.INNER);
    }
    public static Criterion gt(String attribute, Object value, JoinType joinTYpe) {
        return Criterion.of(Op.GT, attribute, value, joinTYpe);
    }

    /**
     * 大于等于
     * @param attribute 属性 多级属性用.分割，例如org.employee.role
     * @param value 值
     * @return Criterion
     */
    public static Criterion gte(String attribute, Object value) {
        return Criterion.of(Op.GTE, attribute, value);
    }
    public static Criterion gte(String attribute, Object value, JoinType joinType) {
        return Criterion.of(Op.GTE, attribute, value, joinType);
    }

    /**
     * 小于
     * @param attribute 属性 多级属性用.分割，例如org.employee.role
     * @param value 值
     * @return Criterion
     */
    public static Criterion lt(String attribute, Object value) {
        return Criterion.of(Op.LT, attribute, value);
    }
    public static Criterion lt(String attribute, Object value, JoinType joinType) {
        return Criterion.of(Op.LT, attribute, value, joinType);
    }

    /**
     * 小于等于
     * @param attribute 属性 多级属性用.分割，例如org.employee.role
     * @param value 值
     * @return Criterion
     */
    public static Criterion lte(String attribute, Object value) {
        return Criterion.of(Op.LTE, attribute, value);
    }
    public static Criterion lte(String attribute, Object value, JoinType joinType) {
        return Criterion.of(Op.LTE, attribute, value, joinType);
    }

    /**
     * in查询
     * @param attribute 属性 多级属性用.分割，例如org.employee.role
     * @param value 值
     * @return Criterion
     */
    public static Criterion in(String attribute, Collection<?> value) {
        return Criterion.of(Op.IN, attribute, value);
    }
    public static Criterion in(String attribute, Collection<?> value, JoinType joinType) {
        return Criterion.of(Op.IN, attribute, value, joinType);
    }

    /**
     * like查询
     * @param attribute 属性 多级属性用.分割，例如org.employee.role
     * @param value 值
     * @return Criterion
     */
    public static Criterion like(String attribute, Object value) {
        return Criterion.of(Op.LIKE, attribute, value);
    }
    public static Criterion like(String attribute, Object value, JoinType joinType) {
        return Criterion.of(Op.LIKE, attribute, value, joinType);
    }

    /**
     * 百分号在后边的模糊查询
     * @param attribute 属性 多级属性用.分割，例如org.employee.role
     * @param value 值
     * @return Criterion
     */
    public static Criterion prefixLike(String attribute, String value) {
        return Criterion.of(Op.PREFIX_LIKE, attribute, value);
    }
    public static Criterion prefixLike(String attribute, String value, JoinType joinType) {
        return Criterion.of(Op.PREFIX_LIKE, attribute, value, joinType);
    }

    /**
     * 百分号在前边的模糊查询
     * @param attribute 属性 多级属性用.分割，例如org.employee.role
     * @param value 值
     * @return Criterion
     */
    public static Criterion suffixLike(String attribute, String value) {
        return Criterion.of(Op.SUFFIX_LIKE, attribute, value);
    }
    public static Criterion suffixLike(String attribute, String value, JoinType joinType) {
        return Criterion.of(Op.SUFFIX_LIKE, attribute, value, joinType);
    }

    /**
     * 不对字符串进行拼接处理的like
     * @param attribute 属性
     * @param value 值
     * @return Criterion
     */
    public static Criterion rawLike(String attribute, String value) {
        return Criterion.of(Op.RAW_LIKE, attribute, value);
    }
    public static Criterion rawLike(String attribute, String value, JoinType joinType) {
        return Criterion.of(Op.RAW_LIKE, attribute, value, joinType);
    }

    @Override
    public String toString() {
        return "Criterion{" +
                "op=" + op +
                ", attributes=" + Arrays.toString(attributes) +
                ", value=" + value +
                ", joinType=" + joinType +
                ", ignoreBlank=" + ignoreBlank +
                ", isBlankValue=" + isBlankValue +
                '}';
    }
}
