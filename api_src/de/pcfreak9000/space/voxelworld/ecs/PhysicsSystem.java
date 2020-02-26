package de.pcfreak9000.space.voxelworld.ecs;

import java.util.ArrayList;
import java.util.List;

import org.joml.Intersectionf;
import org.joml.Vector2f;
import org.joml.Vector2fc;

import de.omnikryptec.ecs.Entity;
import de.omnikryptec.ecs.Family;
import de.omnikryptec.ecs.IECSManager;
import de.omnikryptec.ecs.component.ComponentMapper;
import de.omnikryptec.ecs.system.IterativeComponentSystem;
import de.omnikryptec.util.math.Mathf;
import de.omnikryptec.util.updater.Time;
import de.pcfreak9000.space.voxelworld.TileWorld;
import de.pcfreak9000.space.voxelworld.tile.Tile;

public class PhysicsSystem extends IterativeComponentSystem {
    
    private ComponentMapper<TransformComponent> transformMapper = new ComponentMapper<>(TransformComponent.class);
    private ComponentMapper<PhysicsComponent> physicsMapper = new ComponentMapper<>(PhysicsComponent.class);
    
    private ComponentMapper<PlayerInputComponent> mapper = new ComponentMapper<>(PlayerInputComponent.class);
    
    private TileWorld tileWorld;
    
    //TODO
    public void setWorld(TileWorld world) {
        this.tileWorld = world;
    }
    
    public PhysicsSystem() {
        super(Family.of(PhysicsComponent.class, TransformComponent.class));
    }
    
    @Override
    public void updateIndividual(IECSManager manager, Entity entity, Time time) {
        TransformComponent tc = transformMapper.get(entity);
        PhysicsComponent pc = physicsMapper.get(entity);
        pc.velocity.add(0, -98.1f * time.deltaf);
        float dx = time.deltaf * (pc.velocity.x() + pc.tmpv.x());
        float dy = time.deltaf * (pc.velocity.y() + pc.tmpv.y());
        tc.transform.localspaceWrite().translate(dx, dy);
        Vector2fc pos = tc.transform.worldspacePos();
        if (!(pc.x == 0 && pc.y == 0 && pc.w == 0 && pc.h == 0)) {
            pc.x = pos.x();
            pc.y = pos.y();
            List<Tile> collisions = new ArrayList<>();
            tileWorld.collectTileIntersections(collisions, (int) Mathf.floor(pc.x / Tile.TILE_SIZE),
                    (int) Mathf.floor(pc.y / Tile.TILE_SIZE), (int) Mathf.ceil(pc.w / Tile.TILE_SIZE),
                    (int) Mathf.ceil(pc.h / Tile.TILE_SIZE));
            do {
                int counter = 0;
                Vector2f res = new Vector2f();
                for (Tile t : collisions) {
                    float wx = t.getGlobalTileX() * Tile.TILE_SIZE;
                    float wy = t.getGlobalTileY() * Tile.TILE_SIZE;
                    float wxw = (t.getGlobalTileX() + 1) * Tile.TILE_SIZE;
                    float wyh = (t.getGlobalTileY() + 1) * Tile.TILE_SIZE;
                    if (Intersectionf.testAabAab(wx, wy, 0, wxw, wyh, 0, pc.x, pc.y, 0, pc.x + pc.w, pc.y + pc.h, 0)) {
                        float xdif = (pc.x + pc.w / 2) - (wx + Tile.TILE_SIZE / 2);
                        float ydif = (pc.y + pc.h / 2) - (wy + Tile.TILE_SIZE / 2);
                        res = res.add(xdif, ydif);
                        counter++;
                    }
                }
                if (counter != 0) {
                    res = res.normalize();
                    //System.out.println(res);
                    //res.normalize(Tile.TILE_SIZE);
                    tc.transform.localspaceWrite().translate(res);
                    pos = tc.transform.worldspacePos();
                    pc.x = pos.x();
                    pc.y = pos.y();
                    pc.velocity.set(0);
                } else {
                    break;
                }
            } while (true);
        }
        if(tc.transform.worldspacePos().y()<-1000) {
            tc.transform.localspaceWrite().translate(0, 2000);
        }
        if (entity.hasComponent(mapper.getType())) {
            mapper.get(entity).cam.localspaceWrite().translation(-pos.x(), -pos.y(), 0);
        }
    }
}
