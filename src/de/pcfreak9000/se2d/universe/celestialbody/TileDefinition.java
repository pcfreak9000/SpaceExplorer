package de.pcfreak9000.se2d.universe.celestialbody;

import omnikryptec.resource.texture.Texture;

public class TileDefinition {

	public static final float TILE_SIZE = 24;
	public static final float TILE_LAYER = 0;

	private Texture tex;
	private boolean prerenderable = true;

	public TileDefinition(Texture t) {
		this(t, true);
	}

	public TileDefinition(Texture t, boolean prerenderable) {
		this.tex = t;
	}

	public Texture getTexture() {
		return tex;
	}

	public boolean isPrerenderable() {
		return prerenderable;
	}

}
