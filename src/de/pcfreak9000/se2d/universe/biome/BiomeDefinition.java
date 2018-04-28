package de.pcfreak9000.se2d.universe.biome;

import de.pcfreak9000.se2d.universe.celestial.CelestialBody;

public interface BiomeDefinition {

	/**
	 * For the same seed the same Biome with the same properties must be produced.
	 * 
	 * @param seed
	 * @return
	 */
	Biome getBiome(long seed);

	float evaluate(CelestialBody body);

	boolean likes(CelestialBody body);

}
