package de.pcfreak9000.spaceexplorer.universe.worlds;

import java.util.HashMap;
import java.util.Map;

import de.pcfreak9000.spaceexplorer.universe.objects.Entity;
import de.pcfreak9000.spaceexplorer.universe.tiles.Tile;

/**
 * the abstract class used by a {@link World} to populate its {@link Chunk}s
 *
 * @author pcfreak9000
 *
 */
public abstract class Generatable {

    private final Map<Object, Object> attributes = new HashMap<>();

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
    public boolean hasAttribute(final Object key) {
        return this.attributes.containsKey(key);
    }

    /**
     * Returns the attribute for a given Class if present
     *
     * @param clazz the class
     * @return the attribute
     */
    public <T> T getAttributeCK(final Class<T> clazz) {
        return (T) getAttribute(clazz);
    }

    /**
     * Returns the attribute for a given Key if present. Auto-casts the attribute.
     *
     * @param key the key
     * @return the attribute
     * @see #getAttribute(Object)
     */
    public <T> T getAttributeAC(final Object key) {
        return (T) getAttribute(key);
    }

    /**
     * Returns the attribute for a given Key if present
     *
     * @param key the key
     * @return the attribute
     */
    public Object getAttribute(final Object key) {
        return this.attributes.get(key);
    }

    /**
     * Adds an attribute with the objects class as key
     *
     * @param object the attribute
     * @return this {@link Generatable}
     */
    public <T> Generatable putAttributeCK(final T object) {
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
    public Generatable putAttribute(final Object key, final Object obj) {
        this.attributes.put(key, obj);
        return this;
    }

}
