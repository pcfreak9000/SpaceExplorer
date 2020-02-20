package de.pcfreak9000.space.voxelworld;

public class TileWorld {
    
    private int width;
    private int height;
    
    public TileWorld(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    public int getTilesWidth() {
        return this.width;
    }
    
    public int getTilesHeight() {
        return this.height;
    }
    
}
