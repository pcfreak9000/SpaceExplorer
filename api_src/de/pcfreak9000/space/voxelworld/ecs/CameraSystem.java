package de.pcfreak9000.space.voxelworld.ecs;

import org.joml.Vector2fc;

import de.omnikryptec.ecs.Family;
import de.omnikryptec.ecs.IECSManager;
import de.omnikryptec.ecs.component.ComponentMapper;
import de.omnikryptec.ecs.system.AbstractComponentSystem;
import de.omnikryptec.event.EventSubscription;
import de.omnikryptec.render.Camera;
import de.omnikryptec.util.updater.Time;
import de.pcfreak9000.space.core.Space;
import de.pcfreak9000.space.voxelworld.TileWorld;
import de.pcfreak9000.space.voxelworld.VoxelworldEvents;
import de.pcfreak9000.space.voxelworld.tile.Tile;

public class CameraSystem extends AbstractComponentSystem {
    
    private Camera playerCam;
    private TileWorld tileWorld;
    
    private ComponentMapper<TransformComponent> transformMapper = new ComponentMapper<>(TransformComponent.class);
    
    public CameraSystem() {
        super(Family.of(PlayerInputComponent.class, TransformComponent.class));
        Space.BUS.register(this);
    }
    
    @EventSubscription
    public void tileworldLoadingEvent(VoxelworldEvents.SetVoxelWorldEvent svwe) {
        this.playerCam = svwe.groundMgr.getPlanetCamera();
        this.tileWorld = svwe.tileWorldNew;
    }
    
    @Override
    public void update(IECSManager iecsManager, Time time) {
        Vector2fc positionState = transformMapper.get(entities.get(0)).transform.worldspacePos();
        playerCam.getTransform().localspaceWrite().translation(-positionState.x(), -positionState.y(), 0);
        
        //temporary wrap around
        TransformComponent tc = transformMapper.get(entities.get(0));
        if (tc.transform.worldspacePos().x() < 0) {
            tc.transform.localspaceWrite().translate(this.tileWorld.getWorldWidth() * Tile.TILE_SIZE, 0);
        } else if (tc.transform.worldspacePos().x() > this.tileWorld.getWorldWidth() * Tile.TILE_SIZE) {
            tc.transform.localspaceWrite().translate(-this.tileWorld.getWorldWidth() * Tile.TILE_SIZE, 0);
        }
    }
}
