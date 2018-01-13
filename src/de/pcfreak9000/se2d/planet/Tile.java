package de.pcfreak9000.se2d.planet;

import de.pcfreak9000.se2d.planet.biome.BiomeDefinition;
import omnikryptec.gameobject.Sprite;
import omnikryptec.util.EnumCollection.FixedSizeMode;
import omnikryptec.util.Maths;

public class Tile extends Sprite {

	private TileDefinition myDefinition;
	private BiomeDefinition myBiome;
	private boolean validPosition = true;

	
	public Tile(TileDefinition def, BiomeDefinition biomedef) {
		super(def.getTexture());
		myDefinition = def;
		myBiome = biomedef;
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

	void invalidate() {
		validPosition = false;
	}
	
	public BiomeDefinition getBiome() {
		return myBiome;
	}
	
	public boolean isValid() {
		return validPosition;
	}

}
