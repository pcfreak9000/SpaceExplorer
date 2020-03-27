package de.pcfreak9000.space.voxelworld.ecs;

import org.joml.Vector2fc;

import de.omnikryptec.ecs.Entity;
import de.omnikryptec.ecs.Family;
import de.omnikryptec.ecs.IECSManager;
import de.omnikryptec.ecs.component.ComponentMapper;
import de.omnikryptec.ecs.component.ComponentType;
import de.omnikryptec.ecs.system.AbstractComponentSystem;
import de.omnikryptec.event.EventSubscription;
import de.omnikryptec.render.Camera;
import de.omnikryptec.render.objects.AdvancedSprite;
import de.omnikryptec.util.updater.Time;
import de.pcfreak9000.space.core.CoreEvents;
import de.pcfreak9000.space.core.Space;
import de.pcfreak9000.space.voxelworld.VoxelworldEvents;

public class CameraSystem extends AbstractComponentSystem {
    
    private Camera playerCam;
    
    private ComponentMapper<TransformComponent> transformMapper = new ComponentMapper<>(TransformComponent.class);
    
    private Entity backgroundEntity = new Entity();
    
    public CameraSystem() {
        super(Family.of(PlayerInputComponent.class, TransformComponent.class));
        Space.BUS.register(this);
        RenderComponent c = new RenderComponent(new AdvancedSprite());
        backgroundEntity.addComponent(c);
        c.sprite.setHeight(1920);
        c.sprite.setWidth(3413.33333f);
        c.sprite.setLayer(-2);
    }
    
    @EventSubscription
    public void resEvent(CoreEvents.AssignResourcesEvent ev) {
        RenderComponent rc = backgroundEntity.getComponent(ComponentType.of(RenderComponent.class));
        rc.sprite.setTexture(ev.textures.get("Space.png"));
    }
    
    @EventSubscription
    public void tileworldLoadingEvent(VoxelworldEvents.SetVoxelWorldEvent svwe) {
        this.playerCam = svwe.groundMgr.getPlanetCamera();
        if (svwe.tileWorldNew != null) {
            svwe.groundMgr.getECSManager().addEntity(backgroundEntity);
        } else {
            svwe.groundMgr.getECSManager().removeEntity(backgroundEntity);
        }
    }
    
    @Override
    public void update(IECSManager iecsManager, Time time) {
        Vector2fc positionState = transformMapper.get(entities.get(0)).transform.worldspacePos();
        playerCam.getTransform().localspaceWrite().translation(-positionState.x(), -positionState.y(), 0);
        RenderComponent rc = backgroundEntity.getComponent(ComponentType.of(RenderComponent.class));
        rc.sprite.getTransform().localspaceWrite().translation(positionState.x()-1920/2, positionState.y()-1920/2);
    }
}
