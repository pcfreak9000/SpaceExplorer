package de.pcfreak9000.space.voxelworld.ecs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joml.Intersectionf;
import org.joml.Vector2f;
import org.joml.Vector2fc;

import de.omnikryptec.ecs.Entity;
import de.omnikryptec.ecs.Family;
import de.omnikryptec.ecs.IECSManager;
import de.omnikryptec.ecs.component.ComponentMapper;
import de.omnikryptec.ecs.system.IterativeComponentSystem;
import de.omnikryptec.event.EventSubscription;
import de.omnikryptec.render.Camera;
import de.omnikryptec.util.math.Mathf;
import de.omnikryptec.util.updater.Time;
import de.pcfreak9000.space.core.Space;
import de.pcfreak9000.space.voxelworld.TileWorld;
import de.pcfreak9000.space.voxelworld.VoxelworldEvents;
import de.pcfreak9000.space.voxelworld.ecs.Physics.Manifold;
import de.pcfreak9000.space.voxelworld.tile.Tile;

public class PhysicsSystem extends IterativeComponentSystem {
    
    private ComponentMapper<TransformComponent> transformMapper = new ComponentMapper<>(TransformComponent.class);
    private ComponentMapper<PhysicsComponent> physicsMapper = new ComponentMapper<>(PhysicsComponent.class);
    
    private ComponentMapper<PlayerInputComponent> mapper = new ComponentMapper<>(PlayerInputComponent.class);
    
    private TileWorld tileWorld;
    private Camera playerCam;
    
    @EventSubscription
    public void tileworldLoadingEvent(VoxelworldEvents.SetVoxelWorldEvent svwe) {
        this.tileWorld = svwe.tileWorldNew;
        this.playerCam = svwe.groundMgr.getPlanetCamera();
    }
    
    public PhysicsSystem() {
        super(Family.of(PhysicsComponent.class, TransformComponent.class));
        Space.BUS.register(this);
    }
    
    @Override
    public void updateIndividual(IECSManager manager, Entity entity, Time time) {
        TransformComponent tc = transformMapper.get(entity);
        PhysicsComponent pc = physicsMapper.get(entity);
        Vector2fc positionState = tc.transform.worldspacePos();
        //Friction
        pc.acceleration.sub(pc.velocity.x() * 1.5f, pc.velocity.y() * 1.5f, pc.acceleration);
        //Integrate motion
        float posNewX = 0.5f * pc.acceleration.x() * Mathf.square(time.deltaf) + pc.velocity.x() * time.deltaf
                + positionState.x();
        float posNewY = 0.5f * pc.acceleration.y() * Mathf.square(time.deltaf) + pc.velocity.y() * time.deltaf
                + positionState.y();
        pc.velocity.add(pc.acceleration.x() * time.deltaf, pc.acceleration.y() * time.deltaf, pc.velocity);
        //Check and resolve collisions
        boolean valid = true;
        if (!(pc.x == 0 && pc.y == 0 && pc.w == 0 && pc.h == 0)) {
            pc.x = posNewX;
            pc.y = posNewY;
            List<Tile> collisions = new ArrayList<>();
            tileWorld.collectTileIntersections(collisions, (int) Mathf.floor(pc.x / Tile.TILE_SIZE),
                    (int) Mathf.floor(pc.y / Tile.TILE_SIZE), (int) Mathf.ceil(pc.w / Tile.TILE_SIZE),
                    (int) Mathf.ceil(pc.h / Tile.TILE_SIZE));
            
            int counter = 0;
            Iterator<Tile> it = collisions.iterator();
            while (it.hasNext()) {
                Tile t = it.next();
                float wx = t.getGlobalTileX() * Tile.TILE_SIZE;
                float wy = t.getGlobalTileY() * Tile.TILE_SIZE;
                float wxw = (t.getGlobalTileX() + 1) * Tile.TILE_SIZE;
                float wyh = (t.getGlobalTileY() + 1) * Tile.TILE_SIZE;
                if (!Intersectionf.testAabAab(wx, wy, 0, wxw, wyh, 0, pc.x, pc.y, 0, pc.x + pc.w, pc.y + pc.h, 0)) {
                    it.remove();
                } else {
                    counter++;
                }
            }
            if (counter != 0) {
                valid = false;
                pc.velocity.sub(new Vector2f(0, 1).mul(2 * pc.velocity.dot(new Vector2f(0, 1))), pc.velocity);
            }
        }
        if (valid) {
            tc.transform.localspaceWrite().setTranslation(posNewX, posNewY);
        }
        if (tc.transform.worldspacePos().y() < -1000) {
            tc.transform.localspaceWrite().translate(0, 2000);
        }
        positionState = tc.transform.worldspacePos();
        if (entity.hasComponent(mapper.getType())) {
            playerCam.getTransform().localspaceWrite().translation(-positionState.x(), -positionState.y(), 0);//TODO not the best place for the cam...
        }
    }
    
    //    @Override
    //    public void updateIndividual(IECSManager manager, Entity entity, Time time) {
    //        TransformComponent tc = transformMapper.get(entity);
    //        PhysicsComponent pc = physicsMapper.get(entity);
    //        pc.velocity.add(0, -98.1f * time.deltaf);
    //        pc.velocity.add(pc.forces.x * time.deltaf, pc.forces.y * time.deltaf);
    //        pc.forces.set(0);
    //        float dx = time.deltaf * (pc.velocity.x());
    //        float dy = time.deltaf * (pc.velocity.y());
    //        boolean valid = true;
    //        Vector2fc pos = tc.transform.worldspacePos();
    //        if (!(pc.x == 0 && pc.y == 0 && pc.w == 0 && pc.h == 0)) {
    //            pc.x = pos.x()+dx;
    //            pc.y = pos.y()+dy;
    //            List<Tile> collisions = new ArrayList<>();
    //            tileWorld.collectTileIntersections(collisions, (int) Mathf.floor(pc.x / Tile.TILE_SIZE),
    //                    (int) Mathf.floor(pc.y / Tile.TILE_SIZE), (int) Mathf.ceil(pc.w / Tile.TILE_SIZE),
    //                    (int) Mathf.ceil(pc.h / Tile.TILE_SIZE));
    //            
    //            int counter = 0;
    //            Iterator<Tile> it = collisions.iterator();
    //            while (it.hasNext()) {
    //                Tile t = it.next();
    //                float wx = t.getGlobalTileX() * Tile.TILE_SIZE;
    //                float wy = t.getGlobalTileY() * Tile.TILE_SIZE;
    //                float wxw = (t.getGlobalTileX() + 1) * Tile.TILE_SIZE;
    //                float wyh = (t.getGlobalTileY() + 1) * Tile.TILE_SIZE;
    //                if (!Intersectionf.testAabAab(wx, wy, 0, wxw, wyh, 0, pc.x, pc.y, 0, pc.x + pc.w, pc.y + pc.h, 0)) {
    //                    it.remove();
    //                } else {
    //                    counter++;
    //                }
    //            }
    //            if (counter != 0) {
    //                valid = false;
    //                pc.velocity.set(0);
    ////                for (Tile t : collisions) {
    ////                    pos = tc.transform.worldspacePos();
    ////                    pc.x = pos.x();
    ////                    pc.y = pos.y();
    ////                    Manifold m = new Manifold();
    ////                    m.awidth = Tile.TILE_SIZE;
    ////                    m.aheight = Tile.TILE_SIZE;
    ////                    m.bwidth = pc.w;
    ////                    m.bheight = pc.h;
    ////                    m.apos = new Vector2f(t.getGlobalTileX() * Tile.TILE_SIZE, t.getGlobalTileY() * Tile.TILE_SIZE);
    ////                    m.bpos = new Vector2f(pc.x, pc.y);
    ////                    m.avel = new Vector2f(0);
    ////                    m.bvel = pc.velocity;
    ////                    m.ainvMass = 0;
    ////                    m.binvMass = 1;
    ////                    if (Physics.AABBvsAABB(m)) {
    ////                        Physics.ResolveCollision(m);
    ////                        Physics.PositionalCorrection(m);
    ////                        tc.transform.localspaceWrite().setTranslation(m.bpos);
    ////                    }
    ////                }
    //            }
    //        }
    //        if(valid) {
    //            tc.transform.localspaceWrite().translate(dx, dy);
    //        }
    //        if (tc.transform.worldspacePos().y() < -1000) {
    //            tc.transform.localspaceWrite().translate(0, 2000);
    //        }
    //        if (entity.hasComponent(mapper.getType())) {
    //            playerCam.getTransform().localspaceWrite().translation(-pos.x(), -pos.y(), 0);//TODO not the best place for the cam...
    //        }
    //    }
}
