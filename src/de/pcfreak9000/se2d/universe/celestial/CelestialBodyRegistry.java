package de.pcfreak9000.se2d.universe.celestial;

import java.util.ArrayList;
import java.util.List;

import de.pcfreak9000.se2d.game.core.GameRegistry;

public class CelestialBodyRegistry extends GameRegistry<CelestialBodyDefinition> {

	private static List<CelestialBodyDefinition> startCapable = new ArrayList<>();

	@Override
	public CelestialBodyRegistry register(String name, CelestialBodyDefinition data) {
		super.register(name, data);
		if (data.isStartCapable()) {
			startCapable.add(data);
		}
		return this;
	}

	public List<CelestialBodyDefinition> getStartCapables() {
		return startCapable;
	}

}
