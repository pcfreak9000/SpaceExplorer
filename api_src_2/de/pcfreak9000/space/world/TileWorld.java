package de.pcfreak9000.space.world;

import org.joml.Vector2fc;

import de.omnikryptec.ecs.Entity;
import de.omnikryptec.ecs.component.ComponentType;
import de.omnikryptec.util.Logger;
import de.omnikryptec.util.math.transform.Transform2Df;
import de.pcfreak9000.space.world.ecs.RenderComponent;
import de.pcfreak9000.space.world.ecs.TransformComponent;
import de.pcfreak9000.space.world.tile.Tile;

public class TileWorld {
    
    private static final Logger LOGGER = Logger.getLogger(TileWorld.class);
    
    private final Chunk[][] chunks;
    private final int chunksSize;
    
    private final IGenerator generator;
    
    public TileWorld(int tileRadius, IGenerator generator) {
        this.chunks = initChunkArray(tileRadius);
        this.chunksSize = calculateChunksSize(tileRadius);
        this.generator = generator;
    }
    
    private Chunk[][] initChunkArray(int tileRadius) {
        if (tileRadius >= 99999 / Tile.TILE_SIZE) {
            LOGGER.error("World size exceeds precision");
        }
        int chunksSize = calculateChunksSize(tileRadius);
        if (chunksSize < 0) {
            // Should not happen
            LOGGER.error("World size is negative");
        }
        return new Chunk[chunksSize][chunksSize];
    }
    
    private int calculateChunksSize(int tileRadius) {
        return ((int) Math.ceil((double) tileRadius / Chunk.CHUNK_TILE_SIZE)) << 1;
    }
    
    public int getChunkRadius() {
        return chunksSize >> 1;
    }
    
    public Chunk requireGetc(final int cx, final int cy) {
        if (!inBounds(cx, cy)) {
            LOGGER.error("Requiring out of bounds chunk");
            return null;
        }
        Chunk c = getChunk(cx, cy);
        if (c == null) {
            c = new Chunk(cx, cy);
            generator.generateChunk(c);
            LOGGER.info("Generated chunk at " + cx + " " + cy);
            c.pack();
            setChunk(c);
        }
        return c;
    }
    
    public Chunk requireGete(Transform2Df t) {
        Vector2fc v = t.wPosition();
        int cx = Chunk.toGlobalChunk(Tile.toGlobalTile(v.x()));
        int cy = Chunk.toGlobalChunk(Tile.toGlobalTile(v.y()));
        Chunk chunk = this.requireGetc(cx, cy);
        return chunk;
    }
    
    public Chunk requireGett(int tilex, int tiley) {
        return requireGetc(Chunk.toGlobalChunk(tilex), Chunk.toGlobalChunk(tiley));
    }
    
    /**
     * 
     * @param cx global chunk x
     * @param cy global chunk y
     * @return a Chunk
     */
    public Chunk getChunk(final int cx, final int cy) {
        if (!inBounds(cx, cy)) {
            return null;
        }
        return this.chunks[cx + (this.chunksSize >> 1)][cy + (this.chunksSize >> 1)];
    }
    
    private void setChunk(Chunk c) {
        if (!inBounds(c.getChunkX(), c.getChunkY())) {
            LOGGER.error("Out of bounds chunk: " + c);
        }
        this.chunks[c.getChunkX() + (this.chunksSize >> 1)][c.getChunkY() + (this.chunksSize >> 1)] = c;
    }
    
    /**
     * Is the chunk position contained in the boundaries of this {@link TileWorld}?
     * 
     * @param cx global chunk x
     * @param cy global chunk y
     * @return boolean
     */
    public boolean inBounds(final int cx, final int cy) {
        return !(cx >= (this.chunksSize >> 1) || cy >= (this.chunksSize >> 1) || cx < -(this.chunksSize >> 1)
                || cy < -(this.chunksSize >> 1));
    }
    
    /******************** Entity stuff ********************/
    
    private static final ComponentType RENDER_TYPE = ComponentType.of(RenderComponent.class);
    private static final ComponentType TRANSFORM_TYPE = ComponentType.of(TransformComponent.class);
    
    public void addDynamicEntity(Entity e) {
        if (hasTransform(e)) {
            requireGete(getTransform(e)).addDynamic(e);
        }
    }
    
    public void removeDynamicEntity(Entity e) {
        if (hasTransform(e)) {
            requireGete(getTransform(e)).removeDynamic(e);
        }
    }
    
    public void addStaticEntity(Entity e) {
        if (hasTransform(e)) {
            requireGete(getTransform(e)).addStatic(e);
        }
    }
    
    public void removeStaticEntity(Entity e) {
        if (hasTransform(e)) {
            requireGete(getTransform(e)).removeStatic(e);
        }
    }
    
    private Transform2Df getTransform(Entity e) {
        if (e.hasComponent(RENDER_TYPE)) {
            RenderComponent rc = e.getComponent(RENDER_TYPE);
            return rc.sprite.getTransform();
        } else if (e.hasComponent(TRANSFORM_TYPE)) {
            TransformComponent tc = e.getComponent(TRANSFORM_TYPE);
            return tc.transform;
        }
        return null;
    }
    
    private boolean hasTransform(Entity e) {
        return e.hasComponent(RENDER_TYPE) || e.hasComponent(TRANSFORM_TYPE);
    }
}
