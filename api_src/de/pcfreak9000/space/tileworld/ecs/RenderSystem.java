package de.pcfreak9000.space.tileworld.ecs;

import de.omnikryptec.ecs.Entity;
import de.omnikryptec.ecs.EntityListener;
import de.omnikryptec.ecs.Family;
import de.omnikryptec.ecs.IECSManager;
import de.omnikryptec.ecs.component.ComponentMapper;
import de.omnikryptec.ecs.system.AbstractComponentSystem;
import de.omnikryptec.render3.d2.ViewRenderer2D;
import de.omnikryptec.util.updater.Time;

public class RenderSystem extends AbstractComponentSystem implements EntityListener {
    
    private final ComponentMapper<RenderComponent> renderMapper = new ComponentMapper<>(RenderComponent.class);
    private final ComponentMapper<TransformComponent> transformMapper = new ComponentMapper<>(TransformComponent.class);
    
    private final ViewRenderer2D vrenderer;
    
    public RenderSystem(ViewRenderer2D vr) {
        super(Family.of(RenderComponent.class));
        this.vrenderer = vr;
    }
    
    @Override
    public void entityAdded(Entity entity) {
        registerRenderedEntity(entity);
    }
    
    private void registerRenderedEntity(Entity entity) {
        RenderComponent rc = this.renderMapper.get(entity);
        this.vrenderer.addSprite(rc.sprite);
        //        if (rc.light != null) {
        //            this.renderer.addLight(rc.light);
        //        }
        //sync the rendering transform to the actual transform
        if (entity.hasComponent(this.transformMapper.getType())) {
            rc.sprite.setTransform(this.transformMapper.get(entity).transform);
            //            if (rc.light != null && rc.light instanceof SimpleSprite) {
            //                ((SimpleSprite) rc.light).getTransform().setParent(this.transformMapper.get(entity).transform);
            //            }
        }
    }
    
    @Override
    public void entityRemoved(Entity entity) {
        RenderComponent rc = this.renderMapper.get(entity);
        this.vrenderer.removeSprite(rc.sprite);
        //        if (rc.light != null) {
        //            this.renderer.removeLight(rc.light);
        //        }
    }
    
    @Override
    public void update(IECSManager iecsManager, Time time) {
        //RendererContext is updated elsewhere
    }
    
    @Override
    public void addedToIECSManager(IECSManager iecsManager) {
        super.addedToIECSManager(iecsManager);
        //add already registered entities that are not noticed by the EntityListener
        for (Entity e : this.entities) {
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
