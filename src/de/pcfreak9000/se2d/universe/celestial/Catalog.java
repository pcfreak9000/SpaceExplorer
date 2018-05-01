package de.pcfreak9000.se2d.universe.celestial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.pcfreak9000.se2d.universe.biome.DefaultBiome;

public class Catalog {
	
	public static final DefaultBiome DEFAULT_BIOME = new DefaultBiome();
	
	private static Map<String, CelestialBodyDefinition> content = new HashMap<>();
	private static List<CelestialBodyDefinition> startCapable = new ArrayList<>();
	
	public static void add(CelestialBodyDefinition def) {
		content.put(def.getClass().getName(), def);
		if(def.isStartCapable()) {
			startCapable.add(def);
		}
	}

	public static CelestialBodyDefinition get(String classname) {
		return content.get(classname);
	}

	public static boolean exists(String classname) {
		return content.containsKey(classname);
	}

	public static List<CelestialBodyDefinition> getStartCapables(){
		return startCapable;
	}
	
	public static void checkRegistered(CelestialBodyDefinition def) {
		if(!exists(def.getClass().getName())) {
			throw new IllegalStateException("CelestialBodyDefinition "+def.getClass().getName()+" is unregistered!");
		}
	}
	
}
