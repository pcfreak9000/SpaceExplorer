package de.pcfreak9000.space.world;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix3x2f;

import de.omnikryptec.ecs.Entity;
import de.omnikryptec.ecs.IECSManager;
import de.omnikryptec.render.batch.AdvancedBatch2D;
import de.omnikryptec.render.batch.vertexmanager.OrderedCachedVertexManager;
import de.omnikryptec.util.math.Mathd;
import de.pcfreak9000.space.world.ecs.RenderComponent;
import de.pcfreak9000.space.world.tile.Tile;

public class Chunk {
    public static final int CHUNK_TILE_SIZE = 32;
    
    public static int toGlobalChunk(int globalTile) {
        return (int) Mathd.floor(globalTile / (double) CHUNK_TILE_SIZE);
    }
    
    private final int chunkX;
    private final int chunkY;
    
    private Entity entity;
    
    private Tile[][] tiles;
    private List<Entity> statics;
    private List<Entity> dynamics;
    
    //Statics (pre-Combine?), Dynamics (moving objects; managed by WorldUpdater or an ECSSystem?)
    
    Chunk(int cx, int cy) {
        this.chunkX = cx;
        this.chunkY = cy;
        this.tiles = new Tile[CHUNK_TILE_SIZE][CHUNK_TILE_SIZE];
        this.statics = new ArrayList<>();
        this.dynamics = new ArrayList<>();
    }
    
    public int getChunkX() {
        return chunkX;
    }
    
    public int getChunkY() {
        return chunkY;
    }
    
    public void addStatic(Entity e) {
        this.statics.add(e);
    }
    
    public void removeStatic(Entity e) {
        this.statics.remove(e);
    }
    
    public void addDynamic(Entity e) {
        this.dynamics.add(e);
    }
    
    public void removeDynamic(Entity e) {
        this.dynamics.remove(e);
    }
    
    public void addThis(IECSManager ecs) {
        for (Entity e : statics) {
            ecs.addEntity(e);
        }
        for (Entity e : dynamics) {
            ecs.addEntity(e);
        }
        ecs.addEntity(entity);
    }
    
    public void removeThis(IECSManager ecs) {
        for (Entity e : statics) {
            ecs.removeEntity(e);
        }
        for (Entity e : dynamics) {
            ecs.removeEntity(e);
        }
        ecs.removeEntity(entity);
    }
    
    public Tile getTile(int lx, int ly) {
        return tiles[lx][ly];
    }
    
    public void setTile(Tile t, int i, int j) {
        this.tiles[i][j] = t;
    }
    
    public void pack() {
        if (entity != null) {
            throw new IllegalStateException("Already packed");
        }
        entity = new Entity();
        OrderedCachedVertexManager VERTEX_MANAGER = new OrderedCachedVertexManager(
                6 * CHUNK_TILE_SIZE * CHUNK_TILE_SIZE);
        AdvancedBatch2D PACKING_BATCH = new AdvancedBatch2D(VERTEX_MANAGER);
        PACKING_BATCH.begin();
        Matrix3x2f tmpTransform = new Matrix3x2f();
        for (int i = 0; i < CHUNK_TILE_SIZE; i++) {
            for (int j = 0; j < CHUNK_TILE_SIZE; j++) {
                Tile t = tiles[i][j];
                PACKING_BATCH.reflectionStrength().set(t.getType().getReflectiveness());
                tmpTransform.setTranslation(i * Tile.TILE_SIZE + Tile.TILE_SIZE * CHUNK_TILE_SIZE * chunkX,
                        j * Tile.TILE_SIZE + Tile.TILE_SIZE * CHUNK_TILE_SIZE * chunkY);
                PACKING_BATCH.draw(t.getType().getTexture(), tmpTransform, Tile.TILE_SIZE, Tile.TILE_SIZE, false,
                        false);
            }
        }
        PACKING_BATCH.end();
        ChunkSprite sprite = new ChunkSprite(VERTEX_MANAGER, chunkX, chunkY);
        entity.addComponent(new RenderComponent(sprite));
    }
    
}
