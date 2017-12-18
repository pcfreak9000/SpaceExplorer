package de.pcfreak9000.se2d.planet;

import omnikryptec.gameobject.Sprite;
import omnikryptec.util.Maths;
import omnikryptec.util.Color;
import omnikryptec.util.EnumCollection.FixedSizeMode;

public class Tile extends Sprite{

	private TileDefinition myDefinition;
	
	public Tile(TileDefinition def) {
		super(def.getTexture());
		setFixedSizeMode(FixedSizeMode.ON);
		setFixedSize(TileDefinition.TILE_SIZE, TileDefinition.TILE_SIZE);
	}
	
	public TileDefinition getDefinition() {
		return myDefinition;
	}
	
	public long getX() {
		return Maths.fastFloor(getTransform().getPosition(true).x/TileDefinition.TILE_SIZE);
	}
	
	public long getY() {
		return Maths.fastFloor(getTransform().getPosition(true).y/TileDefinition.TILE_SIZE);
	}
}
