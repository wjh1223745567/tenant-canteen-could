package com.iotinall.canteen.common.constant;

import org.apache.commons.lang3.RandomUtils;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;

import java.io.Serializable;

public class UserIdentityGenerator extends IdentityGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) {
        return RandomUtils.nextLong(1000000000000000000L, Long.MAX_VALUE);
    }
}

