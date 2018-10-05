package de.pcfreak9000.spaceexplorer.universe.biome;

import de.pcfreak9000.spaceexplorer.game.core.GameRegistry;
import de.pcfreak9000.spaceexplorer.universe.tiles.Tile;
import de.pcfreak9000.spaceexplorer.universe.tiles.TileDefinition;
import de.pcfreak9000.spaceexplorer.universe.worlds.Chunk;
import de.pcfreak9000.spaceexplorer.universe.worlds.Generatable;
import de.pcfreak9000.spaceexplorer.util.Private;

@Private
public class DefaultBiome implements BiomeDefinition, Biome {

	@Override
	public Biome getBiome(long seed) {
		return this;
	}

	@Override
	public float evaluate(Generatable body) {
		return Float.NEGATIVE_INFINITY;
	}

	@Override
	public boolean likes(Generatable body) {
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
