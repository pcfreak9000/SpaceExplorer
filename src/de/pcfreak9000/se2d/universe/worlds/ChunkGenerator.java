package de.pcfreak9000.se2d.universe.worlds;

import de.pcfreak9000.se2d.universe.objects.Entity;
import de.pcfreak9000.se2d.universe.tiles.Tile;

/**
 * the interface used by a {@link World} to populate its {@link Chunk}s
 * @author pcfreak9000
 *
 */
public interface ChunkGenerator {

	/**
	 * populate the given {@link Chunk} with {@link Tile}s and {@link Entity}s etc
	 * @param c the {@link Chunk} to be populated
	 */
	void generateChunk(Chunk c);

}
