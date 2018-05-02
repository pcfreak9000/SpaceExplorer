package de.pcfreak9000.se2d.game.core;

import java.util.HashMap;

import de.pcfreak9000.se2d.universe.biome.BiomeDefinition;
import de.pcfreak9000.se2d.universe.biome.DefaultBiome;
import de.pcfreak9000.se2d.universe.celestial.CelestialBodyRegistry;
import de.pcfreak9000.se2d.universe.objects.WorldObjectDefinition;
import de.pcfreak9000.se2d.universe.tiles.TileDefinition;
import de.pcfreak9000.se2d.util.Se2Dlog;

public class GameRegistry<T> {

	private static final GameRegistry<TileDefinition> tileRegistry;
	private static final GameRegistry<BiomeDefinition> biomeRegistry;
	private static final CelestialBodyRegistry celestialBodyRegistry;
	private static final GameRegistry<WorldObjectDefinition> worldObjectRegistry;
	
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
	
	public static GameRegistry<WorldObjectDefinition> getWorldObjectRegistry() {
		return worldObjectRegistry;
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
			throw new IllegalStateException(data.getClass().getSimpleName() + " " + data + " is unregistered!");
		}
	}


}