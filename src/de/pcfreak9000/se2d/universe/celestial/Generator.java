package de.pcfreak9000.se2d.universe.celestial;

import de.pcfreak9000.se2d.universe.SpaceCoordinates;

public interface Generator {

	CelestialBody generate(long seed, SpaceCoordinates sc);

}
