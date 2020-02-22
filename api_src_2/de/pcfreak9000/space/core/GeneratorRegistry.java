package de.pcfreak9000.space.core;

import java.util.ArrayList;
import java.util.List;

import de.pcfreak9000.space.voxelworld.TileWorldGenerator;

public class GeneratorRegistry extends GameRegistry<TileWorldGenerator>{
    
    public List<TileWorldGenerator> filtered(Object ...filter){
        List<TileWorldGenerator> filterOutput = new ArrayList<>();
        for(TileWorldGenerator t : registered.values()) {
            if(t.hasCapabilities(filter)) {
                filterOutput.add(t);
            }
        }
        return filterOutput;
    }
    
}
