package de.pcfreak9000.se2d.universe.biome;

import de.pcfreak9000.se2d.game.core.GameRegistry;
import de.pcfreak9000.se2d.universe.celestial.CelestialBody;
import de.pcfreak9000.se2d.universe.tiles.Tile;
import de.pcfreak9000.se2d.universe.tiles.TileDefinition;
import de.pcfreak9000.se2d.universe.worlds.Chunk;
import de.pcfreak9000.se2d.util.Private;

@Private
public class DefaultBiome implements BiomeDefinition, Biome {

	@Override
	public Biome getBiome(long seed) {
		return this;
	}

	@Override
	public float evaluate(CelestialBody body) {
		return Float.NEGATIVE_INFINITY;
	}

	@Override
	public boolean likes(CelestialBody body) {
		return true;
	}

	@Override
	public TileDefinition getTileDefinition(int gtx, int gty) {
		return GameRegistry.MISSING_DEFINITION;
	}

	@Override
	public void decorate(Chunk c, Tile tile) {

	}

}
