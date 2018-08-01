package de.pcfreak9000.se2d.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import de.pcfreak9000.se2d.game.core.GameRegistry;

/**
 * Indicates that the annoted object must be registered, usually in the {@link GameRegistry}. This can be done in the InitEvents.
 * @author pcfreak9000
 *
 */
@Retention(RetentionPolicy.SOURCE)
@Private
public @interface RegisterSensitive {
}
