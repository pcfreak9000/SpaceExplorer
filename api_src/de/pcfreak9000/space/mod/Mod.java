package de.pcfreak9000.space.mod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.pcfreak9000.space.core.Space;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Mod {

    String id();

    String name();

    long[] version();

    String resourceLocation();

    String[] se2dversion() default { Space.VERSION };

    boolean accessible() default true;
}