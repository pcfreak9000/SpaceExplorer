package de.pcfreak9000.se2d.coregame.start;

import java.util.List;

import de.pcfreak9000.se2d.universe.Orbit;
import de.pcfreak9000.se2d.universe.biome.Biome;
import de.pcfreak9000.se2d.universe.biome.BiomeDefinition;
import de.pcfreak9000.se2d.universe.celestial.CelestialBody;
import de.pcfreak9000.se2d.universe.celestial.CelestialBodyDefinition;
import de.pcfreak9000.se2d.universe.tiles.Tile;
import de.pcfreak9000.se2d.universe.worlds.Chunk;

public class Planet extends CelestialBody{

	public Planet(CelestialBodyDefinition generator, Orbit orbit, int world_rad, String name, long seed) {
		super(generator, orbit, world_rad, name, seed);
	}

	@Override
	public BiomeDefinition getBiomeDefinition(List<BiomeDefinition> possibilities, int globalTileX, int globalTileY) {
		return possibilities.get(0);
	}

	@Override
	public boolean inBounds(int globalTileX, int globalTileY) {
		return globalTileX*globalTileX+globalTileY*globalTileY<=getTileRadius()*getTileRadius();
	}
	
	@Override
	public void adjustTile(Chunk c, Biome b, Tile t) {
	}

}
