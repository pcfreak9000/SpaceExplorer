package de.pcfreak9000.spaceexplorer.game.core;

import java.util.HashMap;

import de.pcfreak9000.spaceexplorer.items.Item;
import de.pcfreak9000.spaceexplorer.universe.biome.BiomeDefinition;
import de.pcfreak9000.spaceexplorer.universe.biome.DefaultBiome;
import de.pcfreak9000.spaceexplorer.universe.celestial.CelestialBodyRegistry;
import de.pcfreak9000.spaceexplorer.universe.objects.EntityDefinition;
import de.pcfreak9000.spaceexplorer.universe.tiles.TileDefinition;
import de.pcfreak9000.spaceexplorer.util.RegisterSensitive;
import de.pcfreak9000.spaceexplorer.util.Se2Dlog;

/**
 * used to register everything annotated by {@link RegisterSensitive}
 * 
 * @author pcfreak9000
 *
 * @param <T> the Type of whats being registered
 */
public class GameRegistry<T> {

	private static final GameRegistry<TileDefinition> tileRegistry;
	private static final GameRegistry<BiomeDefinition> biomeRegistry;
	private static final CelestialBodyRegistry celestialBodyRegistry;
	private static final GameRegistry<EntityDefinition> worldObjectRegistry;
	private static final GameRegistry<Item> itemRegistry;

	public static final TileDefinition MISSING_DEFINITION;
	public static final DefaultBiome DEFAULT_BIOME;

	static {
		tileRegistry = new GameRegistry<>();
		biomeRegistry = new GameRegistry<>();
		MISSING_DEFINITION = new TileDefinition("");
		tileRegistry.register("missing_tile", MISSING_DEFINITION);
		DEFAULT_BIOME = new DefaultBiome();
		biomeRegistry.register("default_biome", DEFAULT_BIOME);
		celestialBodyRegistry = new CelestialBodyRegistry();
		worldObjectRegistry = new GameRegistry<>();
		itemRegistry = new GameRegistry<>();
	}

	public static GameRegistry<TileDefinition> getTileRegistry() {
		return tileRegistry;
	}

	public static GameRegistry<BiomeDefinition> getBiomeRegistry() {
		return biomeRegistry;
	}

	public static CelestialBodyRegistry getCelestialBodyRegistry() {
		return celestialBodyRegistry;
	}

	public static GameRegistry<EntityDefinition> getWorldObjectRegistry() {
		return worldObjectRegistry;
	}

	public static GameRegistry<Item> getItemRegistry() {
		return itemRegistry;
	}

	private HashMap<String, T> registered = new HashMap<>();

	public GameRegistry<T> register(String name, T data) {
		T before = registered.put(name, data);
		if (before != null) {
			Se2Dlog.log("Overriden: " + name);
		}
		return this;
	}

	public T get(String name) {
		return registered.get(name);
	}

	public boolean isRegistered(String name) {
		return registered.containsKey(name);
	}

	public boolean isRegistered(T data) {
		return registered.containsValue(data);
	}

	public void checkRegistered(T data) {
		if (!isRegistered(data)) {
			throw new IllegalStateException(data.getClass().getSimpleName() + " " + data + " is not registered!");
		}
	}

}
