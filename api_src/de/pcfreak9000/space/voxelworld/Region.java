package de.pcfreak9000.space.voxelworld;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.function.Predicate;

import org.joml.FrustumIntersection;
import org.joml.Matrix3x2f;

import com.google.common.base.Objects;

import de.omnikryptec.core.Omnikryptec;
import de.omnikryptec.ecs.Entity;
import de.omnikryptec.ecs.IECSManager;
import de.omnikryptec.libapi.exposed.render.Texture;
import de.omnikryptec.render.batch.AdvancedBatch2D;
import de.omnikryptec.render.batch.Batch2D;
import de.omnikryptec.render.batch.SimpleBatch2D;
import de.omnikryptec.render.batch.vertexmanager.OrderedCachedVertexManager;
import de.omnikryptec.render.objects.AdvancedSprite;
import de.omnikryptec.render.objects.Sprite;
import de.omnikryptec.util.Logger;
import de.omnikryptec.util.Util;
import de.omnikryptec.util.data.Color;
import de.omnikryptec.util.math.Mathd;
import de.pcfreak9000.space.voxelworld.ecs.RenderComponent;
import de.pcfreak9000.space.voxelworld.tile.Tile;
import de.pcfreak9000.space.voxelworld.tile.TileType;

public class Region {

    private static class RemovalNode {
        Tile t;
        float v;
    }

    private static final Logger LOGGER = Logger.getLogger(Region.class);

    private static final boolean DEBUG_SHOW_BORDERS = true;

    public static final int REGION_TILE_SIZE = 64;

    private static final float BACKGROUND_FACTOR = 0.5f;

    public static int toGlobalRegion(int globalTile) {
        return (int) Mathd.floor(globalTile / (double) REGION_TILE_SIZE);
    }

    private final int rx;
    private final int ry;

    private final int tx;
    private final int ty;

    private final TileWorld tileWorld;

    private final Quadtree<Tile> tiles;
    private final Quadtree<Tile> tilesBackground;
    private final List<Entity> entitiesStatic;
    private final List<Entity> entitiesDynamic;

    private boolean recacheTiles;
    private boolean recacheLights;
    private final Queue<Tile> lightBfsQueue;
    private final Queue<RemovalNode>[] lightRemovalBfsQueue;
    private final OrderedCachedVertexManager ocvm;
    private final OrderedCachedVertexManager lightOcvm;
    private final Entity regionEntity;

    public Region(int rx, int ry, TileWorld tw) {
        this.tileWorld = tw;
        this.rx = rx;
        this.ry = ry;
        this.tx = rx * REGION_TILE_SIZE;
        this.ty = ry * REGION_TILE_SIZE;
        this.tiles = new Quadtree<>(REGION_TILE_SIZE, this.tx, this.ty);
        this.tilesBackground = new Quadtree<>(REGION_TILE_SIZE, this.tx, this.ty);
        this.entitiesStatic = new ArrayList<>();
        this.entitiesDynamic = new ArrayList<>();
        this.ocvm = new OrderedCachedVertexManager(6 * REGION_TILE_SIZE);
        this.lightOcvm = new OrderedCachedVertexManager(6 * REGION_TILE_SIZE);
        this.regionEntity = new Entity();
        this.lightBfsQueue = new ArrayDeque<>();
        this.lightRemovalBfsQueue = new Queue[3];
        for (int i = 0; i < this.lightRemovalBfsQueue.length; i++) {
            this.lightRemovalBfsQueue[i] = new ArrayDeque<>();
        }
        this.recacheLights = true;
        this.recacheTiles = true;
        RenderComponent rc = new RenderComponent(new AdvancedSprite() {
            @Override
            public void draw(Batch2D batch) {
                if (Region.this.recacheTiles) {
                    Region.this.recacheTiles = false;
                    recacheTiles();
                }
                Region.this.ocvm.draw(batch);
                if (DEBUG_SHOW_BORDERS) {
                    batch.color().set(1, 0, 0, 1);
                    float left = Region.this.tx * Tile.TILE_SIZE;
                    float right = (Region.this.tx + REGION_TILE_SIZE) * Tile.TILE_SIZE;
                    float top = Region.this.ty * Tile.TILE_SIZE;
                    float bot = (Region.this.ty + REGION_TILE_SIZE) * Tile.TILE_SIZE;
                    batch.drawLine(left, bot, left, top, 2);
                    batch.drawLine(left, bot, right, bot, 2);
                    batch.drawLine(right, top, left, top, 2);
                    batch.drawLine(right, top, right, bot, 2);
                    batch.color().setAll(1);
                }
            }

            @Override
            public boolean isVisible(FrustumIntersection frustum) {
                return frustum.testAab(Region.this.tx * Tile.TILE_SIZE, Region.this.ty * Tile.TILE_SIZE, 0,
                        (Region.this.tx + REGION_TILE_SIZE) * Tile.TILE_SIZE,
                        (Region.this.ty + REGION_TILE_SIZE) * Tile.TILE_SIZE, 0);

            }
        });
        rc.light = new Sprite() {

            @Override
            public void draw(Batch2D batch) {
                if (Region.this.recacheLights) {
                    Region.this.recacheLights = false;
                    recacheLights();
                }
                Region.this.lightOcvm.draw(batch);
                if (DEBUG_SHOW_BORDERS) {
                    batch.color().set(1, 0, 0, 1);
                    float left = Region.this.tx * Tile.TILE_SIZE;
                    float right = (Region.this.tx + REGION_TILE_SIZE) * Tile.TILE_SIZE;
                    float top = Region.this.ty * Tile.TILE_SIZE;
                    float bot = (Region.this.ty + REGION_TILE_SIZE) * Tile.TILE_SIZE;
                    batch.drawLine(left, bot, left, top, 2);
                    batch.drawLine(left, bot, right, bot, 2);
                    batch.drawLine(right, top, left, top, 2);
                    batch.drawLine(right, top, right, bot, 2);
                    batch.color().setAll(1);
                }
            }

            @Override
            public boolean isVisible(FrustumIntersection frustum) {
                return frustum.testAab((Region.this.tx - 3) * Tile.TILE_SIZE, (Region.this.ty - 3) * Tile.TILE_SIZE, 0,
                        (Region.this.tx + REGION_TILE_SIZE + 3) * Tile.TILE_SIZE,
                        (Region.this.ty + REGION_TILE_SIZE + 3) * Tile.TILE_SIZE, 0);

            }
        };
        this.regionEntity.addComponent(rc);
    }

    public void queueRecacheLights() {
        this.recacheLights = true;
    }

    public void queueRecacheTiles() {
        this.recacheTiles = true;
    }

    public int getGlobalRegionX() {
        return this.rx;
    }

    public int getGlobalRegionY() {
        return this.ry;
    }

    public int getGlobalTileX() {
        return this.tx;
    }

    public int getGlobalTileY() {
        return this.ty;
    }

    public void tileIntersections(Collection<Tile> output, int x, int y, int w, int h) {
        this.tiles.getAABB(output, x, y, w, h);
    }

    public Tile get(int x, int y) {
        return this.tiles.get(x, y);
    }

    public Tile setTile(Tile t) {
        Util.ensureNonNull(t);
        Tile old = this.tiles.set(t, t.getGlobalTileX(), t.getGlobalTileY());
        if (old != null && (old.light().maxRGB() > 0
                || !Objects.equal(old.getType().getFilterColor(), t.getType().getFilterColor())
                || old.getType().getLightLoss() != t.getType().getLightLoss())) {
            removeLight(old);
        }
        if (t.getType().hasLight()) {
            addLight(t);
        }
        return old;
    }

    public Tile getBackground(int tx, int ty) {
        return this.tilesBackground.get(tx, ty);
    }

    public void setTileBackground(Tile t) {
        this.tilesBackground.set(t, t.getGlobalTileX(), t.getGlobalTileY());
        if (t.getType().hasLight()) {
            //addLight(t);
            //queueRecacheLights();
        }
    }

    private void addLight(Tile light) {
        this.lightBfsQueue.add(light);
        light.light().set(light.getType().getLightColor());
        queueRecacheLights();
    }

    private void removeLight(Tile light) {
        for (int i = 0; i < 3; i++) {
            RemovalNode node = new RemovalNode();
            node.t = light;
            node.v = light.light().get(i);
            if (node.v > 0) {
                this.lightRemovalBfsQueue[i].add(node);
            }
        }
        queueRecacheLights();
    }

    public void addThisTo(IECSManager ecsManager) {
        for (Entity e : this.entitiesStatic) {
            ecsManager.addEntity(e);
        }
        for (Entity e : this.entitiesDynamic) {
            ecsManager.addEntity(e);
        }
        ecsManager.addEntity(this.regionEntity);
    }

    public void removeThisFrom(IECSManager ecsManager) {
        for (Entity e : this.entitiesStatic) {
            ecsManager.removeEntity(e);
        }
        for (Entity e : this.entitiesDynamic) {
            ecsManager.removeEntity(e);
        }
        ecsManager.removeEntity(this.regionEntity);
    }

    private void resolveLights() {
        for (int i = 0; i < this.lightRemovalBfsQueue.length; i++) {
            while (!this.lightRemovalBfsQueue[i].isEmpty()) {
                RemovalNode front = this.lightRemovalBfsQueue[i].poll();
                int tx = front.t.getGlobalTileX();
                int ty = front.t.getGlobalTileY();
                if (this.tileWorld.inBounds(tx + 1, ty)) {
                    Tile t = this.tileWorld.get(tx + 1, ty);
                    checkRemoveLightHelper(front, t, i);
                }
                if (this.tileWorld.inBounds(tx - 1, ty)) {
                    Tile t = this.tileWorld.get(tx - 1, ty);
                    checkRemoveLightHelper(front, t, i);
                }
                if (this.tileWorld.inBounds(tx, ty + 1)) {
                    Tile t = this.tileWorld.get(tx, ty + 1);
                    checkRemoveLightHelper(front, t, i);
                }
                if (this.tileWorld.inBounds(tx, ty - 1)) {
                    Tile t = this.tileWorld.get(tx, ty - 1);
                    checkRemoveLightHelper(front, t, i);
                }
            }
        }
        while (!this.lightBfsQueue.isEmpty()) {
            Tile front = this.lightBfsQueue.poll();
            int tx = front.getGlobalTileX();
            int ty = front.getGlobalTileY();
            if (front.getType().hasLightFilter()) {
                Color filter = front.getType().getFilterColor();
                front.light().mulRGB(filter);
            }
            if (this.tileWorld.inBounds(tx + 1, ty)) {
                Tile t = this.tileWorld.get(tx + 1, ty);
                checkAddLightHelper(front, t);
            }
            if (this.tileWorld.inBounds(tx - 1, ty)) {
                Tile t = this.tileWorld.get(tx - 1, ty);
                checkAddLightHelper(front, t);
            }
            if (this.tileWorld.inBounds(tx, ty + 1)) {
                Tile t = this.tileWorld.get(tx, ty + 1);
                checkAddLightHelper(front, t);
            }
            if (this.tileWorld.inBounds(tx, ty - 1)) {
                Tile t = this.tileWorld.get(tx, ty - 1);
                checkAddLightHelper(front, t);
            }
        }

    }

    private void checkRemoveLightHelper(RemovalNode front, Tile t, int index) {
        if (t != null) {
            Color col = t.light();
            if (col.get(index) > 0 && col.get(index) < front.v) {
                RemovalNode node = new RemovalNode();
                node.t = t;
                node.v = col.get(index);
                t.light().set(index, 0);
                this.lightRemovalBfsQueue[index].add(node);
                queueNeighbouringLightRecaching(t);
            } else if (col.get(index) >= front.v) {
                this.lightBfsQueue.add(t);
                queueNeighbouringLightRecaching(t);
            }
        }
    }

    private void checkAddLightHelper(Tile front, Tile t) {
        if (t != null) {
            boolean found = false;
            for (int i = 0; i < 3; i++) {
                if (t.light().get(i) + 1 < front.light().get(i)) {
                    t.light().set(i, front.light().get(i) - front.getType().getLightLoss());
                    found = true;
                }
            }
            if (found) {
                this.lightBfsQueue.add(t);
                queueNeighbouringLightRecaching(t);
            }
        }
    }

    private void queueNeighbouringLightRecaching(Tile t) {
        int c = Region.toGlobalRegion(t.getGlobalTileX());
        int d = Region.toGlobalRegion(t.getGlobalTileY());
        if (this.rx != c || this.ry != d) {
            Region r = this.tileWorld.getRegion(c, d);
            if (r != null) {
                r.queueRecacheLights();
            }
        }
    }

    private void recacheLights() {
        resolveLights();
        LOGGER.debug("Recaching lights: " + toString());
        this.lightOcvm.clear();
        SimpleBatch2D PACKING_BATCH = new SimpleBatch2D(this.lightOcvm);
        PACKING_BATCH.begin();
        Matrix3x2f tmpTransform = new Matrix3x2f();
        List<Tile> tiles = new ArrayList<>();
        Predicate<Tile> predicate = (t) -> t.light().maxRGB() >= 1;
        //background does not need to be recached all the time because it can not change (rn)
        this.tilesBackground.getAll(tiles, predicate);
        Texture tex = Omnikryptec.getTexturesS().get("light_2.png");
        float mult = 3.3f;
        for (Tile t : tiles) {
            Color c = t.light();
            float factor = 1 / TileType.MAX_LIGHT_VALUE;
            PACKING_BATCH.color().set(c);
            PACKING_BATCH.color().mulRGB(factor);
            tmpTransform.setTranslation(
                    t.getGlobalTileX() * Tile.TILE_SIZE - mult / 2 * Tile.TILE_SIZE + 0.5f * Tile.TILE_SIZE,
                    t.getGlobalTileY() * Tile.TILE_SIZE - mult / 2 * Tile.TILE_SIZE + 0.5f * Tile.TILE_SIZE);
            PACKING_BATCH.draw(tex, tmpTransform, Tile.TILE_SIZE * mult, Tile.TILE_SIZE * mult, false, false);
        }
        tiles.clear();
        this.tiles.getAll(tiles, predicate);
        for (Tile t : tiles) {
            Color c = t.light();
            float factor = 1 / TileType.MAX_LIGHT_VALUE;
            PACKING_BATCH.color().set(c);
            PACKING_BATCH.color().mulRGB(factor);
            //tmpTransform.setTranslation(t.getGlobalTileX() * Tile.TILE_SIZE, t.getGlobalTileY() * Tile.TILE_SIZE);
            tmpTransform.setTranslation(
                    t.getGlobalTileX() * Tile.TILE_SIZE - mult / 2 * Tile.TILE_SIZE + 0.5f * Tile.TILE_SIZE,
                    t.getGlobalTileY() * Tile.TILE_SIZE - mult / 2 * Tile.TILE_SIZE + 0.5f * Tile.TILE_SIZE);
            PACKING_BATCH.draw(tex, tmpTransform, Tile.TILE_SIZE * mult, Tile.TILE_SIZE * mult, false, false);
        }
        PACKING_BATCH.end();
    }

    private void recacheTiles() {//TODO improve recaching
        LOGGER.debug("Recaching: " + toString());
        this.ocvm.clear();
        AdvancedBatch2D PACKING_BATCH = new AdvancedBatch2D(this.ocvm);
        PACKING_BATCH.begin();
        Matrix3x2f tmpTransform = new Matrix3x2f();
        List<Tile> tiles = new ArrayList<>();
        Predicate<Tile> predicate = (t) -> t.getType().color().getA() > 0;
        //background does not need to be recached all the time because it can not change (rn)
        this.tilesBackground.getAll(tiles, predicate);
        for (Tile t : tiles) {
            PACKING_BATCH.color().set(t.getType().color());
            PACKING_BATCH.color().mulRGB(BACKGROUND_FACTOR);
            tmpTransform.setTranslation(t.getGlobalTileX() * Tile.TILE_SIZE, t.getGlobalTileY() * Tile.TILE_SIZE);
            PACKING_BATCH.draw(t.getType().getTexture(), tmpTransform, Tile.TILE_SIZE, Tile.TILE_SIZE, false, false);
        }
        tiles.clear();
        this.tiles.getAll(tiles, predicate);
        for (Tile t : tiles) {
            PACKING_BATCH.color().set(t.getType().color());
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
