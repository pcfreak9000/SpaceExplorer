package de.pcfreak9000.space.world;

import de.omnikryptec.util.math.transform.Transform2Df;
import de.pcfreak9000.space.world.tile.Tile;

public class WorldLoadingFence {
    
    private Transform2Df following;
    
    private int xChunkRange = 1;
    private int yChunkRange = 1;
    //Is this even useful?
    private int xChunkOffset;
    private int yChunkOffset;
    
    public WorldLoadingFence(Transform2Df foll) {
        this.following = foll;
    }
    
    public int getChunkMidpointX() {
        return xChunkOffset + Chunk.toGlobalChunk(Tile.toGlobalTile(following.wPosition().x()));
    }
    
    public int getChunkMidpointY() {
        return yChunkOffset + Chunk.toGlobalChunk(Tile.toGlobalTile(following.wPosition().y()));
    }
    
    public int getChunkRadiusRangeX() {
        return xChunkRange;
    }
    
    public int getChunkRadiusRangeY() {
        return yChunkRange;
    }
    
    public void setRange(int xR, int yR) {
        this.xChunkRange = xR;
    }
}
