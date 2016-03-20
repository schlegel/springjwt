package com.github.schlegel;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface LoggingEvent {
    String value();
    Property[] properties() default {};

    @interface Property {
        String key();
        String value();
    }
}
