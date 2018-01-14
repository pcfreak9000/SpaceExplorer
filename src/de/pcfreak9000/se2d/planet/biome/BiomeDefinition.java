package de.pcfreak9000.se2d.planet.biome;

import de.pcfreak9000.se2d.planet.PlanetData;
import de.pcfreak9000.se2d.planet.TileDefinition;

public abstract class BiomeDefinition {

	private int flags=0;
	
	public BiomeDefinition(int flags) {
		this.flags = flags;
	}
	
	public abstract boolean likes(PlanetData data, int tilex, int tiley);

	public abstract TileDefinition getTileDefinition(PlanetData data, int tilex, int tiley);
	
	public int getFlags() {
		return flags;
	}
	
	public boolean isFlagSet(int flag) {
		return (flags&flag)==flag;
	}
	
}
