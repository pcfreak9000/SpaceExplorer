package de.pcfreak9000.spaceexplorer.util;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicates that the annotated object should not be directly accessed by mods.
 *
 * @author pcfreak9000
 *
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Private
public @interface Private {
}
