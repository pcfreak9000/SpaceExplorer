package de.pcfreak9000.space.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.pcfreak9000.space.world.GeneratorTemplate;

public class GeneratorRegistry extends GameRegistry<GeneratorTemplate>{
    
    public List<GeneratorTemplate> getStarts(){
        List<GeneratorTemplate> starts = new ArrayList<>();
        for(GeneratorTemplate t : registered.values()) {
            if(t.canStart()) {
                starts.add(t);
            }
        }
        return starts;
    }
    
}
