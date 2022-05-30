package com.iotinall.canteen.common.jsr303;

import com.iotinall.canteen.common.jsr303.validators.StrEnumValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *
 * time: 4/23/2019
 *
 * @author xin-bing
 */
@Documented
@Constraint(validatedBy = {StrEnumValidator.class})
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(StrEnum.List.class)
public @interface StrEnum {
    String message();

    String[] values();

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    /**
     * Defines several {@code @Email} constraints on the same element.
     *
     * @see IntEnum
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        StrEnum[] value();
    }
}
