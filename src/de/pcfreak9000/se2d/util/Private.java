package de.pcfreak9000.se2d.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicates that the annotated object should not be directly accessed by mods.
 * 
 * @author pcfreak9000
 *
 */
@Retention(RetentionPolicy.SOURCE)
@Private
public @interface Private {
}
