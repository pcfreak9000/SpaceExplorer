package de.pcfreak9000.space.voxelworld;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.omnikryptec.util.math.Mathd;
import de.omnikryptec.util.math.Mathf;
import de.omnikryptec.util.profiling.Profiler;
import de.pcfreak9000.space.voxelworld.tile.Tile;

public class TileWorld {
    
    //in tiles
    private int width;
    private int height;
    
    private final int arrayWidth;
    private final int arrayHeight;
    
    private RegionGenerator generator;
    
    private Region[][] regions;
    
    public TileWorld(int width, int height, RegionGenerator generator) {
        this.width = width;
        this.height = height;
        this.arrayWidth = (int) Mathd.ceil(width / (double) Region.REGION_TILE_SIZE);
        this.arrayHeight = (int) Mathd.ceil(height / (double) Region.REGION_TILE_SIZE);
        this.generator = generator;
        this.regions = new Region[arrayWidth][arrayHeight];
    }
    
    public Region requestRegion(int rx, int ry) {
        if (inRegionBounds(rx, ry)) {
            Region r = regions[rx][ry];
            if (r == null) {
                r = new Region(rx, ry, this);
                generator.generateChunk(r, this);
                regions[rx][ry] = r;
            }
            return r;
        }
        return null;
    }
    
    public Region getRegion(int rx, int ry) {
        if (inRegionBounds(rx, ry)) {
            return regions[rx][ry];
        }
        return null;
    }
    
    public boolean inRegionBounds(int rx, int ry) {
        return rx >= 0 && rx < this.arrayWidth && ry >= 0 && ry < this.arrayHeight;
    }
    
    public boolean inBounds(int tx, int ty) {
        return tx >= 0 && tx < this.width && ty >= 0 && ty < this.height;
    }
    
    public Tile get(int tx, int ty) {
        int rx = Region.toGlobalRegion(tx);
        int ry = Region.toGlobalRegion(ty);
        Region r = requestRegion(rx, ry);
        return r.get(tx, ty);
    }
    
    public void collectTileIntersections(Collection<Tile> output, int x, int y, int w, int h) {
        Profiler.begin("collectTileIntersects");
        boolean xy = inBounds(x, y);
        boolean xwyh = inBounds(x + w, y + h);
        if (!xy && !xwyh) {
            Profiler.end();
            return;
        }
        Set<Region> regions = new HashSet<>();
        if (xy) {
            regions.add(requestRegion(Region.toGlobalRegion(x), Region.toGlobalRegion(y)));
        }
        if (xwyh) {
            regions.add(requestRegion(Region.toGlobalRegion(x + w), Region.toGlobalRegion(y + h)));
        }
        if (inBounds(x + w, y)) {
            regions.add(requestRegion(Region.toGlobalRegion(x + w), Region.toGlobalRegion(y)));
        }
        if (inBounds(x, y + h)) {
            regions.add(requestRegion(Region.toGlobalRegion(x), Region.toGlobalRegion(y + h)));
        }
        for (Region r : regions) {
            r.tileIntersections(output, x, y, w, h);
        }
        Profiler.end();
    }
    
    public int getWorldWidth() {
        return this.width;
    }
    
    public int getWorldHeight() {
        return this.height;
    }
    
}
