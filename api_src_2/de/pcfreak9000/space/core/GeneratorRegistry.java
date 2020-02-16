package de.pcfreak9000.space.core;

import java.util.ArrayList;
import java.util.List;

import de.pcfreak9000.space.world.GeneratorTemplate;

public class GeneratorRegistry extends GameRegistry<GeneratorTemplate>{
    
    public List<GeneratorTemplate> filtered(Object ...filter){
        List<GeneratorTemplate> filterOutput = new ArrayList<>();
        for(GeneratorTemplate t : registered.values()) {
            if(t.hasCapabilities(filter)) {
                filterOutput.add(t);
            }
        }
        return filterOutput;
    }
    
}
