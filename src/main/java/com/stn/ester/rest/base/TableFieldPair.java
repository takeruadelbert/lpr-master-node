package com.stn.ester.rest.base;

import com.stn.ester.rest.service.AppService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableFieldPair {
    Class<? extends AppService> service();

    String tableName();

    String fieldName();


}
