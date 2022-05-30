package com.iotinall.canteen.protocol.excel.annotation;


import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 列属性信息
 * <p>
 * 支持Java对象数据类型：Boolean、String、Short、Integer、Long、Float、Double、Date
 * 支持Excel的Cell类型为：String
 *
 * @author loki
 * @date 2019/10/11 3:49 下午
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelField {

    /**
     * 列名称
     *
     * @return
     */
    String name() default "";

    /**
     * 列宽 (大于0时生效; 如果不指定列宽，将会自适应调整宽度；)
     *
     * @return
     */
    int width() default 0;

    /**
     * 列序号
     *
     * @return
     */
    int sort() default 0;

    /**
     * 水平对齐方式
     *
     * @return
     */
    HorizontalAlignment align() default HorizontalAlignment.LEFT;

    /**
     * 时间格式化，日期类型时生效
     *
     * @return
     */
    String format() default "yyyy-MM-dd";

}
