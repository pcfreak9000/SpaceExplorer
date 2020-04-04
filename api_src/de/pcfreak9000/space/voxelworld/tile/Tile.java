package de.pcfreak9000.space.voxelworld.tile;

import de.omnikryptec.util.Util;
import de.omnikryptec.util.data.Color;
import de.omnikryptec.util.math.Mathd;

public class Tile {
    public static final float TILE_SIZE = 16*1.5f;
    
    public static int toGlobalTile(float x) {
        return (int) Mathd.floor(x / (double) TILE_SIZE);
    }
    
    private final int globalTileX;
    private final int globalTileY;
    
    private final Color light;
    
    private final TileType type;
    
    public Tile(TileType type, int gtx, int gty) {
        this.type = Util.ensureNonNull(type);
        this.light = new Color(0,0,0,1);
        this.globalTileX = gtx;
        this.globalTileY = gty;
    }
    
    public Color light() {
        return light;
    }
    
    public TileType getType() {
        return type;
    }
    
    public int getGlobalTileX() {
        return globalTileX;
    }
    
    public int getGlobalTileY() {
        return globalTileY;
    }
    
    @Override
    public String toString() {
        return String.format("Tile[%s, x=%d, y=%d]", this.type, this.globalTileX, this.globalTileY);
    }
}
