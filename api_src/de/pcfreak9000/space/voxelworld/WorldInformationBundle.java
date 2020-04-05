package de.pcfreak9000.space.voxelworld;

public class WorldInformationBundle {

    private final TileWorld tileWorld;
    private final Background background;

    public WorldInformationBundle(TileWorld tileWorld, Background background) {
        this.tileWorld = tileWorld;
        this.background = background;
    }

    public TileWorld getTileWorld() {
        return this.tileWorld;
    }

    public Background getBackground() {
        return this.background;
    }

}
