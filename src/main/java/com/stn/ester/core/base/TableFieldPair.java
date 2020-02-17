package com.stn.ester.core.base;

import com.stn.ester.repositories.jpa.base.BaseRepository;
import com.stn.ester.services.base.CrudService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableFieldPair {
    Class<? extends CrudService> service() default CrudService.class;

    Class<? extends BaseRepository> repository() default BaseRepository.class;

    String fieldName() default "";

    String attributeName() default "";

    String attributeArrayName() default "";

    AutoRemoveChildType autoRemoveChildType() default AutoRemoveChildType.ONETOMANY;
}
