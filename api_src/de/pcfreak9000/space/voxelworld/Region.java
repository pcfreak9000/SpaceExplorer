package de.pcfreak9000.space.voxelworld;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import org.joml.Matrix3x2f;

import de.omnikryptec.ecs.Entity;
import de.omnikryptec.ecs.IECSManager;
import de.omnikryptec.render.batch.AdvancedBatch2D;
import de.omnikryptec.render.batch.Batch2D;
import de.omnikryptec.render.batch.SimpleBatch2D;
import de.omnikryptec.render.batch.vertexmanager.OrderedCachedVertexManager;
import de.omnikryptec.render.objects.AdvancedSprite;
import de.omnikryptec.render.objects.Sprite;
import de.omnikryptec.util.Logger;
import de.omnikryptec.util.data.Color;
import de.omnikryptec.util.math.Mathd;
import de.pcfreak9000.space.voxelworld.ecs.RenderComponent;
import de.pcfreak9000.space.voxelworld.tile.Tile;

public class Region {
    
    private static final Logger LOGGER = Logger.getLogger(Region.class);
    
    public static final int REGION_TILE_SIZE = 256;
    
    private static final float BACKGROUND_FACTOR = 0.5f;
    
    public static int toGlobalRegion(int globalTile) {
        return (int) Mathd.floor(globalTile / (double) REGION_TILE_SIZE);
    }
    
    private int tx;
    private int ty;
    
    private final TileWorld tileWorld;
    
    private Quadtree<Tile> tiles;
    private Quadtree<Tile> tilesBackground;
    private List<Entity> entitiesStatic;
    private List<Entity> entitiesDynamic;
    
    private OrderedCachedVertexManager ocvm;
    private OrderedCachedVertexManager lightOcvm;
    private Entity regionEntity;
    
    public Region(int rx, int ry, TileWorld tw) {
        this.tileWorld = tw;
        this.tx = rx * REGION_TILE_SIZE;
        this.ty = ry * REGION_TILE_SIZE;
        this.tiles = new Quadtree<>(REGION_TILE_SIZE, tx, ty);
        this.tilesBackground = new Quadtree<>(REGION_TILE_SIZE, tx, ty);
        this.entitiesStatic = new ArrayList<>();
        this.entitiesDynamic = new ArrayList<>();
        this.ocvm = new OrderedCachedVertexManager(6 * REGION_TILE_SIZE);
        this.lightOcvm = new OrderedCachedVertexManager(6 * REGION_TILE_SIZE);
        this.regionEntity = new Entity();
        RenderComponent rc = new RenderComponent(new AdvancedSprite() {
            @Override
            public void draw(Batch2D batch) {
                Region.this.ocvm.draw(batch);
            }
        });
        rc.light = new Sprite() {
            
            @Override
            public void draw(Batch2D batch) {
                Region.this.lightOcvm.draw(batch);
            }
        };
        this.regionEntity.addComponent(rc);
    }
    
    public int getGlobalTileX() {
        return tx;
    }
    
    public int getGlobalTileY() {
        return ty;
    }
    
    public void setTile(Tile t) {
        this.tiles.set(t, t.getGlobalTileX(), t.getGlobalTileY());
        if (t.getType().hasLight()) {
            addLight(t);
        }
    }
    
    public void removeTile(int x, int y) {
        Tile old = this.tiles.set(null, x, y);
        if (old != null && old.getType().hasLight()) {
            removeLight(old);
        }
    }
    
    private void addLight(Tile t) {
        
    }
    
    private void removeLight(Tile t) {
        
    }
    
    public void setTileBackground(Tile t) {
        this.tilesBackground.set(t, t.getGlobalTileX(), t.getGlobalTileY());
        if (t.getType().hasLight()) {
            addLight(t);
        }
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
    
    public void tileIntersections(Collection<Tile> output, int x, int y, int w, int h) {
        this.tiles.getAABB(output, x, y, w, h);
    }
    
    public Tile get(int x, int y) {
        return this.tiles.get(x, y);
    }
    
    public void recacheLights() {
        LOGGER.debug("Recaching light: " + toString());
        lightOcvm.clear();
        SimpleBatch2D PACKING_BATCH = new SimpleBatch2D(lightOcvm);
        PACKING_BATCH.begin();
        Matrix3x2f tmpTransform = new Matrix3x2f();
        List<Tile> tiles = new ArrayList<>();
        Predicate<Tile> predicate = (t) -> {
            Color c = t.getLight();
            return c.getR() != 0 || c.getG() == 0 || c.getB() == 0;
        };
        //background does not need to be recached all the time because it can not change (rn)
        this.tilesBackground.getAll(tiles, predicate);
        for (Tile t : tiles) {
            Color c = t.getLight();
            PACKING_BATCH.color().set(c);
            tmpTransform.setTranslation(t.getGlobalTileX() * Tile.TILE_SIZE, t.getGlobalTileY() * Tile.TILE_SIZE);
            PACKING_BATCH.draw(null, tmpTransform, Tile.TILE_SIZE, Tile.TILE_SIZE, false, false);
        }
        PACKING_BATCH.end();
    }
    
    public void recacheTiles() {//TODO improve recaching
        LOGGER.debug("Recaching: " + toString());
        ocvm.clear();
        AdvancedBatch2D PACKING_BATCH = new AdvancedBatch2D(ocvm);
        PACKING_BATCH.begin();
        Matrix3x2f tmpTransform = new Matrix3x2f();
        List<Tile> tiles = new ArrayList<>();
        //background does not need to be recached all the time because it can not change (rn)
        this.tilesBackground.getAll(tiles);
        PACKING_BATCH.color().setAllRGB(BACKGROUND_FACTOR);
        for (Tile t : tiles) {
            tmpTransform.setTranslation(t.getGlobalTileX() * Tile.TILE_SIZE, t.getGlobalTileY() * Tile.TILE_SIZE);
            PACKING_BATCH.draw(t.getType().getTexture(), tmpTransform, Tile.TILE_SIZE, Tile.TILE_SIZE, false, false);
        }
        tiles.clear();
        PACKING_BATCH.color().setAll(1);
        this.tiles.getAll(tiles);
        for (Tile t : tiles) {
            tmpTransform.setTranslation(t.getGlobalTileX() * Tile.TILE_SIZE, t.getGlobalTileY() * Tile.TILE_SIZE);
            PACKING_BATCH.draw(t.getType().getTexture(), tmpTransform, Tile.TILE_SIZE, Tile.TILE_SIZE, false, false);
        }
        PACKING_BATCH.end();
    }
    
    @Override
    public String toString() {
        return String.format("Region[x=%d, y=%d]", this.tx, this.ty);
    }
}
