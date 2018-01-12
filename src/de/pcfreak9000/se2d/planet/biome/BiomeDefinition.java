package de.pcfreak9000.se2d.planet.biome;

import de.pcfreak9000.se2d.planet.PlanetData;
import de.pcfreak9000.se2d.planet.TileDefinition;

public abstract class BiomeDefinition {

	public abstract boolean likes(PlanetData data, float tilex, float tiley);

	public abstract TileDefinition getTileDefinition(PlanetData data, float tilex, float tiley);
	
	public int getWeight() {
		return 1;
	}
	
}
