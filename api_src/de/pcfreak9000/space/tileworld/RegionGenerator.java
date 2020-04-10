package de.pcfreak9000.space.tileworld;

/**
 * Generates regions of a TileWorld.
 *
 * @author pcfreak9000
 *
 */
public interface RegionGenerator {

    void generateChunk(Region region, TileWorld tileWorld);

}
