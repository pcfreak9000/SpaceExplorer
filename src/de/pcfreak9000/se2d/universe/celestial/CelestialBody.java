package de.pcfreak9000.se2d.universe.celestial;

import java.util.List;
import java.util.stream.Collectors;

import de.pcfreak9000.se2d.universe.Orbit;
import de.pcfreak9000.se2d.universe.biome.Biome;
import de.pcfreak9000.se2d.universe.biome.BiomeDefinition;
import de.pcfreak9000.se2d.universe.tiles.Tile;
import de.pcfreak9000.se2d.universe.tiles.TileDefinition;
import de.pcfreak9000.se2d.universe.worlds.Chunk;
import de.pcfreak9000.se2d.universe.worlds.ChunkGenerator;
import de.pcfreak9000.se2d.universe.worlds.World;

public class CelestialBody implements ChunkGenerator{

	private CelestialBodyDefinition generator;
	private Orbit orbit;
	private World world;

	private String name;
	private long seed;

	public CelestialBody(CelestialBodyDefinition generator, Orbit orbit, int world_radius, String name, long seed) {
		Catalog.checkRegistered(generator);
		this.generator = generator;
		this.orbit = orbit;
		this.name = name;
		this.seed = seed;
		this.world = new World(name, this, world_radius);
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

	public long getSeed() {
		return seed;
	}

	public CelestialBodyDefinition getDefinition() {
		return generator;
	}

	public boolean isVisitable() {
		return world != null;
	}
	
	@Override
	public void generateChunk(Chunk c) {
		if (!isVisitable()) {
			throw new IllegalStateException("The CelestialBody " + this.getClass().getName()
					+ " is not visitable so it should not be generated!");
		}
		for (int x = 0; x < Chunk.CHUNKSIZE_T; x++) {
			for (int y = 0; y < Chunk.CHUNKSIZE_T; y++) {
				int globalTileX = x + c.getChunkX() * Chunk.CHUNKSIZE_T;
				int globalTileY = y + c.getChunkY() * Chunk.CHUNKSIZE_T;
				if (inBounds(globalTileX, globalTileY)) {
					Biome biome = getBiomeDefinition(generator.getBiomeDefinitions().stream()
							.filter((def) -> def.likes(this)).collect(Collectors.toList()), globalTileX, globalTileY)
									.getBiome(seed);
					Tile tile = biome.getTileDefinition(globalTileX, globalTileY).newTile();
					tile.getTransform().setPosition(globalTileX * TileDefinition.TILE_SIZE,
							globalTileY * TileDefinition.TILE_SIZE);
					adjustTile(tile);
					c.addTile(tile, x, y);
					if (tile.isValid()) {
						biome.decorate(tile);
					}
				}
			}
		}
	}

	/**
	 * For the same position the same BiomeDefinition must be returned for this
	 * CelestialBody.
	 * 
	 * @param possibilities
	 * @param globalTileX
	 * @param globalTileY
	 * @return
	 */
	public BiomeDefinition getBiomeDefinition(List<BiomeDefinition> possibilities, int globalTileX, int globalTileY) {
		return possibilities.get(0);
	}

	public boolean inBounds(int globalTileX, int globalTileY) {
		return true;
	}

	public void adjustTile(Tile t) {
	}

}
