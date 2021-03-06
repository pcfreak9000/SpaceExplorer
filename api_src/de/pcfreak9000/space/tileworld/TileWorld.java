package de.pcfreak9000.space.tileworld;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import de.omnikryptec.util.math.Mathd;
import de.omnikryptec.util.profiling.Profiler;
import de.pcfreak9000.space.tileworld.tile.Tile;
import de.pcfreak9000.space.tileworld.tile.TileState;

public class TileWorld {
    
    //in tiles
    private final int width;
    private final int height;
    
    private final int arrayWidth;
    private final int arrayHeight;
    
    private final RegionGenerator generator;
    
    private final Region[][] regions;
    
    private boolean wrapsAround = true;
    
    public TileWorld(int width, int height, RegionGenerator generator) {
        this.width = width;
        this.height = height;
        this.arrayWidth = (int) Mathd.ceil(width / (double) Region.REGION_TILE_SIZE);
        this.arrayHeight = (int) Mathd.ceil(height / (double) Region.REGION_TILE_SIZE);
        this.generator = generator;
        this.regions = new Region[this.arrayWidth][this.arrayHeight];
    }
    
    public Region requestRegion(int rx, int ry) {
        if (inRegionBounds(rx, ry)) {
            Region r = this.regions[rx][ry];
            if (r == null) {
                r = new Region(rx, ry, this);
                this.regions[rx][ry] = r;
                this.generator.generateChunk(r, this);
            }
            return r;
        }
        return null;
    }
    
    public Region getRegion(int rx, int ry) {
        if (inRegionBounds(rx, ry)) {
            return this.regions[rx][ry];
        }
        return null;
    }
    
    public boolean inRegionBounds(int rx, int ry) {
        return rx >= 0 && rx < this.arrayWidth && ry >= 0 && ry < this.arrayHeight;
    }
    
    public boolean inBounds(int tx, int ty) {
        return tx >= 0 && tx < this.width && ty >= 0 && ty < this.height;
    }
    
    public Tile getTile(int tx, int ty) {
        int rx = Region.toGlobalRegion(tx);
        int ry = Region.toGlobalRegion(ty);
        Region r = requestRegion(rx, ry);
        return r == null ? null : r.getTile(tx, ty);//Meh
    }
    
    public void setTile(Tile tile, int tx, int ty) {
        int rx = Region.toGlobalRegion(tx);
        int ry = Region.toGlobalRegion(ty);
        Region r = requestRegion(rx, ry);
        if (r != null) {
            r.setTile(tile, tx, ty);
        }
    }
    
    public void collectTileIntersections(Collection<TileState> output, int x, int y, int w, int h, Predicate<TileState> predicate) {
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
            r.tileIntersections(output, x, y, w, h, predicate);
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
