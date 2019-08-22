package de.pcfreak9000.space.world;

import de.pcfreak9000.space.util.RegisterSensitive;

@RegisterSensitive(registry = "GENERATOR_REGISTRY")
public abstract class GeneratorTemplate {
        
    public abstract boolean satisfiesPlace(/*TODO nice place args*/);
    
    public abstract IGenerator createGenerator(long seed);
    
    public boolean canStart() {
        return false;
    }
    
    public boolean canPortal() {
        return false;
    }
    
}
