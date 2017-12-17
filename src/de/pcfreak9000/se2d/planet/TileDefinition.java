package de.pcfreak9000.se2d.planet;

import omnikryptec.resource.texture.Texture;

public class TileDefinition{
	
	public static final float DEFAULT_TILE_SIZE = 16;
	
	private Texture tex;
	private float width = DEFAULT_TILE_SIZE;
	private float height = DEFAULT_TILE_SIZE;
	private boolean prerenderable=true;
	
	public TileDefinition(Texture t) {
		this.tex = t;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public Texture getTexture() {
		return tex;
	}
	
	public boolean isPrerenderable() {
		return prerenderable;
	}
}
