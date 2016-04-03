package com.github.schlegel;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface AnalyticsEvent {
    String value();
    Property[] properties() default {};

    @interface Property {
        String key();
        String value();
    }
}
