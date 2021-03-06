package de.pcfreak9000.space.core.registry;

import java.util.ArrayList;
import java.util.List;

import de.pcfreak9000.space.tileworld.WorldGenerator;

public class GeneratorRegistry extends GameRegistry<WorldGenerator> {

    public List<WorldGenerator> filtered(Object... filter) {
        List<WorldGenerator> filterOutput = new ArrayList<>();
        for (WorldGenerator t : this.registered.values()) {
            if (t.hasCapabilities(filter)) {
                filterOutput.add(t);
            }
        }
        return filterOutput;
    }

}
