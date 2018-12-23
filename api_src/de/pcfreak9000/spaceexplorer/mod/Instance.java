package de.pcfreak9000.spaceexplorer.mod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Instance {
    
    String id();
    
    long[] requiredVersion() default {};
}
