package de.pcfreak9000.se2d.universe.tiles;

import de.pcfreak9000.se2d.game.core.GameRegistry;
import omnikryptec.gameobject.Sprite;
import omnikryptec.resource.loader.ResourceLoader;
import omnikryptec.util.EnumCollection.FixedSizeMode;

public class Tile extends Sprite {

	private TileDefinition myDefinition;

	private boolean validPosition = true;
	private int gtx, gty;

	public Tile(TileDefinition def, int gtx, int gty) {
		super(ResourceLoader.MISSING_TEXTURE);
		this.gtx = gtx;
		this.gty = gty;
		GameRegistry.getTileRegistry().checkRegistered(def);
		myDefinition = def;
		setFixedSizeMode(FixedSizeMode.ON);
		setFixedSize(TileDefinition.TILE_SIZE, TileDefinition.TILE_SIZE);
		setLayer(TileDefinition.TILE_LAYER);
	}

	public TileDefinition getDefinition() {
		return myDefinition;
	}

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
