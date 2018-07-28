package de.pcfreak9000.se2d.coregame.start;

import de.pcfreak9000.se2d.universe.Orbit;
import de.pcfreak9000.se2d.universe.SpaceCoordinates;
import de.pcfreak9000.se2d.universe.celestial.CelestialBody;
import de.pcfreak9000.se2d.universe.celestial.CelestialBodyDefinition;

public class StartPlanetDef extends CelestialBodyDefinition{

	@Override
	public CelestialBody generate(long seed, SpaceCoordinates sc, CelestialBody parent) {
		Planet planet = new Planet(this, new Orbit(), 1000, "tstst", sc.adjustSeedToPos(seed));
		return planet;
	}

	@Override
	public boolean isStartCapable() {
		return true;
	}
	
}
