package de.pcfreak9000.spaceexplorer.universe.celestial;

import java.util.List;
import java.util.stream.Collectors;

import de.pcfreak9000.space.core.registry.GameRegistry;
import de.pcfreak9000.spaceexplorer.universe.Orbit;
import de.pcfreak9000.spaceexplorer.universe.biome.Biome;
import de.pcfreak9000.spaceexplorer.universe.biome.BiomeDefinition;
import de.pcfreak9000.spaceexplorer.universe.tiles.Tile;
import de.pcfreak9000.spaceexplorer.universe.tiles.TileDefinition;
import de.pcfreak9000.spaceexplorer.universe.worlds.Chunk;
import de.pcfreak9000.spaceexplorer.universe.worlds.Generatable;
import de.pcfreak9000.spaceexplorer.universe.worlds.World;

/**
 * Represents all kinds of CelestialBodys
 *
 * @author pcfreak9000
 *
 */
public class CelestialBody extends Generatable {

    private final CelestialBodyDefinition generator;
    private final Orbit orbit;
    private World world;

    private final String name;
    private final long seed;
    private final int tileRadius;

    /**
     *
     * @param generator   the CBs generating {@link CelestialBodyDefinition}
     * @param orbit       instance of {@link Orbit} containing information about
     *                    this CBs orbit
     * @param worldRadius the biggest radius of the {@link World}, negative values
     *                    and zero will result in a non-visitable CB
     * @param name        the CBs name
     * @param seed        a seed for this CB at this position
     */
    public CelestialBody(final CelestialBodyDefinition generator, final Orbit orbit, final int worldRadius,
            final String name, final long seed) {
        GameRegistry.getCelestialBodyRegistry().checkRegistered(generator);
        this.generator = generator;
        this.orbit = orbit;
        this.name = name;
        this.seed = seed;
        this.tileRadius = worldRadius;
        if (this.tileRadius > 0) {
            this.world = new World(name, this, worldRadius);
        }
    }

    public Orbit getOrbit() {
        return this.orbit;
    }

    public World getWorld() {
        return this.world;
    }

    public String getName() {
        return this.name;
    }

    public long getSeed() {
        return this.seed;
    }

    public int getTileRadius() {
        return this.tileRadius;
    }

    public CelestialBodyDefinition getDefinition() {
        return this.generator;
    }

    public boolean isVisitable() {
        return this.world != null;
    }

    @Override
    public void generateChunk(final Chunk c) {
        if (!isVisitable()) {
            throw new IllegalStateException("The CelestialBody " + this.getClass().getName()
                    + " is not visitable so it should not be generated!");
        }
        for (int x = 0; x < Chunk.CHUNKSIZE_T; x++) {
            for (int y = 0; y < Chunk.CHUNKSIZE_T; y++) {
                final int globalTileX = x + c.getChunkX() * Chunk.CHUNKSIZE_T;
                final int globalTileY = y + c.getChunkY() * Chunk.CHUNKSIZE_T;
                if (inBounds(globalTileX, globalTileY)) {
                    final Biome biome = getBiomeDefinition(this.generator.getBiomeDefinitions().stream()
                            .filter((def) -> def.likes(this)).collect(Collectors.toList()), globalTileX, globalTileY)
                                    .getBiome(this.seed);
                    final Tile tile = biome.getTileDefinition(globalTileX, globalTileY).newTile(globalTileX,
                            globalTileY);
                    tile.getTransform().setPosition(globalTileX * TileDefinition.TILE_SIZE,
                            globalTileY * TileDefinition.TILE_SIZE);
                    adjustTile(c, biome, tile);
                    c.addTile(tile, x, y);
                    if (tile.isValid()) {
                        biome.decorate(c, tile);
                    }
                }
            }
        }
    }

    /**
     * For the same position the same {@link BiomeDefinition} must be returned for
     * this CelestialBody.
     *
     * @param possibilities possible BiomeDefinitions
     * @param globalTileX   global tile x
     * @param globalTileY   global tile y
     * @return a {@link BiomeDefinition}
     */
    public BiomeDefinition getBiomeDefinition(final List<BiomeDefinition> possibilities, final int globalTileX,
            final int globalTileY) {
        return possibilities.get(0);
    }

    /**
     * Is the position in the boundaries of the {@link CelestialBody}?
     *
     * @param globalTileX global tile x
     * @param globalTileY global tile y
     * @return boolean
     */
    public boolean inBounds(final int globalTileX, final int globalTileY) {
        return true;
    }

    /**
     * e.g. invalidate the {@link Tile}
     *
     * @param c the tile's {@link Chunk}
     * @param b the tile's {@link Biome}
     * @param t the tile itself
     */
    public void adjustTile(final Chunk c, final Biome b, final Tile t) {
    }

    @Override
    public String toString() {
        return "CB " + this.getClass().getSimpleName() + ", name=\"" + this.name + "\", r=" + this.tileRadius + "ts; "
                + this.generator.toString() + ", s=" + this.seed;
    }
}
