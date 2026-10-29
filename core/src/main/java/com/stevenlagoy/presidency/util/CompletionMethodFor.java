package com.stevenlagoy.presidency.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method annotation allowing a subclass of {@link com.stevenlagoy.presidency.core.EntityFactory.EntityContext}
 * to specify that a method may be used to complete the value of a field when that field is {@code null}.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CompletionMethodFor {
    /** Name of the field this method completes. */
    String value();
}
