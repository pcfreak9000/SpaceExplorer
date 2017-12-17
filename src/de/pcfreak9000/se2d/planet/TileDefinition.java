package de.pcfreak9000.se2d.planet;

import omnikryptec.resource.texture.Texture;

public class TileDefinition{
	
	public static final float TILE_SIZE = 64;
	
	private Texture tex;
	private boolean prerenderable=true;
	
	public TileDefinition(Texture t) {
		this.tex = t;
	}
	
	public Texture getTexture() {
		return tex;
	}
	
	public boolean isPrerenderable() {
		return prerenderable;
	}
}
