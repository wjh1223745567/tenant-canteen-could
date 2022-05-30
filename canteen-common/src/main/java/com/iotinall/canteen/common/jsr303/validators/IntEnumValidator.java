package com.iotinall.canteen.common.jsr303.validators;

import com.iotinall.canteen.common.jsr303.IntEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * int 枚举校验器
 * time: 4/23/2019
 *
 * @author xin-bing
 */
public class IntEnumValidator implements ConstraintValidator<IntEnum, Integer> {
    private Set<Integer> set;
    @Override
    public void initialize(IntEnum idValidator) {
        int[] values = idValidator.values();
        set = IntStream.of(values).collect(HashSet::new, Set::add, Set::addAll);
    }

    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext ctx) {
        return id == null || set.contains(id);
    }
}