package de.pcfreak9000.space.voxelworld;

import de.omnikryptec.util.math.transform.Transform2Df;
import de.pcfreak9000.space.voxelworld.tile.Tile;

public class WorldLoadingFence {

    private final Transform2Df following;

    private int xChunkRange = 1;
    private int yChunkRange = 1;
    //Is this even useful?
    private int xChunkOffset;
    private int yChunkOffset;

    public WorldLoadingFence(Transform2Df foll) {
        this.following = foll;
    }

    public int getChunkMidpointX() {
        return this.xChunkOffset + Region.toGlobalRegion(Tile.toGlobalTile(this.following.worldspacePos().x()));
    }

    public int getChunkMidpointY() {
        return this.yChunkOffset + Region.toGlobalRegion(Tile.toGlobalTile(this.following.worldspacePos().y()));
    }

    public int getChunkRadiusRangeX() {
        return this.xChunkRange;
    }

    public int getChunkRadiusRangeY() {
        return this.yChunkRange;
    }

    public void setRange(int xR, int yR) {
        this.xChunkRange = xR;
        this.yChunkRange = yR;
    }
}
