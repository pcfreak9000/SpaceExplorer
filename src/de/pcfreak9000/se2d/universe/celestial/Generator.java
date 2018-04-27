package de.pcfreak9000.se2d.universe.celestial;

import de.pcfreak9000.se2d.universe.SpaceCoordinates;
import de.pcfreak9000.se2d.universe.biome.Biome;

public interface Generator<T extends CelestialBody> {

	T generate(long seed, SpaceCoordinates sc);
	Biome getBiome(T body, int tx, int ty);
	void populateChunk(Chunk c);
	
}
