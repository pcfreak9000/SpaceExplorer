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
import de.omnikryptec.resource.TextureConfig;
import de.omnikryptec.resource.TextureConfig.WrappingMode;
import de.omnikryptec.util.math.Mathf;
import de.omnikryptec.util.updater.Time;
import de.pcfreak9000.space.core.CoreEvents;
import de.pcfreak9000.space.core.Space;
import de.pcfreak9000.space.voxelworld.TileWorld;
import de.pcfreak9000.space.voxelworld.VoxelworldEvents;
import de.pcfreak9000.space.voxelworld.tile.Tile;

public class CameraSystem extends AbstractComponentSystem {
    
    private Camera playerCam;
    private TileWorld tileWorld;
    
    private ComponentMapper<TransformComponent> transformMapper = new ComponentMapper<>(TransformComponent.class);
    
    private Entity backgroundEntity = new Entity();
    
    public CameraSystem() {
        super(Family.of(PlayerInputComponent.class, TransformComponent.class));
        Space.BUS.register(this);
        RenderComponent c = new RenderComponent(new AdvancedSprite());
        backgroundEntity.addComponent(c);
        c.sprite.setHeight(1920*2);
        c.sprite.setWidth(3413.33333f*2);
        c.sprite.setLayer(-2);
        c.sprite.setTilingFactor(3);
    }
    
    @EventSubscription
    public void resEvent(CoreEvents.AssignResourcesEvent ev) {
        RenderComponent rc = backgroundEntity.getComponent(ComponentType.of(RenderComponent.class));
        rc.sprite.setTexture(ev.textures.get("Space.png", new TextureConfig().wrappingMode(WrappingMode.Repeat)));
    }
    
    @EventSubscription
    public void tileworldLoadingEvent(VoxelworldEvents.SetVoxelWorldEvent svwe) {
        this.playerCam = svwe.groundMgr.getPlanetCamera();
        this.tileWorld = svwe.tileWorldNew;
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
        float xratio = positionState.x() / (tileWorld.getWorldWidth() * Tile.TILE_SIZE);
        float yratio = positionState.y() / (tileWorld.getWorldHeight() * Tile.TILE_SIZE);
        rc.sprite.getTransform().localspaceWrite().translation(xratio * -2000 + positionState.x() - 3413.33f/2,
                yratio * -2000 + positionState.y() - 1920/2);
        
        //temporary wrap around
        TransformComponent tc = transformMapper.get(entities.get(0));
        if (tc.transform.worldspacePos().x() < 0) {
            tc.transform.localspaceWrite().translate(this.tileWorld.getWorldWidth() * Tile.TILE_SIZE, 0);
        } else if (tc.transform.worldspacePos().x() > this.tileWorld.getWorldWidth() * Tile.TILE_SIZE) {
            tc.transform.localspaceWrite().translate(-this.tileWorld.getWorldWidth() * Tile.TILE_SIZE, 0);
        }
    }
}
