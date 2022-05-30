package com.iotinall.canteen.annotations;

import com.iotinall.canteen.constants.FreshenMessEnum;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FreshenMess {

    FreshenMessEnum[] value();

}
