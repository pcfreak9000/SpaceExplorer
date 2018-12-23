package de.pcfreak9000.spaceexplorer.coregame.start;

import java.util.List;

import de.omnikryptec.util.data.Color;
import de.pcfreak9000.spaceexplorer.universe.Orbit;
import de.pcfreak9000.spaceexplorer.universe.biome.Biome;
import de.pcfreak9000.spaceexplorer.universe.biome.BiomeDefinition;
import de.pcfreak9000.spaceexplorer.universe.celestial.CelestialBody;
import de.pcfreak9000.spaceexplorer.universe.celestial.CelestialBodyDefinition;
import de.pcfreak9000.spaceexplorer.universe.tiles.StaticRectCollider;
import de.pcfreak9000.spaceexplorer.universe.tiles.Tile;
import de.pcfreak9000.spaceexplorer.universe.tiles.TileDefinition;
import de.pcfreak9000.spaceexplorer.universe.worlds.Chunk;

public class Planet extends CelestialBody {
    
    public Planet(final CelestialBodyDefinition generator, final Orbit orbit, final int world_rad, final String name,
            final long seed) {
        super(generator, orbit, world_rad, name, seed);
    }
    
    @Override
    public BiomeDefinition getBiomeDefinition(final List<BiomeDefinition> possibilities, final int globalTileX,
            final int globalTileY) {
        return possibilities.get(0);
    }
    
    @Override
    public boolean inBounds(final int globalTileX, final int globalTileY) {
        return globalTileX * globalTileX + globalTileY * globalTileY <= getTileRadius() * getTileRadius();
    }
    
    @Override
    public void adjustTile(final Chunk c, final Biome b, final Tile t) {
        if (t.getTileX() * t.getTileX() + t.getTileY() * t.getTileY() >= getTileRadius() * getTileRadius()
                - getTileRadius() * 2) {
            t.invalidate();
            t.setColor(new Color(0.5f, 0.5f, 0.5f));
            new StaticRectCollider(TileDefinition.TILE_SIZE, TileDefinition.TILE_SIZE,
                    t.getTransform().getPosition(true)).add();
        }
    }
    
}
