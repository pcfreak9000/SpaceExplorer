package de.pcfreak9000.space.world.ecs;

import de.omnikryptec.ecs.Entity;
import de.omnikryptec.ecs.EntityListener;
import de.omnikryptec.ecs.Family;
import de.omnikryptec.ecs.IECSManager;
import de.omnikryptec.ecs.component.ComponentMapper;
import de.omnikryptec.ecs.system.ComponentSystem;
import de.omnikryptec.render.objects.IRenderedObjectManager;
import de.omnikryptec.util.updater.Time;

public class RenderSystem extends ComponentSystem implements EntityListener {
    
    private ComponentMapper<RenderComponent> renderMapper = new ComponentMapper<>(RenderComponent.class);
    private ComponentMapper<TransformComponent> transformMapper = new ComponentMapper<>(TransformComponent.class);
    
    private IRenderedObjectManager renderedObjects;
    
    public RenderSystem(IRenderedObjectManager objs) {
        super(Family.of(RenderComponent.class));
        this.renderedObjects = objs;
    }
    
    @Override
    public void entityAdded(Entity entity) {
        registerRenderedEntity(entity);
    }
    
    private void registerRenderedEntity(Entity entity) {
        renderedObjects.add(renderMapper.get(entity).sprite);
        //sync the rendering transform to the actual transform
        if (entity.hasComponent(transformMapper.getType())) {
            renderMapper.get(entity).sprite.setTransform(transformMapper.get(entity).transform);
        }
    }
    
    @Override
    public void entityRemoved(Entity entity) {
        renderedObjects.remove(renderMapper.get(entity).sprite);
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
