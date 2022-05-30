package com.iotinall.canteen.common.jsr303;

import com.iotinall.canteen.common.jsr303.validators.IdCardValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 身份证校验
 * @author xin-bing
 * @date 10/25/2019 15:25
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {IdCardValidator.class })
public @interface IdCard {

    boolean required() default false; // 不是必须的

    ValidGrade grade() default ValidGrade.LOOSE; // 校验等级

    String message() default "身份证号码格式不正确";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
    enum ValidGrade {
        LOOSE, // 宽松
        STRICT // 严格
    }
}
