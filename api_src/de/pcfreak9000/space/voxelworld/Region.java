package de.pcfreak9000.space.voxelworld;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.joml.Matrix3x2f;

import de.omnikryptec.ecs.Entity;
import de.omnikryptec.ecs.IECSManager;
import de.omnikryptec.render.batch.AdvancedBatch2D;
import de.omnikryptec.render.batch.Batch2D;
import de.omnikryptec.render.batch.vertexmanager.OrderedCachedVertexManager;
import de.omnikryptec.render.objects.AdvancedSprite;
import de.omnikryptec.util.Logger;
import de.omnikryptec.util.math.Mathd;
import de.pcfreak9000.space.voxelworld.ecs.RenderComponent;
import de.pcfreak9000.space.voxelworld.tile.Tile;

public class Region {
    
    private static final Logger LOGGER = Logger.getLogger(Region.class);
    
    public static final int REGION_TILE_SIZE = 256;
    
    public static int toGlobalRegion(int globalTile) {
        return (int) Mathd.floor(globalTile / (double) REGION_TILE_SIZE);
    }
    
    private int tx;
    private int ty;
    
    private Quadtree<Tile> tiles;
    private List<Entity> entitiesStatic;
    private List<Entity> entitiesDynamic;
    
    private OrderedCachedVertexManager ocvm;
    private Entity regionEntity;
    
    public Region(int rx, int ry) {
        this.tx = rx * REGION_TILE_SIZE;
        this.ty = ry * REGION_TILE_SIZE;
        this.tiles = new Quadtree<>(REGION_TILE_SIZE, tx, ty);
        this.entitiesStatic = new ArrayList<>();
        this.entitiesDynamic = new ArrayList<>();
        this.ocvm = new OrderedCachedVertexManager(6 * REGION_TILE_SIZE);
        this.regionEntity = new Entity();
        this.regionEntity.addComponent(new RenderComponent(new AdvancedSprite() {
            @Override
            public void draw(Batch2D batch) {
                Region.this.ocvm.draw(batch);
                //Region.this.tiles.draw(batch);
            }
        }));
    }
    
    public int getGlobalTileX() {
        return tx;
    }
    
    public int getGlobalTileY() {
        return ty;
    }
    
    public void setTile(Tile t) {
        this.tiles.set(t, t.getGlobalTileX(), t.getGlobalTileY());
    }
    
    public void removeTile(int x, int y) {
        this.tiles.set(null, x, y);
    }
    
    public void addThis(IECSManager ecsManager) {
        for (Entity e : entitiesStatic) {
            ecsManager.addEntity(e);
        }
        for (Entity e : entitiesDynamic) {
            ecsManager.addEntity(e);
        }
        ecsManager.addEntity(regionEntity);
    }
    
    public void removeThis(IECSManager ecsManager) {
        for (Entity e : entitiesStatic) {
            ecsManager.removeEntity(e);
        }
        for (Entity e : entitiesDynamic) {
            ecsManager.removeEntity(e);
        }
        ecsManager.removeEntity(regionEntity);
    }
    
    public void recache() {//TODO improve
        LOGGER.debug("Recaching");
        ocvm.clear();
        AdvancedBatch2D PACKING_BATCH = new AdvancedBatch2D(ocvm);
        PACKING_BATCH.begin();
        Matrix3x2f tmpTransform = new Matrix3x2f();
        List<Tile> tiles = new ArrayList<>();
        this.tiles.getAll(tiles);
        for (Tile t : tiles) {
            tmpTransform.setTranslation(t.getGlobalTileX() * Tile.TILE_SIZE, t.getGlobalTileY() * Tile.TILE_SIZE);
            PACKING_BATCH.draw(t.getType().getTexture(), tmpTransform, Tile.TILE_SIZE, Tile.TILE_SIZE, false, false);
            
        }
        PACKING_BATCH.end();
    }
    
    public void tileIntersections(Collection<Tile> output, int x, int y, int w, int h) {
        this.tiles.getAABB(output, x, y, w, h);
    }
    
}
