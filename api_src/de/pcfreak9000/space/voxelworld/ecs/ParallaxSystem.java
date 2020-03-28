package de.pcfreak9000.space.voxelworld.ecs;

import org.joml.Vector3fc;

import de.omnikryptec.ecs.Entity;
import de.omnikryptec.ecs.Family;
import de.omnikryptec.ecs.IECSManager;
import de.omnikryptec.ecs.component.ComponentMapper;
import de.omnikryptec.ecs.system.IterativeComponentSystem;
import de.omnikryptec.event.EventSubscription;
import de.omnikryptec.render.Camera;
import de.omnikryptec.util.updater.Time;
import de.pcfreak9000.space.core.Space;
import de.pcfreak9000.space.voxelworld.TileWorld;
import de.pcfreak9000.space.voxelworld.VoxelworldEvents;
import de.pcfreak9000.space.voxelworld.tile.Tile;

public class ParallaxSystem extends IterativeComponentSystem {
    
    public ParallaxSystem() {
        super(Family.of(ParallaxComponent.class, RenderComponent.class));
        Space.BUS.register(this);
    }
    
    private ComponentMapper<RenderComponent> renderMapper = new ComponentMapper<>(RenderComponent.class);
    private ComponentMapper<ParallaxComponent> parallaxMapper = new ComponentMapper<>(ParallaxComponent.class);
    
    private TileWorld tileWorld;
    private Camera cam;
    
    @EventSubscription
    public void tileworldLoadingEvent(VoxelworldEvents.SetVoxelWorldEvent svwe) {
        this.tileWorld = svwe.tileWorldNew;
        this.cam = svwe.groundMgr.getPlanetCamera();
    }
    
    @Override
    public void updateIndividual(IECSManager manager, Entity entity, Time time) {
        RenderComponent rc = renderMapper.get(entity);
        ParallaxComponent pc = parallaxMapper.get(entity);
        Vector3fc positionState = cam.getTransform().worldspacePos();
        float xratio = positionState.x() / (tileWorld.getWorldWidth() * Tile.TILE_SIZE);
        float yratio = positionState.y() / (tileWorld.getWorldHeight() * Tile.TILE_SIZE);
        rc.sprite.getTransform().localspaceWrite().translation(
                xratio * pc.xMov - positionState.x() - 1920 * pc.aspect / 2,
                yratio * pc.yMov - positionState.y() - 1920 / 2);
    }
    
}
