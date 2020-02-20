package com.stn.ester.core.base;

import com.stn.ester.services.base.CrudService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnDeleteSetParentNull {
    Class<? extends CrudService> service();

    String fieldName();

    @interface List {
        OnDeleteSetParentNull[] value();
    }
}
