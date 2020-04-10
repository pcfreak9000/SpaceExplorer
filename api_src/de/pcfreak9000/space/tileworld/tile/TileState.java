package de.pcfreak9000.space.tileworld.tile;

import de.omnikryptec.util.Util;
import de.omnikryptec.util.data.Color;

public class TileState {

    
    private final int globalTileX;
    private final int globalTileY;
    
    private final Color light;
    private final Color sunlight;
    private boolean directSun;
    
    private final Tile type;
    
    public TileState(Tile type, int gtx, int gty) {
        this.type = Util.ensureNonNull(type);
        this.light = new Color(0, 0, 0, 1);
        this.sunlight = new Color(0, 0, 0, 1);
        this.globalTileX = gtx;
        this.globalTileY = gty;
    }
    
    public Color light() {
        return this.light;
    }
    
    public Color sunlight() {
        return this.sunlight;
    }
    
    public Tile getTile() {
        return this.type;
    }
    
    public int getGlobalTileX() {
        return this.globalTileX;
    }
    
    public int getGlobalTileY() {
        return this.globalTileY;
    }
    
    public void setDirectSun(boolean b) {
        this.directSun = b;
    }
    
    public boolean isDirectSun() {
        return directSun;
    }
    
    @Override
    public String toString() {
        return String.format("Tile[%s, x=%d, y=%d]", this.type, this.globalTileX, this.globalTileY);
    }
}
