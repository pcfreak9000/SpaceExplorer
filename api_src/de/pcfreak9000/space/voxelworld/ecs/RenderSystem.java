package de.pcfreak9000.space.voxelworld.ecs;

import de.omnikryptec.ecs.Entity;
import de.omnikryptec.ecs.EntityListener;
import de.omnikryptec.ecs.Family;
import de.omnikryptec.ecs.IECSManager;
import de.omnikryptec.ecs.component.ComponentMapper;
import de.omnikryptec.ecs.system.AbstractComponentSystem;
import de.omnikryptec.render.renderer.AdvancedRenderer2D;
import de.omnikryptec.util.updater.Time;

public class RenderSystem extends AbstractComponentSystem implements EntityListener {
    
    private ComponentMapper<RenderComponent> renderMapper = new ComponentMapper<>(RenderComponent.class);
    private ComponentMapper<TransformComponent> transformMapper = new ComponentMapper<>(TransformComponent.class);
    
    private AdvancedRenderer2D renderer;
    
    public RenderSystem(AdvancedRenderer2D renderer) {
        super(Family.of(RenderComponent.class));
        this.renderer = renderer;
    }
    
    @Override
    public void entityAdded(Entity entity) {
        registerRenderedEntity(entity);
    }
    
    private void registerRenderedEntity(Entity entity) {
        renderer.add(renderMapper.get(entity).sprite);
        //sync the rendering transform to the actual transform
        if (entity.hasComponent(transformMapper.getType())) {
            renderMapper.get(entity).sprite.setTransform(transformMapper.get(entity).transform);
        }
    }
    
    @Override
    public void entityRemoved(Entity entity) {
        renderer.remove(renderMapper.get(entity).sprite);
    }
    
    @Override
    public void update(IECSManager iecsManager, Time time) {
        //RendererContext is updated elsewhere
    }
    
    @Override
    public void addedToIECSManager(IECSManager iecsManager) {
        super.addedToIECSManager(iecsManager);
        //add already registered entities that are not noticed by the EntityListener
        for (Entity e : entities) {
            registerRenderedEntity(e);
        }
        iecsManager.addEntityListener(getFamily(), this);
    }
    
    @Override
    public void removedFromIECSManager(IECSManager iecsManager) {
        super.removedFromIECSManager(iecsManager);
        iecsManager.removeEntityListener(getFamily(), this);
    }
}
