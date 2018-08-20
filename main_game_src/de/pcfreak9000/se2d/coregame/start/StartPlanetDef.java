package de.pcfreak9000.se2d.coregame.start;

import java.util.Random;

import de.pcfreak9000.se2d.universe.Orbit;
import de.pcfreak9000.se2d.universe.SpaceCoordinates;
import de.pcfreak9000.se2d.universe.biome.Field;
import de.pcfreak9000.se2d.universe.celestial.CelestialBody;
import de.pcfreak9000.se2d.universe.celestial.CelestialBodyDefinition;
import de.pcfreak9000.se2d.universe.worlds.Fields;

public class StartPlanetDef extends CelestialBodyDefinition {

	@Override
	public CelestialBody generate(long seed, SpaceCoordinates sc, CelestialBody parent) {
		Random random = new Random(seed);
		Planet planet = new Planet(this, new Orbit(), random.nextInt(100) + 50, "Lord Kek", sc.adjustSeedToPos(seed));
		planet.putAttribute(Fields.class, new Fields());
		planet.getAttribute(Fields.class).addField(Fields.TEMPERATURE, new Field(-100));
		return planet;
	}

	@Override
	public boolean isStartCapable() {
		return true;
	}

}
