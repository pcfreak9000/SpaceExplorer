package de.pcfreak9000.spaceexplorer.universe.celestial;

import java.util.HashSet;
import java.util.Set;

import de.pcfreak9000.spaceexplorer.game.core.GameRegistry;
import de.pcfreak9000.spaceexplorer.universe.SpaceCoordinates;
import de.pcfreak9000.spaceexplorer.universe.biome.BiomeDefinition;
import de.pcfreak9000.spaceexplorer.util.RegisterSensitive;

/**
 * Defines the general properties of a {@link CelestialBody}.
 *
 * @author pcfreak9000
 *
 */
@RegisterSensitive
public abstract class CelestialBodyDefinition {
    
    private final Set<BiomeDefinition> definitions = new HashSet<>();
    
    public CelestialBodyDefinition() {
        addBiomeDefinition(GameRegistry.DEFAULT_BIOME);
    }
    
    /**
     * either the position or the parent can be null!
     * 
     * @param seed
     * @param sc
     * @param parent
     * @return {@link CelestialBody}
     */
    public abstract CelestialBody generate(long seed, SpaceCoordinates sc, CelestialBody parent);
    
    public Set<BiomeDefinition> getBiomeDefinitions() {
        return this.definitions;
    }
    
    public void addBiomeDefinition(final BiomeDefinition bd) {
        this.definitions.add(bd);
    }
    
    /**
     * Can the player start here? (e.g. if he won't suffocate here because of no
     * atmosphere)
     * 
     * @return boolean
     */
    public boolean isStartCapable() {
        return false;
    }
    
    @Override
    public String toString() {
        return "CBD " + this.getClass().getSimpleName() + " isSC=" + isStartCapable();
    }
}
