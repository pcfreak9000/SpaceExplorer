package de.pcfreak9000.space.voxelworld;

import de.omnikryptec.util.math.Mathf;

public class TileWorld {
    
    //in tiles
    private int width;
    private int height;
    
    private RegionGenerator generator;
    
    private Region[][] regions;
    
    public TileWorld(int width, int height, RegionGenerator generator) {
        this.width = width;
        this.height = height;
        this.generator = generator;
        this.regions = new Region[(int) Mathf.ceil(width / (float) Region.REGION_TILE_SIZE)][(int) Mathf
                .ceil(height / (float) Region.REGION_TILE_SIZE)];
    }
    
    public Region requestRegion(int rx, int ry) {
        if (inRegionBounds(rx, ry)) {
            Region r = regions[rx][ry];
            if (r == null) {
                r = new Region(rx, ry);
                generator.generateChunk(r);
                regions[rx][ry] = r;
            }
            return r;
        }
        return null;
    }
    
    private boolean inRegionBounds(int rx, int ry) {
        return rx >= 0 && rx < (int) Mathf.ceil(width / (float) Region.REGION_TILE_SIZE) && ry >= 0
                && ry < (int) Mathf.ceil(height / (float) Region.REGION_TILE_SIZE);
    }
    
    public boolean inBounds(int tx, int ty) {
        return tx >= 0 && tx < this.width && ty >= 0 && ty < this.height;
    }
    
    public int getWorldWidth() {
        return this.width;
    }
    
    public int getWorldHeight() {
        return this.height;
    }
    
}
