package de.pcfreak9000.se2d.universe.celestial;

import java.util.Set;

import de.pcfreak9000.se2d.universe.Orbit;
import de.pcfreak9000.se2d.universe.biome.Biome;
import de.pcfreak9000.se2d.universe.biome.BiomeDefinition;

public abstract class CelestialBody {

	private CelestialBodyDefinition generator;
	private Orbit orbit;
	private World world;

	private String name;
	private long seed;
	
	public CelestialBody(CelestialBodyDefinition generator, Orbit orbit, World world, String name, long seed) {
		this.generator = generator;
		this.orbit = orbit;
		this.world = world;
		this.name = name;
		this.seed = seed;
	}

	public Orbit getOrbit() {
		return orbit;
	}

	public World getWorld() {
		return world;
	}

	public String getName() {
		return name;
	}

	public void generateChunk(Chunk c) {
		for (int x = 0; x < Chunk.CHUNKSIZE_T; x++) {
			for (int y = 0; y < Chunk.CHUNKSIZE_T; y++) {
				int globalTileX = x + c.getChunkX() * Chunk.CHUNKSIZE_T;
				int globalTileY = y + c.getChunkY() * Chunk.CHUNKSIZE_T;
				if(inBounds(globalTileX, globalTileY)) {
					Biome biome = getBiomeDefinition(generator.getBiomeDefinitions(), globalTileX, globalTileY).getBiome(seed);
					Tile tile = biome.getTileDefinition(globalTileX, globalTileY).newTile();
					adjustTile(tile);
					c.addTile(tile,x,y);
					if(tile.isValid()) {
						biome.decorate(tile);
					}
				}
			}
		}
	}
	
	public abstract BiomeDefinition getBiomeDefinition(Set<BiomeDefinition> possibilities, int globalTileX, int globalTileY);
	
	public abstract boolean inBounds(int globalTileX, int globalTileY);
	
	public abstract void adjustTile(Tile t);

}
