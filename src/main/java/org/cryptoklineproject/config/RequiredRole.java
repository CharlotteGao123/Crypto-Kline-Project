package org.cryptoklineproject.config;

import java.lang.annotation.*;

/**
 * @RequiredRole
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiredRole {
    String value();
}
