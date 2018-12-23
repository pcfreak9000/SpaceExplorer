package de.pcfreak9000.spaceexplorer.mod;

import java.util.Arrays;

import de.pcfreak9000.spaceexplorer.util.Private;

/**
 * contains a mod
 *
 * @author pcfreak9000
 *
 */
@Private
public class ModContainer {
    
    private final Mod mod;
    private final Class<?> mainclass;
    private final Object instance;
    
    public ModContainer(final Class<?> mc, final Mod mod, final Object instance) {
        this.mainclass = mc;
        this.mod = mod;
        this.instance = instance;
    }
    
    public Mod getMod() {
        return this.mod;
    }
    
    public Object getInstance() {
        return this.instance;
    }
    
    public Class<?> getModClass() {
        return this.mainclass;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ModContainer)) {
            return false;
        }
        final ModContainer other = (ModContainer) o;
        if (other.mainclass.getName().equals(this.mainclass.getName())) {
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return this.mod.id() + " " + Arrays.toString(this.mod.version());
    }
}
