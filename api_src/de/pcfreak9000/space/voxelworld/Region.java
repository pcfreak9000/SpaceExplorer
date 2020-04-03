package de.pcfreak9000.space.voxelworld;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.function.Predicate;

import org.joml.FrustumIntersection;
import org.joml.Matrix3x2f;

import de.omnikryptec.core.Omnikryptec;
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
import de.omnikryptec.util.math.Mathf;
import de.pcfreak9000.space.voxelworld.ecs.RenderComponent;
import de.pcfreak9000.space.voxelworld.tile.Tile;
import de.pcfreak9000.space.voxelworld.tile.TileType;

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
    private boolean recacheTiles = true;
    private OrderedCachedVertexManager lightOcvm;
    private boolean recacheLights = true;
    private Entity regionEntity;
    private Queue<Tile> lightBfsQueue = new ArrayDeque<>();
    private Queue<RemovalNode> lightRemovalBfsQueue = new ArrayDeque<>();
    
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
                if (recacheTiles) {
                    recacheTiles = false;
                    recacheTiles();
                }
                Region.this.ocvm.draw(batch);
                batch.color().set(1, 0, 0, 1);
                float left = tx * Tile.TILE_SIZE;
                float right = (tx + REGION_TILE_SIZE) * Tile.TILE_SIZE;
                float top = ty * Tile.TILE_SIZE;
                float bot = (ty + REGION_TILE_SIZE) * Tile.TILE_SIZE;
                batch.drawLine(left, bot, left, top, 2);
                batch.drawLine(left, bot, right, bot, 2);
                batch.drawLine(right, top, left, top, 2);
                batch.drawLine(right, top, right, bot, 2);
                batch.color().setAll(1);
            }
            
            @Override
            public boolean isVisible(FrustumIntersection frustum) {
                return frustum.testAab(tx * Tile.TILE_SIZE, ty * Tile.TILE_SIZE, 0,
                        (tx + REGION_TILE_SIZE) * Tile.TILE_SIZE, (ty + REGION_TILE_SIZE) * Tile.TILE_SIZE, 0);
                
            }
        });
        rc.light = new Sprite() {
            
            @Override
            public void draw(Batch2D batch) {
                if (recacheLights) {
                    recacheLights = false;
                    recacheLights();
                }
                Region.this.lightOcvm.draw(batch);
                batch.color().set(1, 0, 0, 1);
                float left = tx * Tile.TILE_SIZE;
                float right = (tx + REGION_TILE_SIZE) * Tile.TILE_SIZE;
                float top = ty * Tile.TILE_SIZE;
                float bot = (ty + REGION_TILE_SIZE) * Tile.TILE_SIZE;
                batch.drawLine(left, bot, left, top, 2);
                batch.drawLine(left, bot, right, bot, 2);
                batch.drawLine(right, top, left, top, 2);
                batch.drawLine(right, top, right, bot, 2);
                batch.color().setAll(1);
            }
            
            @Override
            public boolean isVisible(FrustumIntersection frustum) {
                return frustum.testAab((tx - 3) * Tile.TILE_SIZE, (ty - 3) * Tile.TILE_SIZE, 0,
                        (tx + REGION_TILE_SIZE + 3) * Tile.TILE_SIZE, (ty + REGION_TILE_SIZE + 3) * Tile.TILE_SIZE, 0);
                
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
        if (old != null && old.lightV != 0) {
            removeLight(old);
        }
    }
    
    private void addLight(Tile light) {
        lightBfsQueue.add(light);
        light.lightV = TileType.MAX_LIGHT_VALUE;
        queueRecacheLights();
    }
    
    private void removeLight(Tile light) {
        RemovalNode node = new RemovalNode();
        node.t = light;
        node.v = light.lightV;
        lightRemovalBfsQueue.add(node);
        queueRecacheLights();
    }
    
    public void setTileBackground(Tile t) {
        this.tilesBackground.set(t, t.getGlobalTileX(), t.getGlobalTileY());
        if (t.getType().hasLight()) {
            //addLight(t);
            //queueRecacheLights();
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
    
    private static class RemovalNode {
        Tile t;
        int v;
    }
    
    private void resolveLights() {
        while (!lightRemovalBfsQueue.isEmpty()) {
            RemovalNode front = lightRemovalBfsQueue.poll();
            int tx = front.t.getGlobalTileX();
            int ty = front.t.getGlobalTileY();
            if (tileWorld.inBounds(tx + 1, ty)) {
                Tile t = tileWorld.get(tx + 1, ty);
                if (t != null) {
                    int tLvl = t.lightV;
                    if (tLvl != 0 && tLvl < front.v) {
                        RemovalNode node = new RemovalNode();
                        node.t = t;
                        node.v = tLvl;
                        t.lightV = 0;
                        lightRemovalBfsQueue.add(node);
                    } else if (tLvl >= front.v) {
                        lightBfsQueue.add(t);
                    }
                }
            }
            if (tileWorld.inBounds(tx - 1, ty)) {
                Tile t = tileWorld.get(tx - 1, ty);
                if (t != null) {
                    int tLvl = t.lightV;
                    if (tLvl != 0 && tLvl < front.v) {
                        RemovalNode node = new RemovalNode();
                        node.t = t;
                        node.v = tLvl;
                        t.lightV = 0;
                        lightRemovalBfsQueue.add(node);
                    } else if (tLvl >= front.v) {
                        lightBfsQueue.add(t);
                    }
                }
            }
            if (tileWorld.inBounds(tx, ty + 1)) {
                Tile t = tileWorld.get(tx, ty + 1);
                if (t != null) {
                    int tLvl = t.lightV;
                    if (tLvl != 0 && tLvl < front.v) {
                        RemovalNode node = new RemovalNode();
                        node.t = t;
                        node.v = tLvl;
                        t.lightV = 0;
                        lightRemovalBfsQueue.add(node);
                    } else if (tLvl >= front.v) {
                        lightBfsQueue.add(t);
                    }
                }
            }
            if (tileWorld.inBounds(tx, ty - 1)) {
                Tile t = tileWorld.get(tx, ty - 1);
                if (t != null) {
                    int tLvl = t.lightV;
                    if (tLvl != 0 && tLvl < front.v) {
                        RemovalNode node = new RemovalNode();
                        node.t = t;
                        node.v = tLvl;
                        t.lightV = 0;
                        lightRemovalBfsQueue.add(node);
                    } else if (tLvl >= front.v) {
                        lightBfsQueue.add(t);
                    }
                }
            }
        }
        while (!lightBfsQueue.isEmpty()) {
            Tile front = lightBfsQueue.poll();
            int tx = front.getGlobalTileX();
            int ty = front.getGlobalTileY();
            if (tileWorld.inBounds(tx + 1, ty)) {
                Tile t = tileWorld.get(tx + 1, ty);
                if (t != null && t.lightV + 2 <= front.lightV) {
                    t.lightV = front.lightV - 1;
                    lightBfsQueue.add(t);
                }
            }
            if (tileWorld.inBounds(tx - 1, ty)) {
                Tile t = tileWorld.get(tx - 1, ty);
                if (t != null && t.lightV + 2 <= front.lightV) {
                    t.lightV = front.lightV - 1;
                    lightBfsQueue.add(t);
                }
            }
            if (tileWorld.inBounds(tx, ty + 1)) {
                Tile t = tileWorld.get(tx, ty + 1);
                if (t != null && t.lightV + 2 <= front.lightV) {
                    t.lightV = front.lightV - 1;
                    lightBfsQueue.add(t);
                }
            }
            if (tileWorld.inBounds(tx, ty - 1)) {
                Tile t = tileWorld.get(tx, ty - 1);
                if (t != null && t.lightV + 2 <= front.lightV) {
                    t.lightV = front.lightV - 1;
                    lightBfsQueue.add(t);
                }
            }
        }
        
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
    
    public void queueRecacheLights() {
        this.recacheLights = true;
    }
    
    private void recacheLights() {
        resolveLights();
        LOGGER.debug("Recaching lights: " + toString());
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
            //            Tile fore = this.tiles.get(t.getGlobalTileX(), t.getGlobalTileY());
            //            if (fore != null && fore.getType().isOpaque()) {
            //                //hmmmm
            //                continue;
            //            }
            Color c = t.getLight();
            float factor = t.lightV / (float) TileType.MAX_LIGHT_VALUE;
            PACKING_BATCH.color().set(c.getR() * factor, c.getG() * factor, c.getB() * factor, 1);
            tmpTransform.setTranslation(t.getGlobalTileX() * Tile.TILE_SIZE, t.getGlobalTileY() * Tile.TILE_SIZE);
            PACKING_BATCH.draw(null, tmpTransform, Tile.TILE_SIZE * 1, Tile.TILE_SIZE * 1, false, false);
        }
        tiles.clear();
        this.tiles.getAll(tiles, predicate);
        for (Tile t : tiles) {
            Color c = t.getLight();
            float factor = t.lightV / (float) TileType.MAX_LIGHT_VALUE;
            PACKING_BATCH.color().set(c.getR() * factor, c.getG() * factor, c.getB() * factor, 1);
            tmpTransform.setTranslation(t.getGlobalTileX() * Tile.TILE_SIZE, t.getGlobalTileY() * Tile.TILE_SIZE);
            PACKING_BATCH.draw(null, tmpTransform, Tile.TILE_SIZE * 1, Tile.TILE_SIZE * 1, false, false);
        }
        PACKING_BATCH.end();
    }
    
    public void queueRecacheTiles() {
        this.recacheTiles = true;
    }
    
    private void recacheTiles() {//TODO improve recaching
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
