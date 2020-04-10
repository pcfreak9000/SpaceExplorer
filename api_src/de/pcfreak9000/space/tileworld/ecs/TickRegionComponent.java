package de.pcfreak9000.space.tileworld.ecs;

import de.omnikryptec.ecs.component.Component;
import de.pcfreak9000.space.tileworld.Region;

public class TickRegionComponent implements Component {
    
    public final Region region;
    
    public TickRegionComponent(Region r) {
        this.region = r;
    }
}
