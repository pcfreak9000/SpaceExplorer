package de.pcfreak9000.spaceexplorer.coregame.start;

import java.util.Random;

import de.pcfreak9000.spaceexplorer.universe.Orbit;
import de.pcfreak9000.spaceexplorer.universe.SpaceCoordinates;
import de.pcfreak9000.spaceexplorer.universe.biome.Field;
import de.pcfreak9000.spaceexplorer.universe.celestial.CelestialBody;
import de.pcfreak9000.spaceexplorer.universe.celestial.CelestialBodyDefinition;
import de.pcfreak9000.spaceexplorer.universe.worlds.Fields;

public class StartPlanetDef extends CelestialBodyDefinition {
    
    @Override
    public CelestialBody generate(final long seed, final SpaceCoordinates sc, final CelestialBody parent) {
        final Random random = new Random(seed);
        final Planet planet = new Planet(this, new Orbit(), random.nextInt(100) + 50, "Lord Kek",
                sc.adjustSeedToPos(seed));
        planet.putAttribute(Fields.class, new Fields());
        planet.getAttributeCK(Fields.class).addField(Fields.TEMPERATURE, new Field(-100));
        return planet;
    }
    
    @Override
    public boolean isStartCapable() {
        return true;
    }
    
}
