package de.pcfreak9000.se2d.universe.tiles;

import omnikryptec.util.EnumCollection.UpdateType;

public class TileDefinition {

	public static final float TILE_SIZE = 24;
	public static final float TILE_LAYER = 0;

	private String tex;
	private boolean prerenderable = true;
	private UpdateType type = UpdateType.STATIC;

	public TileDefinition(String t) {
		this(t, true);
	}

	public TileDefinition(String t, boolean prerenderable) {
		this.tex = t;
	}

	public String getTexture() {
		return tex;
	}

	public boolean isPrerenderable() {
		return prerenderable;
	}

	public UpdateType getUpdateType() {
		return type;
	}

	public TileDefinition setUpdateType(UpdateType t) {
		this.type = t;
		return this;
	}

	public Tile newTile(int gtx, int gty) {
		return new Tile(this, gtx, gty);
	}

}
