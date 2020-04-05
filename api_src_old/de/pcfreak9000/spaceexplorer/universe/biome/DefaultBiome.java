package de.pcfreak9000.spaceexplorer.universe.biome;

import de.pcfreak9000.space.core.GameRegistry;
import de.pcfreak9000.spaceexplorer.universe.tiles.Tile;
import de.pcfreak9000.spaceexplorer.universe.tiles.TileDefinition;
import de.pcfreak9000.spaceexplorer.universe.worlds.Chunk;
import de.pcfreak9000.spaceexplorer.universe.worlds.Generatable;
import de.pcfreak9000.spaceexplorer.util.Private;

@Private
public class DefaultBiome implements BiomeDefinition, Biome {

    @Override
    public Biome getBiome(final long seed) {
        return this;
    }

    @Override
    public float evaluate(final Generatable body) {
        return Float.NEGATIVE_INFINITY;
    }

    @Override
    public boolean likes(final Generatable body) {
        return true;
    }

    @Override
    public TileDefinition getTileDefinition(final int gtx, final int gty) {
        return GameRegistry.MISSING_DEFINITION;
    }

    @Override
    public void decorate(final Chunk c, final Tile tile) {

    }

}
