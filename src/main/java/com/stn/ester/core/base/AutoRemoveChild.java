package com.stn.ester.core.base;

import com.stn.ester.repositories.jpa.base.BaseRepository;
import com.stn.ester.services.base.CrudService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
//if type is manytomany, use repository, if type is onetomany use service
public @interface AutoRemoveChild {
    String attributeName();

    String fieldName() default "";

    String attributeArrayName() default "";

    Class<? extends BaseRepository> repository() default BaseRepository.class;

    Class<? extends CrudService> service() default CrudService.class;

    AutoRemoveChildType type() default AutoRemoveChildType.ONETOMANY;

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        AutoRemoveChild[] value();
    }
}
