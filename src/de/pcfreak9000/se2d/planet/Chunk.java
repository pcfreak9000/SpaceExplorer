package de.pcfreak9000.se2d.planet;

import java.util.ArrayList;

import omnikryptec.gameobject.Camera;
import omnikryptec.gameobject.Sprite;
import omnikryptec.graphics.SpriteBatch;
import omnikryptec.main.Scene2D;
import omnikryptec.renderer.d3.RenderMap;
import omnikryptec.resource.loader.ResourceLoader;
import omnikryptec.resource.texture.Texture;

public class Chunk extends Sprite{

	public static final float CHUNKSIZE = 250*TileDefinition.DEFAULT_TILE_SIZE;

	
	private RenderMap<Texture, float[]> data = new RenderMap<>(Texture.class);
	private long x,y;
	
	public Chunk(long x, long y) {
		this.x = x;
		this.y = y;
	}
	
	private RenderMap<Texture, ArrayList<Tile>> tiles = new RenderMap<>(Texture.class);
	
	private TileDefinition TMP_T = new TileDefinition(ResourceLoader.currentInstance().getTexture("sdfsdfdsf"));
	
	public Chunk generate() {
		for(int x=0; x<CHUNKSIZE; x+=TileDefinition.DEFAULT_TILE_SIZE) {
			for(int y=0; y<CHUNKSIZE; y+=TileDefinition.DEFAULT_TILE_SIZE) {
				Tile tile = new Tile(TMP_T);
				tile.getTransform().setPosition(x+this.x, y+this.y);
				if(tiles.get(tile.getTexture())==null) {
					tiles.put(tile.getTexture(), new ArrayList<>());
				}
				tiles.get(tile.getTexture()).add(tile);
			}
		}
		return this;
	}
	
	public Chunk preRender() {
		SpriteBatch batch = new SpriteBatch(getSize(tiles));
		batch.begin(true);
		for(Texture t : tiles.keysArray()) {
			for(Tile tile : tiles.get(t)) {
				batch.draw(tile);
			}
			data.put(t, batch.getData());
		}
		batch.end();
		return this;
	}
	
	private int getSize(RenderMap<Texture, ArrayList<Tile>> tiles2) {
		int xxx=0;
		for(Texture t : tiles2.keysArray()) {
			xxx+=tiles2.get(t).size();
		}
		
		return xxx;
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
