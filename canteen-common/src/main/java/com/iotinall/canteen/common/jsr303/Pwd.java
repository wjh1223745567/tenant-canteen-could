package com.iotinall.canteen.common.jsr303;

import com.iotinall.canteen.common.jsr303.validators.PwdValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 校验是否时合法的密码
 * @author xin-bing
 * @date 10/17/2019 14:44
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {PwdValidator.class })
public @interface Pwd {
    boolean required() default true;
    // 默认的
    // String pattern() default "^[a-zA-Z][a-zA-Z-_.*@]{5, 15}"; // 字母开头，字母数字组成，6~16位
    String message() default "密码格式不正确";

    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
