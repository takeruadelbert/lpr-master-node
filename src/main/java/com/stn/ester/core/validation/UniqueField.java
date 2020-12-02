package com.stn.ester.core.validation;

import com.stn.ester.core.validation.constraintvalidator.UniqueFieldValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueFieldValidator.class)
@Repeatable(UniqueField.List.class)
public @interface UniqueField {
    String message() default "This field already exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String columnNames();

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        UniqueField[] value();
    }
}
