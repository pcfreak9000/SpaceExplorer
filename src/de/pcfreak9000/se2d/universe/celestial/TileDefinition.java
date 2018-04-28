package de.pcfreak9000.se2d.universe.celestial;

public class TileDefinition {

	public static final float TILE_SIZE = 24;
	public static final float TILE_LAYER = 0;

	private String tex;
	private boolean prerenderable = true;

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

	public Tile newTile() {
		return new Tile(this);
	}
	
}
