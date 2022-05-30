package com.iotinall.canteen.protocol.excel.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 批注
 *
 * @author loki
 * @date 2020/03/10 18:19
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelComment {

    /**
     * 批注显示信息
     *
     * @return
     */
    String text() default "";
}
