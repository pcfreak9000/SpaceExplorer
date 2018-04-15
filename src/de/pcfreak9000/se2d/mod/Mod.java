package de.pcfreak9000.se2d.mod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.pcfreak9000.se2d.game.Launcher;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Mod {

	String id();

	String name();

	long[] version();

	String[] se2dversion() default { Launcher.VERSION };

	boolean accessible() default true;
}