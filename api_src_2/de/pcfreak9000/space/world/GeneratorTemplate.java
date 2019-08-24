package de.pcfreak9000.space.world;

import java.util.HashSet;
import java.util.Set;

import de.pcfreak9000.space.util.RegisterSensitive;

/**
 * TileWorld generator prefab. Capabilities of that generator.
 * 
 * @author pcfreak9000
 *
 */
@RegisterSensitive(registry = "GENERATOR_REGISTRY")
public abstract class GeneratorTemplate {
    
    public static enum GeneratorCapabilitiesBase {
        LVL_ENTRY, ADRESSABLE_PORTAL;
    }
    
    protected final Set<Object> CAPS = new HashSet<>();
        
    public abstract IGenerator createGenerator(long seed);
    
    public GeneratorTemplate() {
        initCaps();
    }
    
    //for anonymous inner classes that can not use a constructor
    protected void initCaps() {
    }
    
    public boolean hasCapabilities(Object... names) {
        for (Object o : names) {
            if (!CAPS.contains(o)) {
                return false;
            }
        }
        return true;
    }
    
}
