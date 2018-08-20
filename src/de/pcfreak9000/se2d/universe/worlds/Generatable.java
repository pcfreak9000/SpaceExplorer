package de.pcfreak9000.se2d.universe.worlds;

import java.util.HashMap;
import java.util.Map;

import de.pcfreak9000.se2d.universe.objects.Entity;
import de.pcfreak9000.se2d.universe.tiles.Tile;

/**
 * the abstract class used by a {@link World} to populate its {@link Chunk}s
 * 
 * @author pcfreak9000
 *
 */
public abstract class Generatable {

	private Map<Class<?>, Object> attributes = new HashMap<>();

	/**
	 * populate the given {@link Chunk} with {@link Tile}s and {@link Entity}s etc
	 * 
	 * @param c the {@link Chunk} to be populated
	 */
	public abstract void generateChunk(Chunk c);

	public boolean hasAttribute(Class<?> clazz) {
		return attributes.containsKey(clazz);
	}

	@SuppressWarnings("unchecked")
	public <T>T getAttribute(Class<T> clazz) {
		return (T) attributes.get(clazz);
	}

	public <T> Generatable putAttribute(Class<T> clazz, T object) {
		attributes.put(clazz, object);
		return this;
	}

}
