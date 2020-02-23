package de.pcfreak9000.space.voxelworld.ecs;

import org.joml.Vector2f;
import org.joml.Vector2fc;

import de.omnikryptec.ecs.Entity;
import de.omnikryptec.ecs.Family;
import de.omnikryptec.ecs.IECSManager;
import de.omnikryptec.ecs.component.ComponentMapper;
import de.omnikryptec.ecs.system.IterativeComponentSystem;
import de.omnikryptec.util.updater.Time;
import de.pcfreak9000.space.voxelworld.TileWorld;

public class PhysicsSystem extends IterativeComponentSystem {
    
    private ComponentMapper<TransformComponent> transformMapper = new ComponentMapper<>(TransformComponent.class);
    private ComponentMapper<PhysicsComponent> physicsMapper = new ComponentMapper<>(PhysicsComponent.class);
    
    private ComponentMapper<PlayerInputComponent> mapper = new ComponentMapper<>(PlayerInputComponent.class);
    
    private TileWorld tileWorld;
    
    public PhysicsSystem() {
        super(Family.of(PhysicsComponent.class, TransformComponent.class));
    }
    
    @Override
    public void updateIndividual(IECSManager manager, Entity entity, Time time) {
        TransformComponent tc = transformMapper.get(entity);
        PhysicsComponent pc = physicsMapper.get(entity);
        //pc.velocity.add(0, -9.81f * time.deltaf);
        float dx = time.deltaf * (pc.velocity.x() + pc.tmpv.x());
        float dy = time.deltaf * (pc.velocity.y() + pc.tmpv.y());
        tc.transform.localspaceWrite().translate(dx, dy);
        if (entity.hasComponent(mapper.getType())) {
            Vector2fc pos = tc.transform.worldspacePos();
            mapper.get(entity).cam.localspaceWrite().translation(-pos.x(), -pos.y(), 0);
        }
    }
}
