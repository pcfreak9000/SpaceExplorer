package de.pcfreak9000.space.voxelworld.ecs;

import de.omnikryptec.core.Omnikryptec;
import de.omnikryptec.ecs.Entity;
import de.omnikryptec.ecs.EntityListener;
import de.omnikryptec.ecs.Family;
import de.omnikryptec.ecs.IECSManager;
import de.omnikryptec.ecs.component.ComponentMapper;
import de.omnikryptec.ecs.system.AbstractComponentSystem;
import de.omnikryptec.event.EventSubscription;
import de.omnikryptec.render.objects.SimpleSprite;
import de.omnikryptec.render.renderer.AdvancedRenderer2D;
import de.omnikryptec.render.renderer.Renderer2D;
import de.omnikryptec.util.updater.Time;
import de.pcfreak9000.space.core.CoreEvents;
import de.pcfreak9000.space.core.Space;

public class RenderSystem extends AbstractComponentSystem implements EntityListener {
    
    private ComponentMapper<RenderComponent> renderMapper = new ComponentMapper<>(RenderComponent.class);
    private ComponentMapper<TransformComponent> transformMapper = new ComponentMapper<>(TransformComponent.class);
    
    private AdvancedRenderer2D renderer;
    private Renderer2D backgroundRenderer;
    
    public RenderSystem(AdvancedRenderer2D renderer, Renderer2D background) {
        super(Family.of(RenderComponent.class));
        this.renderer = renderer;
        this.backgroundRenderer = background;
        Space.BUS.register(this);
    }
    
    @EventSubscription
    public void ev(CoreEvents.AssignResourcesEvent ev) {
        SimpleSprite light = new SimpleSprite();
        light.setWidth(2500);
        light.setHeight(2500);
        light.setTexture(ev.textures.get("light_2.png"));
        this.renderer.addLight(light);
    }
    
    @Override
    public void entityAdded(Entity entity) {
        registerRenderedEntity(entity);
    }
    
    private void registerRenderedEntity(Entity entity) {
        RenderComponent rc = renderMapper.get(entity);
        if (rc.asBackground) {
            backgroundRenderer.add(rc.sprite);
        } else {
            renderer.add(rc.sprite);
        }
        if (rc.light != null) {
            renderer.addLight(rc.light);
        }
        //sync the rendering transform to the actual transform
        if (entity.hasComponent(transformMapper.getType())) {
            rc.sprite.setTransform(transformMapper.get(entity).transform);
            if (rc.light != null && rc.light instanceof SimpleSprite) {
                ((SimpleSprite) rc.light).getTransform().setParent(transformMapper.get(entity).transform);
            }
        }
    }
    
    @Override
    public void entityRemoved(Entity entity) {
        RenderComponent rc = renderMapper.get(entity);
        if (rc.asBackground) {
            backgroundRenderer.remove(rc.sprite);
        } else {
            renderer.remove(rc.sprite);
        }
        if (rc.light != null) {
            renderer.removeLight(rc.light);
        }
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
