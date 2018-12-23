package de.pcfreak9000.spaceexplorer.universe.biome;

import de.pcfreak9000.spaceexplorer.universe.celestial.CelestialBody;
import de.pcfreak9000.spaceexplorer.universe.worlds.Generatable;
import de.pcfreak9000.spaceexplorer.util.RegisterSensitive;

/**
 * Defines the general properties of a {@link Biome}. Usually generates a
 * {@link Biome} for a given seed.
 *
 * @author pcfreak9000
 *
 */
@RegisterSensitive
public interface BiomeDefinition {
    
    /**
     * For the same seed the same {@link Biome} with the same properties must be
     * produced.
     * 
     * @param seed the Biome's seed
     * @return a {@link Biome}
     */
    Biome getBiome(long seed);
    
    float evaluate(Generatable body);
    
    /**
     * can this {@link BiomeDefinition} be used with that {@link CelestialBody}?
     * 
     * @param body the {@link CelestialBody}
     * @return boolean
     */
    boolean likes(Generatable body);
    
}
