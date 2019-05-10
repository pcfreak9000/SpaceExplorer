package de.pcfreak9000.space.world;

import de.omnikryptec.util.math.Mathd;

public class Chunk {
    public static final int CHUNK_TILE_SIZE = 53;
    
    public static int toGlobalChunk(int globalTile) {
        return (int) Mathd.floor(globalTile / (double) CHUNK_TILE_SIZE);
    }
    
    private final int chunkX;
    private final int chunkY;
    
    Chunk(int cx, int cy) {
        this.chunkX = cx;
        this.chunkY = cy;
    }
    
    public int getChunkX() {
        return chunkX;
    }
    
    public int getChunkY() {
        return chunkY;
    }
    
    public void pack() {
        
    }
    
}
