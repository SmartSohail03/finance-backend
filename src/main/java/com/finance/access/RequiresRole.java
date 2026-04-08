package com.finance.access;

import com.finance.model.Role;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// This annotation can be placed on any controller method
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresRole {

    // One or more roles that are allowed to access this method
    Role[] value();
}
