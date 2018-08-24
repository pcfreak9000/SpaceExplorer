package de.pcfreak9000.se2d.universe.tiles;

import de.omnikryptec.gameobject.Sprite;
import de.omnikryptec.resource.loader.ResourceLoader;
import de.omnikryptec.util.EnumCollection.FixedSizeMode;
import de.pcfreak9000.se2d.game.core.GameRegistry;
import de.pcfreak9000.se2d.universe.celestial.CelestialBody;
import de.pcfreak9000.se2d.universe.worlds.World;

/**
 * the class representing a Tile in the {@link World}
 * 
 * @author pcfreak9000
 *
 */
public class Tile extends Sprite {

	private TileDefinition myDefinition;

	private boolean validPosition = true;
	private int gtx, gty;

	/**
	 * Constructs a new Tile.That is usually done in
	 * {@link TileDefinition#newTile(int, int)}
	 * 
	 * @param def the Tile's {@link TileDefinition}
	 * @param gtx global tile x
	 * @param gty global tile y
	 */
	public Tile(TileDefinition def, int gtx, int gty) {
		super(ResourceLoader.MISSING_TEXTURE);
		this.gtx = gtx;
		this.gty = gty;
		GameRegistry.getTileRegistry().checkRegistered(def);
		myDefinition = def;
		setUpdateType(def.getUpdateType());
		setFixedSizeMode(FixedSizeMode.ON);
		setFixedSize(TileDefinition.TILE_SIZE, TileDefinition.TILE_SIZE);
		setLayer(TileDefinition.TILE_LAYER);
	}

	public TileDefinition getDefinition() {
		return myDefinition;
	}

	/**
	 * Only during the generation of a {@link World}. Usually, an invalid Tile does
	 * not support decoration (e.g. this can be used for the edge of a
	 * {@link CelestialBody})
	 */
	public void invalidate() {
		validPosition = false;
	}

	public boolean isValid() {
		return validPosition;
	}

	public int getTileX() {
		return gtx;
	}

	public int getTileY() {
		return gty;
	}

	@Override
	public String toString() {
		return super.toString() + ": " + myDefinition.toString();
	}

}
