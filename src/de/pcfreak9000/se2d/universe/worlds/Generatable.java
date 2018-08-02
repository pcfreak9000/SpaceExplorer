package de.pcfreak9000.se2d.universe.worlds;

import de.pcfreak9000.se2d.universe.objects.Entity;
import de.pcfreak9000.se2d.universe.tiles.Tile;

/**
 * the abstract class used by a {@link World} to populate its {@link Chunk}s
 * 
 * @author pcfreak9000
 *
 */
public abstract class Generatable {

	private Fields fields = new Fields();

	/**
	 * populate the given {@link Chunk} with {@link Tile}s and {@link Entity}s etc
	 * 
	 * @param c
	 *            the {@link Chunk} to be populated
	 */
	public abstract void generateChunk(Chunk c);

	/**
	 * Informations about the environment of this {@link Generatable}
	 * 
	 * @return fields
	 */
	public Fields getFields() {
		return fields;
	}

}
