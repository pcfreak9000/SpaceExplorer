package de.pcfreak9000.se2d.universe.biome;

public interface BiomeDefinition {
	
	/**
	 * For the same seed the same Biome with the same properties must be produced.
	 * @param seed
	 * @return
	 */
	Biome getBiome(long seed);
	
}
