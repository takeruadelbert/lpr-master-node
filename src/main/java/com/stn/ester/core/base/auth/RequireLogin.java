package com.stn.ester.core.base.auth;

import org.springframework.core.annotation.AliasFor;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
@PreAuthorize("hasPermission(null,'allowall')")
public @interface RequireLogin {
    @AliasFor(annotation = PreAuthorize.class, attribute = "value") String[] value() default {"hasPermission(null,'allowall')"};
}
