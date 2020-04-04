package de.pcfreak9000.space.voxelworld.ecs;

import java.util.HashSet;
import java.util.Set;

import org.joml.Vector2f;

import de.omnikryptec.core.Omnikryptec;
import de.omnikryptec.ecs.Family;
import de.omnikryptec.ecs.IECSManager;
import de.omnikryptec.ecs.component.ComponentMapper;
import de.omnikryptec.ecs.system.AbstractComponentSystem;
import de.omnikryptec.event.EventSubscription;
import de.omnikryptec.render.Camera;
import de.omnikryptec.util.math.Mathf;
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
        this.cam = ev.groundMgr.getPlanetCamera().getCameraActual();//TODO meh...?
    }
    
    private TileType ugly = null;
    
    @Override
    public void update(IECSManager iecsManager, Time time) {
        PlayerInputComponent play = mapper.get(entities.get(0));
        float vy = 0;
        float vx = 0;
        //if (physicsMapper.get(entities.get(0)).onGround) {
        if (Keys.FORWARD.isPressed() || Keys.UP.isPressed()) {
            vy += play.maxYv * 1;
        }
        //kinda useless, use for sneaking/ladders instead?
        if (Keys.BACKWARD.isPressed() || Keys.DOWN.isPressed()) {
            vy -= play.maxYv;
        }
        //}
        if (Keys.LEFT.isPressed()) {
            vx -= play.maxXv;
        }
        if (Keys.RIGHT.isPressed()) {
            vx += play.maxXv;
        }
        physicsMapper.get(entities.get(0)).acceleration.set(vx * 3, vy * 3 - 98.1f);
        if (Keys.EXPLODE_DEBUG.isPressed()) {
            Vector2f mouse = Omnikryptec.getInput().getMousePositionInWorld2D(cam, new Vector2f());
            int txm = Tile.toGlobalTile(mouse.x());
            int tym = Tile.toGlobalTile(mouse.y());
            Set<Region> rs = new HashSet<>();
            final int rad = 3;
            for (int i = -rad; i <= rad; i++) {
                for (int j = -rad; j <= rad; j++) {
                    if (Mathf.square(i) + Mathf.square(j) <= Mathf.square(rad)) {
                        int tx = txm + i;
                        int ty = tym + j;
                        Region r = world.requestRegion(Region.toGlobalRegion(tx), Region.toGlobalRegion(ty));
                        if (r != null) {
                            Tile t = r.get(tx, ty);
                            if (t != null && t.getType().canBreak()) {
                                ugly = t.getType();
                                r.removeTile(tx, ty);
                                rs.add(r);
                            }
                        }
                    }
                }
            }
            for (Region r : rs) {
                r.queueRecacheTiles();
            }
        }
        if (Keys.DESTROY.isPressed()) {
            Vector2f mouse = Omnikryptec.getInput().getMousePositionInWorld2D(cam, new Vector2f());
            int tx = Tile.toGlobalTile(mouse.x());
            int ty = Tile.toGlobalTile(mouse.y());
            Region r = world.requestRegion(Region.toGlobalRegion(tx), Region.toGlobalRegion(ty));
            if (r != null) {
                Tile t = r.get(tx, ty);
                if (t != null && t.getType().canBreak()) {
                    ugly = t.getType();
                    r.removeTile(tx, ty);
                    r.queueRecacheTiles();
                }
            }
        }
        if (Keys.BUILD.isPressed()) {
            Vector2f mouse = Omnikryptec.getInput().getMousePositionInWorld2D(cam, new Vector2f());
            int tx = Tile.toGlobalTile(mouse.x());
            int ty = Tile.toGlobalTile(mouse.y());
            Region r = world.requestRegion(Region.toGlobalRegion(tx), Region.toGlobalRegion(ty));
            if (r != null && ugly != null) {
                if (r.get(tx, ty) == null || r.get(tx, ty).getType() == TileType.EMPTY) {
                    r.setTile(new Tile(ugly, tx, ty));
                    r.queueRecacheTiles();
                }
            }
        }
    }
    
}
