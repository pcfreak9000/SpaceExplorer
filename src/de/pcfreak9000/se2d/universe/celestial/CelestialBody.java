package de.pcfreak9000.se2d.universe.celestial;

import java.util.List;
import java.util.stream.Collectors;

import de.pcfreak9000.se2d.game.core.GameRegistry;
import de.pcfreak9000.se2d.universe.Orbit;
import de.pcfreak9000.se2d.universe.biome.Biome;
import de.pcfreak9000.se2d.universe.biome.BiomeDefinition;
import de.pcfreak9000.se2d.universe.tiles.Tile;
import de.pcfreak9000.se2d.universe.tiles.TileDefinition;
import de.pcfreak9000.se2d.universe.worlds.Chunk;
import de.pcfreak9000.se2d.universe.worlds.ChunkGenerator;
import de.pcfreak9000.se2d.universe.worlds.World;

/**
 * Represents all kinds of CelestialBodys
 * @author pcfreak9000
 *
 */
public class CelestialBody implements ChunkGenerator {

	private CelestialBodyDefinition generator;
	private Orbit orbit;
	private World world;

	private String name;
	private long seed;
	private int tileRadius;

	/**
	 * 
	 * @param generator the CBs generating {@link CelestialBodyDefinition}
	 * @param orbit instance of {@link Orbit} containing information about this CBs orbit
	 * @param world_radius the biggest radius of the {@link World}, negative values will result in a non-visitable CB
	 * @param name the CBs name
	 * @param seed a seed for this CB at this position
	 */
	public CelestialBody(CelestialBodyDefinition generator, Orbit orbit, int world_radius, String name, long seed) {
		GameRegistry.getCelestialBodyRegistry().checkRegistered(generator);
		this.generator = generator;
		this.orbit = orbit;
		this.name = name;
		this.seed = seed;
		this.tileRadius = world_radius;
		if(tileRadius>0) {
			this.world = new World(name, this, world_radius);
		}
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

	public int getTileRadius() {
		return tileRadius;
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
					Tile tile = biome.getTileDefinition(globalTileX, globalTileY).newTile(globalTileX, globalTileY);
					tile.getTransform().setPosition(globalTileX * TileDefinition.TILE_SIZE,
							globalTileY * TileDefinition.TILE_SIZE);
					adjustTile(c, biome, tile);
					c.addTile(tile, x, y);
					if (tile.isValid()) {
						biome.decorate(c, tile);
					}
				}
			}
		}
	}

	/**
	 * For the same position the same {@link BiomeDefinition} must be returned for this
	 * CelestialBody.
	 * 
	 * @param possibilities possible BiomeDefinitions
	 * @param globalTileX
	 * @param globalTileY
	 * @return a {@link BiomeDefinition}
	 */
	public BiomeDefinition getBiomeDefinition(List<BiomeDefinition> possibilities, int globalTileX, int globalTileY) {
		return possibilities.get(0);
	}

	/**
	 * Is the position in the boundaries of the {@link CelestialBody}?
	 * @param globalTileX
	 * @param globalTileY
	 * @return
	 */
	public boolean inBounds(int globalTileX, int globalTileY) {
		return true;
	}

	/**
	 * e.g. invalidate the {@link Tile}
	 * 
	 * @param c the tile's {@link Chunk}
	 * @param b the tile's {@link Biome}
	 * @param t the tile itself
	 */
	public void adjustTile(Chunk c, Biome b, Tile t) {
	}

	@Override
	public String toString() {
		return "CB " + this.getClass().getSimpleName() + ", name=\"" + name + "\", r=" + tileRadius + "ts; "
				+ generator.toString() + ", s=" + seed;
	}
}
