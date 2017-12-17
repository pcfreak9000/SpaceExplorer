package de.pcfreak9000.se2d.planet;

import omnikryptec.gameobject.Sprite;
import omnikryptec.util.EnumCollection.FixedSizeMode;

public class Tile extends Sprite{

	private TileDefinition myDefinition;
	
	public Tile(TileDefinition def) {
		super(def.getTexture());
		setFixedSizeMode(FixedSizeMode.ON);
		setFixedSize(def.getWidth(), def.getWidth());
	}
	
	public TileDefinition getDefinition() {
		return myDefinition;
	}
	
}
