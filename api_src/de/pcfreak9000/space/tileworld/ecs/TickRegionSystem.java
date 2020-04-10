package de.pcfreak9000.space.tileworld.ecs;

import de.omnikryptec.ecs.Entity;
import de.omnikryptec.ecs.Family;
import de.omnikryptec.ecs.IECSManager;
import de.omnikryptec.ecs.component.ComponentMapper;
import de.omnikryptec.ecs.system.IterativeComponentSystem;
import de.omnikryptec.util.updater.Time;

public class TickRegionSystem extends IterativeComponentSystem {
    
    private ComponentMapper<TickRegionComponent> tMapper = new ComponentMapper<>(TickRegionComponent.class);
    
    public TickRegionSystem() {
        super(Family.of(TickRegionComponent.class));
        
    }

    @Override
    public void updateIndividual(IECSManager manager, Entity entity, Time time) {
        TickRegionComponent c = tMapper.get(entity);
        c.region.tick(time);
    }
    
}
