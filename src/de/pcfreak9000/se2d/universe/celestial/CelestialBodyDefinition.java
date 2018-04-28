package de.pcfreak9000.se2d.universe.celestial;

import java.util.Set;

import de.pcfreak9000.se2d.universe.SpaceCoordinates;
import de.pcfreak9000.se2d.universe.biome.BiomeDefinition;

public interface CelestialBodyDefinition {

	CelestialBody generate(long seed, SpaceCoordinates sc);
	
	Set<BiomeDefinition> getBiomeDefinitions();

}
