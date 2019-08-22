package de.pcfreak9000.space.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.pcfreak9000.space.core.GameRegistry;
import de.pcfreak9000.spaceexplorer.util.Private;

/**
 * Indicates that the annoted object must be registered, usually in the
 * {@link GameRegistry}. This can be done in the InitEvents.
 *
 * @author pcfreak9000
 *
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Inherited
@Private
public @interface RegisterSensitive {
    
    public String registry();
    
}
