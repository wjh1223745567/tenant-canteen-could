package com.iotinall.canteen.common.jsr303;

import com.iotinall.canteen.common.jsr303.validators.IpValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * description: IP表达式
 * time: 4/27/2019 14:36
 * @author xin-bing
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {IpValidator.class })
public @interface Ip {

    IpType ipType() default IpType.IPV4; // 默认ipv4格式

    String message() default "ip地址不正确";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
    enum IpType {
        IPV4, IPV6, ALL
    }
}