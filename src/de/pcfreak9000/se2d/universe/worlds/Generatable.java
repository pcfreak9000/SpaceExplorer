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

	private Map<Object, Object> attributes = new HashMap<>();

	/**
	 * populate the given {@link Chunk} with {@link Tile}s and {@link Entity}s etc
	 * 
	 * @param c the {@link Chunk} to be populated
	 */
	public abstract void generateChunk(Chunk c);

	/**
	 * Checks if an attribute is present in this {@link Generatable}
	 * 
	 * @param key the key the attribute is associated with
	 * @return boolean
	 */
	public boolean hasAttribute(Object key) {
		return attributes.containsKey(key);
	}

	/**
	 * Returns the attribute for a given Class if present
	 * 
	 * @param clazz the class
	 * @return the attribute
	 */
	public <T> T getAttributeCK(Class<T> clazz) {
		return (T) getAttribute(clazz);
	}

	/**
	 * Returns the attribute for a given Key if present. Auto-casts the attribute.
	 * 
	 * @param key the key
	 * @return the attribute
	 * @see #getAttribute(Object)
	 */
	public <T> T getAttributeAC(Object key) {
		return (T) getAttribute(key);
	}

	/**
	 * Returns the attribute for a given Key if present
	 * 
	 * @param key the key
	 * @return the attribute
	 */
	public Object getAttribute(Object key) {
		return attributes.get(key);
	}

	/**
	 * Adds an attribute with the objects class as key
	 * 
	 * @param object the attribute
	 * @return this {@link Generatable}
	 */
	public <T> Generatable putAttributeCK(T object) {
		putAttribute(object.getClass(), object);
		return this;
	}

	/**
	 * Adds an attribute with a key
	 * 
	 * @param key the key
	 * @param obj the attribute
	 * @return this {@link Generatable}
	 */
	public Generatable putAttribute(Object key, Object obj) {
		attributes.put(key, obj);
		return this;
	}

}
