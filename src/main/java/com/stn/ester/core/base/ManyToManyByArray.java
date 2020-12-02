package com.stn.ester.core.base;

import com.stn.ester.entities.base.BaseEntity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ManyToManyByArray {

    String attributeArrayName();

    String attributeName();

    Class<? extends BaseEntity> targetEntity();

    Class<? extends BaseEntity> joinEntity();

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        ManyToManyByArray[] value();
    }
}
