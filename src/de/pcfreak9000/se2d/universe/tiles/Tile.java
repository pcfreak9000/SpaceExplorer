package de.pcfreak9000.se2d.universe.tiles;

import de.pcfreak9000.se2d.game.core.GameRegistry;
import omnikryptec.gameobject.Sprite;
import omnikryptec.resource.loader.ResourceLoader;
import omnikryptec.util.EnumCollection.FixedSizeMode;
import omnikryptec.util.Maths;

public class Tile extends Sprite {

	private TileDefinition myDefinition;

	private boolean validPosition = true;

	public Tile(TileDefinition def) {
		super(ResourceLoader.MISSING_TEXTURE);
		GameRegistry.getTileRegistry().checkRegistered(def);
		myDefinition = def;
		setFixedSizeMode(FixedSizeMode.ON);
		setFixedSize(TileDefinition.TILE_SIZE, TileDefinition.TILE_SIZE);
		setLayer(TileDefinition.TILE_LAYER);
	}

	public TileDefinition getDefinition() {
		return myDefinition;
	}

	public long getTileX() {
		return Maths.fastFloor(getTransform().getPosition(true).x / TileDefinition.TILE_SIZE);
	}

	public long getTileY() {
		return Maths.fastFloor(getTransform().getPosition(true).y / TileDefinition.TILE_SIZE);
	}

	public void invalidate() {
		validPosition = false;
	}

	public boolean isValid() {
		return validPosition;
	}

	@Override
	public String toString() {
		return super.toString() + ": " + myDefinition.toString();
	}

}
