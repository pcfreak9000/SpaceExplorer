package de.pcfreak9000.se2d.universe.celestial;

import java.util.HashMap;
import java.util.Map;

public class Catalog {

	private static Map<String, CelestialBodyDefinition> content = new HashMap<>();

	public static void add(CelestialBodyDefinition def) {
		content.put(def.getClass().getName(), def);
	}

	public static CelestialBodyDefinition get(String classname) {
		return content.get(classname);
	}

	public static boolean exists(String classname) {
		return content.containsKey(classname);
	}

	public static void checkRegistered(CelestialBodyDefinition def) {
		if(!exists(def.getClass().getName())) {
			throw new IllegalStateException("CelestialBodyDefinition "+def.getClass().getName()+" is unregistered!");
		}
	}
	
}
