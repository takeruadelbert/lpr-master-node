package com.stn.ester.core.validation;

import com.stn.ester.core.validation.constraintvalidator.IsUniqueValidator;
import com.stn.ester.entities.base.BaseEntity;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsUniqueValidator.class)
@Repeatable(IsUnique.List.class)
public @interface IsUnique {
    String message() default "This field already exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    public String key() default "";

    public Class<? extends BaseEntity> entityClass();

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        IsUnique[] value();
    }
}
