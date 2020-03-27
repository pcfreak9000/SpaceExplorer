package de.pcfreak9000.space.voxelworld.ecs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.joml.Vector2f;

import de.omnikryptec.core.Omnikryptec;
import de.omnikryptec.ecs.Family;
import de.omnikryptec.ecs.IECSManager;
import de.omnikryptec.ecs.component.ComponentMapper;
import de.omnikryptec.ecs.system.AbstractComponentSystem;
import de.omnikryptec.event.EventSubscription;
import de.omnikryptec.render.Camera;
import de.omnikryptec.util.updater.Time;
import de.pcfreak9000.space.core.Keys;
import de.pcfreak9000.space.core.Space;
import de.pcfreak9000.space.voxelworld.Region;
import de.pcfreak9000.space.voxelworld.TileWorld;
import de.pcfreak9000.space.voxelworld.VoxelworldEvents;
import de.pcfreak9000.space.voxelworld.tile.Tile;
import de.pcfreak9000.space.voxelworld.tile.TileType;

public class PlayerInputSystem extends AbstractComponentSystem {
    
    public PlayerInputSystem() {
        super(Family.of(PlayerInputComponent.class));
        Space.BUS.register(this);
    }
    
    private ComponentMapper<PlayerInputComponent> mapper = new ComponentMapper<>(PlayerInputComponent.class);
    private ComponentMapper<PhysicsComponent> physicsMapper = new ComponentMapper<>(PhysicsComponent.class);
    
    private TileWorld world;
    private Camera cam;
    
    @EventSubscription
    public void settwevent(VoxelworldEvents.SetVoxelWorldEvent ev) {
        this.world = ev.tileWorldNew;
        this.cam = ev.groundMgr.getPlanetCamera();//TODO meh...?
    }
    
    private TileType ugly = null;
    
    @Override
    public void update(IECSManager iecsManager, Time time) {
        PlayerInputComponent play = mapper.get(entities.get(0));
        float vy = 0;
        float vx = 0;
        if (physicsMapper.get(entities.get(0)).onGround) {
            if (Keys.FORWARD.isPressed() || Keys.UP.isPressed()) {
                vy += play.maxYv * 50;
            }
            //kinda useless, use for sneaking/ladders instead?
            if (Keys.BACKWARD.isPressed() || Keys.DOWN.isPressed()) {
                vy -= play.maxYv;
            }
        }
        if (Keys.LEFT.isPressed()) {
            vx -= play.maxXv;
        }
        if (Keys.RIGHT.isPressed()) {
            vx += play.maxXv;
        }
        physicsMapper.get(entities.get(0)).acceleration.set(vx * 3, vy * 3 - 98.1f);
        if (Keys.DESTROY.isPressed()) {
            Vector2f mouse = Omnikryptec.getInput().getMousePositionInWorld2D(cam, new Vector2f());
            int tx = Tile.toGlobalTile(mouse.x());
            int ty = Tile.toGlobalTile(mouse.y());
            Region r = world.requestRegion(Region.toGlobalRegion(tx), Region.toGlobalRegion(ty));
            if (r != null) {
                List<Tile> t = new ArrayList<>();
                r.tileIntersections(t, tx, ty, 1, 1);
                if (!t.isEmpty()) {
                    ugly = t.get(0).getType();
                }
                r.removeTile(tx, ty);
                r.recache();
            }
        }
        if (Keys.BUILD.isPressed()) {
            Vector2f mouse = Omnikryptec.getInput().getMousePositionInWorld2D(cam, new Vector2f());
            int tx = Tile.toGlobalTile(mouse.x());
            int ty = Tile.toGlobalTile(mouse.y());
            Region r = world.requestRegion(Region.toGlobalRegion(tx), Region.toGlobalRegion(ty));
            if (r != null && ugly != null) {
                r.setTile(new Tile(ugly, tx, ty));
                r.recache();
            }
        }
    }
    
}
