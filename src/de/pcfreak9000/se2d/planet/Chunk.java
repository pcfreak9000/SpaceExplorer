package de.pcfreak9000.se2d.planet;

import java.util.ArrayList;
import java.util.HashMap;

import omnikryptec.gameobject.Sprite;
import omnikryptec.graphics.SpriteBatch;
import omnikryptec.main.Scene2D;
import omnikryptec.renderer.d3.RenderMap;
import omnikryptec.resource.texture.Texture;

public class Chunk extends Sprite{

	public static final int CHUNKSIZE = 100;

	
	private RenderMap<Texture, float[]> data = new RenderMap<>(Texture.class);
	private long x,y;
	
	public Chunk(long x, long y) {
		this.x = x;
		this.y = y;
	}
	
	private RenderMap<Texture, Tile> tiles = new RenderMap<>(Texture.class);
	public Chunk generate() {
		for(int x=0; x<CHUNKSIZE; x++) {
			for(int y=0; y<CHUNKSIZE; y++) {
				
			}
		}
		return this;
	}
	
	public Chunk preRender() {
		SpriteBatch batch = new SpriteBatch(tiles.size());
		batch.begin(true);
		
		batch.end();
		return this;
	}
	
	public Chunk add(Scene2D s) {
		s.addGameObject(this);
		return this;
	}
	
	public Chunk remove(Scene2D s) {
		s.removeGameObject(this);
		return this;
	}
	
	@Override
	public void paint(SpriteBatch batch) {
		for(Texture t : data.keysArray()) {
			t.bindToUnitOptimized(0);
			batch.drawPolygon(t, data.get(t), data.get(t).length/SpriteBatch.FLOATS_PER_VERTEX);
		}
	}
	
}
