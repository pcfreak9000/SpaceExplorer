package de.pcfreak9000.se2d.universe.worlds;

import java.util.HashMap;
import java.util.Map;

import de.pcfreak9000.se2d.universe.biome.Field;

public class Fields {

	public static final String TEMPERATURE = "temperature";
	public static final String HUMIDITY = "humidity";
	public static final String HEIGHT = "height";

	private Map<String, Field> attributes = new HashMap<>();

	public boolean hasAttribute(String name) {
		return attributes.containsKey(name);
	}

	public Field getAttribute(String name) {
		return attributes.get(name);
	}

	public Fields addField(String name, Field field) {
		attributes.putIfAbsent(name, field);
		return this;
	}
}
