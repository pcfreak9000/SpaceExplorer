package de.pcfreak9000.se2d.universe.biome;

import de.pcfreak9000.se2d.universe.worlds.Tile;
import de.pcfreak9000.se2d.universe.worlds.TileDefinition;

public interface Biome {

	/**
	 * For the same position always the same Tile mustr be found.
	 * 
	 * @param gtx
	 * @param gty
	 * @return
	 */
	public TileDefinition getTileDefinition(int gtx, int gty);

	public void decorate(Tile tile);

}
