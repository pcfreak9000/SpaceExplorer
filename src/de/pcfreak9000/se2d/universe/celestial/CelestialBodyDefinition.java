package de.pcfreak9000.se2d.universe.celestial;

import java.util.HashSet;
import java.util.Set;

import de.pcfreak9000.se2d.universe.SpaceCoordinates;
import de.pcfreak9000.se2d.universe.biome.BiomeDefinition;

public abstract class CelestialBodyDefinition {
	
	private Set<BiomeDefinition> definitions = new HashSet<>();
	
	public CelestialBodyDefinition() {
		addBiomeDefinition(CelestialBodyRegistry.DEFAULT_BIOME);
	}
	
	/**
	 * either the position or the parent can be null!
	 * @param seed
	 * @param sc
	 * @param parent
	 * @return
	 */
	public abstract CelestialBody generate(long seed, SpaceCoordinates sc, CelestialBody parent);

	public Set<BiomeDefinition> getBiomeDefinitions(){
		return definitions;
	}

	public void addBiomeDefinition(BiomeDefinition bd) {
		definitions.add(bd);
	}
	
	public boolean isStartCapable() {
		return false;
	}
	
	@Override
	public String toString() {
		return "CBD "+this.getClass().getSimpleName()+" isSC="+isStartCapable();
	}
}
