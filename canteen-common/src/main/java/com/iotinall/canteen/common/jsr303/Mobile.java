package com.iotinall.canteen.common.jsr303;

import com.iotinall.canteen.common.jsr303.validators.MobileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * description: 手机号码校验器
 * time: 4/27/2019 14:36
 * @author xin-bing
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {MobileValidator.class })
public @interface Mobile {

    boolean required() default true; 

    String message() default "手机号码格式不正确";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}