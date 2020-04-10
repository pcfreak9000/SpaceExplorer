package de.pcfreak9000.space.voxelworld.ecs;

import de.omnikryptec.ecs.component.Component;
import de.pcfreak9000.space.voxelworld.Region;

public class TickRegionComponent implements Component {
    
    public final Region region;
    
    public TickRegionComponent(Region r) {
        this.region = r;
    }
}
