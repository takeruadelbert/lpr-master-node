package com.stn.ester.core.validation;

import com.stn.ester.core.validation.constraintvalidator.IsUniqueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsUniqueValidator.class)
@Repeatable(IsUnique.List.class)
public @interface IsUnique {
    String message() default "This field already exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String columnNames();

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        IsUnique[] value();
    }
}
