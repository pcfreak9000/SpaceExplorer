package de.pcfreak9000.se2d.universe.biome;

import de.pcfreak9000.se2d.universe.tiles.Tile;
import de.pcfreak9000.se2d.universe.tiles.TileDefinition;
import de.pcfreak9000.se2d.universe.worlds.Chunk;

/**
 * a unique Biome usually produced by a {@link BiomeDefinition}
 * @author pcfreak9000
 */
public interface Biome {

	/**
	 * For the same position the same {@link TileDefinition} must be returned.
	 * 
	 * @param gtx global tile x
	 * @param gty global tile y
	 * @return a {@link TileDefinition}
	 */
	public TileDefinition getTileDefinition(int gtx, int gty);

	/**
	 * the same {@link Tile} should have the same decoration (e.g. Entitys)
	 * @param c the {@link Chunk} of the {@link Tile}
	 * @param tile the {@link Tile} itself
	 */
	public void decorate(Chunk c, Tile tile);

}
