package de.pcfreak9000.se2d.universe.celestial;

import java.util.Set;

import de.pcfreak9000.se2d.universe.SpaceCoordinates;
import de.pcfreak9000.se2d.universe.biome.BiomeDefinition;

public interface CelestialBodyDefinition {

	/**
	 * either the position or the parent can be null!
	 * @param seed
	 * @param sc
	 * @param parent
	 * @return
	 */
	CelestialBody generate(long seed, SpaceCoordinates sc, CelestialBody parent);

	Set<BiomeDefinition> getBiomeDefinitions();

	void addBiomeDefinition(BiomeDefinition bd);

}
