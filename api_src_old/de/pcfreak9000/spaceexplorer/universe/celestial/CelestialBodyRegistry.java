package de.pcfreak9000.spaceexplorer.universe.celestial;

import java.util.ArrayList;
import java.util.List;

import de.pcfreak9000.space.core.registry.GameRegistry;
import de.pcfreak9000.spaceexplorer.util.Private;

/**
 * Registry of {@link CelestialBody}s. An instance can be retrieved from
 * {@link GameRegistry#getCelestialBodyRegistry()}
 *
 * @author pcfreak9000
 *
 */
@Private
public class CelestialBodyRegistry extends GameRegistry<CelestialBodyDefinition> {

    private static List<CelestialBodyDefinition> startCapable = new ArrayList<>();

    @Override
    public CelestialBodyRegistry register(final String name, final CelestialBodyDefinition data) {
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
