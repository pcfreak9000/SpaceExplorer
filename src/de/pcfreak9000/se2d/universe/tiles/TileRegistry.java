package de.pcfreak9000.se2d.universe.tiles;

import java.util.HashMap;
import java.util.Map;

public class TileRegistry {
	
	public static final TileDefinition MISSING_DEFINITION;
	
	static {
		registered = new HashMap<>();
		MISSING_DEFINITION = new TileDefinition("");
		register("missing_tile", MISSING_DEFINITION);
	}
	
	private static Map<String, TileDefinition> registered;
	
	public static void register(String name, TileDefinition def) {
		registered.put(name, def);
	}
	
	public static TileDefinition get(String id) {
		return registered.get(id);
	}
	
	public static boolean isRegistered(String id) {
		return registered.containsKey(id);
	}
	
	public static boolean isRegistered(TileDefinition def) {
		return registered.containsValue(def);
	}
	
	public static void checkRegistered(TileDefinition def) {
		if(!isRegistered(def)) {
			throw new IllegalStateException("TileDefinition "+def+" is unregistered!");
		}
	}
	
}
