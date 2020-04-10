package de.pcfreak9000.space.tileworld.ecs;

import org.joml.Vector2fc;

import de.omnikryptec.ecs.Family;
import de.omnikryptec.ecs.IECSManager;
import de.omnikryptec.ecs.component.ComponentMapper;
import de.omnikryptec.ecs.system.AbstractComponentSystem;
import de.omnikryptec.event.EventSubscription;
import de.omnikryptec.util.math.Mathf;
import de.omnikryptec.util.updater.Time;
import de.pcfreak9000.space.core.Space;
import de.pcfreak9000.space.tileworld.PlanetCamera;
import de.pcfreak9000.space.tileworld.TileWorld;
import de.pcfreak9000.space.tileworld.VoxelworldEvents;
import de.pcfreak9000.space.tileworld.tile.Tile;

public class CameraSystem extends AbstractComponentSystem {

    private PlanetCamera playerCam;
    private TileWorld tileWorld;

    private final ComponentMapper<TransformComponent> transformMapper = new ComponentMapper<>(TransformComponent.class);

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
        Vector2fc positionState = this.transformMapper.get(this.entities.get(0)).transform.worldspacePos();
        float x = positionState.x() - this.playerCam.getWidth() / 2f;
        float y = positionState.y() - this.playerCam.getHeight() / 2f;
        x = Mathf.max(0, x);
        y = Mathf.max(0, y);
        x = Mathf.min(this.tileWorld.getWorldWidth() * Tile.TILE_SIZE - this.playerCam.getWidth(), x);
        y = Mathf.min(this.tileWorld.getWorldHeight() * Tile.TILE_SIZE - this.playerCam.getHeight(), y);
        this.playerCam.getCameraActual().getTransform().localspaceWrite().translation(-x, -y, 0);

        //temporary wrap around
        TransformComponent tc = this.transformMapper.get(this.entities.get(0));
        if (tc.transform.worldspacePos().x() < 0) {
            tc.transform.localspaceWrite().translate(this.tileWorld.getWorldWidth() * Tile.TILE_SIZE, 0);
        } else if (tc.transform.worldspacePos().x() > this.tileWorld.getWorldWidth() * Tile.TILE_SIZE) {
            tc.transform.localspaceWrite().translate(-this.tileWorld.getWorldWidth() * Tile.TILE_SIZE, 0);
        }
    }
}
