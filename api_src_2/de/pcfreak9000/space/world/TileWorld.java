package de.pcfreak9000.space.world;

import de.omnikryptec.util.Logger;

public class TileWorld {
    
    private static final Logger LOGGER = Logger.getLogger(TileWorld.class);
    
    private final Chunk[][] chunks;
    private final int chunksSize;
    
    private final IGenerator generator;
    
    public TileWorld(int tileRadius, IGenerator generator) {
        this.chunksSize = ((int) Math.ceil((double) tileRadius / Chunk.CHUNK_TILE_SIZE)) << 1;
        if (this.chunksSize < 0) {
            // Should not happen
            LOGGER.error("World size is negative");
        }
        this.chunks = new Chunk[this.chunksSize][this.chunksSize];
        this.generator = generator;
    }
    
    public int getChunkRadius() {
        return chunksSize >> 1;
    }
    
    public Chunk requireGet(final int cx, final int cy) {
        if (!inBounds(cx, cy)) {
            LOGGER.error("Requiring out of bounds chunk");
            return null;
        }
        Chunk c = getChunk(cx, cy);
        if (c == null) {
            c = loadChunk(cx, cy);
            if (c == null) {
                c = new Chunk(cx, cy);
                generator.generateChunk(c);
                c.pack();
            }
            setChunk(c);
        }
        return c;
    }
    
    public void unRequire(Chunk c) {
        //save chunk...
    }
    
    private Chunk loadChunk(int cx, int cy) {
        //load chunk...
        return null;
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
}
