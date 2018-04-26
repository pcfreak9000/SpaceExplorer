package de.pcfreak9000.se2d.universe.celestial;

import de.pcfreak9000.se2d.universe.Orbit;

public interface CelestialBody {
	
	Generator<?> getGenerator();
	String getName();
	Orbit getOrbit();
	
}
