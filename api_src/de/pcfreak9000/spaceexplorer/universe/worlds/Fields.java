package de.pcfreak9000.spaceexplorer.universe.worlds;

import java.util.HashMap;
import java.util.Map;

import de.pcfreak9000.spaceexplorer.universe.biome.Field;

public class Fields {
    
    public static final String TEMPERATURE = "temperature";
    public static final String HUMIDITY = "humidity";
    public static final String HEIGHT = "height";
    
    private final Map<String, Field> attributes = new HashMap<>();
    
    public boolean hasAttribute(final String name) {
        return this.attributes.containsKey(name);
    }
    
    public Field getAttribute(final String name) {
        return this.attributes.get(name);
    }
    
    public Fields addField(final String name, final Field field) {
        this.attributes.putIfAbsent(name, field);
        return this;
    }
}
