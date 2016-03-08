package com.github.schlegel;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface LoggingEvent {
    String value();
    String eventCreator() default "";
    Property[] properties();

    @interface Property {
        String key();
        String value();
    }
}
