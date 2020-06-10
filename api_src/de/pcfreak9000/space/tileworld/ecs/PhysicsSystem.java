package de.pcfreak9000.space.tileworld.ecs;

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
import de.omnikryptec.event.EventSubscription;
import de.omnikryptec.util.math.Mathf;
import de.omnikryptec.util.updater.Time;
import de.pcfreak9000.space.core.Space;
import de.pcfreak9000.space.tileworld.TileWorld;
import de.pcfreak9000.space.tileworld.VoxelworldEvents;
import de.pcfreak9000.space.tileworld.tile.Tile;
import de.pcfreak9000.space.tileworld.tile.TileState;

public class PhysicsSystem extends IterativeComponentSystem {
    
    private final ComponentMapper<TransformComponent> transformMapper = new ComponentMapper<>(TransformComponent.class);
    private final ComponentMapper<PhysicsComponent> physicsMapper = new ComponentMapper<>(PhysicsComponent.class);
    
    private TileWorld tileWorld;
    
    @EventSubscription
    public void tileworldLoadingEvent(VoxelworldEvents.SetVoxelWorldEvent svwe) {
        this.tileWorld = svwe.tileWorldNew;
    }
    
    public PhysicsSystem() {
        super(Family.of(PhysicsComponent.class, TransformComponent.class));
        Space.BUS.register(this);
    }
    
    @Override
    public void updateIndividual(IECSManager manager, Entity entity, Time time) {
        TransformComponent tc = this.transformMapper.get(entity);
        PhysicsComponent pc = this.physicsMapper.get(entity);
        Vector2fc positionState = tc.transform.worldspacePos();
        
        //Friction TODO manage elsewhere
        pc.acceleration.sub(pc.velocity.x() * 1.5f, pc.velocity.y() * 1.5f, pc.acceleration);
        
        //Integrate motion
        float posDeltaX = 0.5f * pc.acceleration.x() * Mathf.square(time.deltaf) + pc.velocity.x() * time.deltaf;
        float posDeltaY = 0.5f * pc.acceleration.y() * Mathf.square(time.deltaf) + pc.velocity.y() * time.deltaf;
        pc.velocity.add(pc.acceleration.x() * time.deltaf, pc.acceleration.y() * time.deltaf, pc.velocity);
        
        //Check and resolve collisions
        if (!(pc.w == 0 && pc.h == 0)) {
            pc.onGround = false;
            float tRemaining = 1.0f;
            TileState tile = null;
            for (int i = 0; i < 4 && tRemaining > 0.0f; i++) {
                float tMin = 1.0f;
                positionState = tc.transform.worldspacePos();
                pc.x = positionState.x();//TODO implement offset?
                pc.y = positionState.y();
                List<TileState> collisions = new ArrayList<>();
                this.tileWorld.collectTileIntersections(collisions, -1 + (int) Mathf.floor(pc.x / Tile.TILE_SIZE),
                        -1 + (int) Mathf.floor(pc.y / Tile.TILE_SIZE),
                        1 + (int) Mathf.ceil((pc.w + posDeltaX) / Tile.TILE_SIZE),
                        1 + (int) Mathf.ceil((pc.h + posDeltaY) / Tile.TILE_SIZE));
                for (TileState t : collisions) {
                    if (!t.getTile().isSolid()) {
                        continue;
                    }
                    Vector2f result = new Vector2f();
                    if (Intersectionf.intersectRayAab(pc.x + pc.w / 2, pc.y + pc.h / 2, 0, posDeltaX, posDeltaY, 0,
                            t.getGlobalTileX() * Tile.TILE_SIZE - pc.w / 2,
                            t.getGlobalTileY() * Tile.TILE_SIZE - pc.h / 2, 0,
                            (1 + t.getGlobalTileX()) * Tile.TILE_SIZE + pc.w / 2,
                            (1 + t.getGlobalTileY()) * Tile.TILE_SIZE + pc.h / 2, 0, result)) {
                        if (result.x() >= 0) {
                            if (result.x() < 1.0f) {
                                pc.onGround = getNormal(t, pc).equals(new Vector2f(0, 1));
                            }
                            if (result.x() < tMin) {
                                tMin = result.x();
                                tile = t;
                            }
                        }
                    }
                }
                if (tMin < 1) {
                    tMin -= 0.1f;//epsilon, big oof
                }
                tc.transform.localspaceWrite().setTranslation(positionState.x() + posDeltaX * tMin,
                        positionState.y() + posDeltaY * tMin);
                if (tMin < 1) {
                    float bouncynessFactor = 1 + tile.getTile().getBouncyness();
                    pc.velocity.sub(getNormal(tile, pc).mul(bouncynessFactor * pc.velocity.dot(getNormal(tile, pc))),
                            pc.velocity);
                    Vector2f hehe = new Vector2f(posDeltaX, posDeltaY);
                    hehe.sub(getNormal(tile, pc).mul(bouncynessFactor * hehe.dot(getNormal(tile, pc))), hehe);
                    posDeltaX = hehe.x;
                    posDeltaY = hehe.y;
                }
                tRemaining -= tMin * tRemaining;
            }
        } else {
            tc.transform.localspaceWrite().setTranslation(positionState.x() + posDeltaX, positionState.y() + posDeltaY);
        }
    }
    
    private Vector2f getNormal(TileState t, PhysicsComponent pc) {
        if ((1 + t.getGlobalTileY()) * Tile.TILE_SIZE < pc.y + pc.h * 0.01f) {
            return new Vector2f(0, 1);
        } else if ((t.getGlobalTileY()) * Tile.TILE_SIZE > pc.y + pc.h * 0.99f) {
            return new Vector2f(0, -1);
        }
        if ((1 + t.getGlobalTileX()) * Tile.TILE_SIZE < pc.x + pc.w * 0.01f) {
            return new Vector2f(-1, 0);
        } else if ((t.getGlobalTileX()) * Tile.TILE_SIZE > pc.x + pc.w * 0.99f) {
            return new Vector2f(1, 0);
        }
        throw new IllegalArgumentException();
    }
    
}
