package de.pcfreak9000.space.tileworld;

import java.util.HashSet;
import java.util.Set;

import com.google.errorprone.annotations.ForOverride;

import de.omnikryptec.util.math.Weighted;
import de.pcfreak9000.space.util.RegisterSensitive;

/**
 * TileWorld generator. Capabilities of that generator.
 *
 * @author pcfreak9000
 *
 */
@RegisterSensitive(registry = "GENERATOR_REGISTRY")
public abstract class WorldGenerator implements Weighted {

    public static enum GeneratorCapabilitiesBase {
        LVL_ENTRY, ADRESSABLE_PORTAL;
    }

    protected final Set<Object> CAPS = new HashSet<>();

    public abstract World generateWorld(long seed);

    public WorldGenerator() {
        initCaps();
    }

    //for anonymous inner classes that can not use a constructor
    @ForOverride
    protected void initCaps() {
    }

    @ForOverride
    @Override
    public int getWeight() {
        return 100;
    }

    public final boolean hasCapabilities(Object... names) {
        for (Object o : names) {
            if (!this.CAPS.contains(o)) {
                return false;
            }
        }
        return true;
    }

}
