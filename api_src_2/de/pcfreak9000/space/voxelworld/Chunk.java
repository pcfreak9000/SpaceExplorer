package de.pcfreak9000.space.voxelworld;

import de.pcfreak9000.space.world.tile.Tile;

public class Chunk {
    
    public static final int CHUNK_TILE_SIZE = 64;
    
    private Tile[][] tiles;
    
    public Chunk(int size) {
        this.tiles = new Tile[size][size];
    }
    
}
