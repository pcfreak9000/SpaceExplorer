package de.pcfreak9000.space.voxelworld;

public class WorldInformationBundle {
    
    private TileWorld tileWorld;
    private Background background;
    
    public WorldInformationBundle(TileWorld tileWorld, Background background) {
        this.tileWorld = tileWorld;
        this.background = background;
    }
    
    public TileWorld getTileWorld() {
        return tileWorld;
    }
    
    public Background getBackground() {
        return background;
    }
    
}
