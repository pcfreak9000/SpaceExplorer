package de.pcfreak9000.space.core;

import java.util.HashMap;

import de.omnikryptec.util.Logger;
import de.pcfreak9000.space.util.RegisterSensitive;

/**
 * used to register everything annotated by {@link RegisterSensitive}
 *
 * @author pcfreak9000
 *
 * @param <T> the Type of whats being registered
 */
public class GameRegistry<T> {
    
    public static final TileRegistry TILE_REGISTRY = new TileRegistry();
    
    protected final Logger LOGGER = Logger.getLogger(getClass());
    
    protected final HashMap<String, T> registered = new HashMap<>();
    
    public GameRegistry<T> register(final String name, final T data) {
        final T before = this.registered.put(name, data);
        if (before != null) {
            LOGGER.info("Overriden: " + name);
        }
        return this;
    }
    
    public T get(final String name) {
        return this.registered.get(name);
    }
    
    public boolean isRegistered(final String name) {
        return this.registered.containsKey(name);
    }
    
    public boolean isRegistered(final T data) {
        return this.registered.containsValue(data);
    }
    
    public void checkRegistered(final T data) {
        if (!isRegistered(data)) {
            throw new IllegalStateException(data.getClass().getSimpleName() + " " + data + " is not registered!");
        }
    }
    
}
