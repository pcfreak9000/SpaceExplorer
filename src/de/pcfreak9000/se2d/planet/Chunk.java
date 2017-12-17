package de.pcfreak9000.se2d.planet;

import java.util.ArrayList;
import java.util.Random;

import omnikryptec.gameobject.Sprite;
import omnikryptec.graphics.SpriteBatch;
import omnikryptec.main.Scene2D;
import omnikryptec.renderer.d3.RenderMap;
import omnikryptec.resource.loader.ResourceLoader;
import omnikryptec.resource.texture.Texture;
import omnikryptec.util.Color;

public class Chunk extends Sprite{
	
	
	public static final int CHUNKSIZE_T = 25;
	public static final float CHUNKSIZE = CHUNKSIZE_T*TileDefinition.TILE_SIZE;

	
	private RenderMap<Texture, float[]> data = new RenderMap<>(Texture.class);
	private long x,y;
	
	public Chunk(long x, long y) {
		this.x = x;
		this.y = y;
	}
	
	private RenderMap<Texture, ArrayList<Tile>> tiles = new RenderMap<>(Texture.class);
	
	private TileDefinition TMP_T = new TileDefinition(ResourceLoader.currentInstance().getTexture("violet.png"));
	
	public Chunk generate(Random random, long maxr, long fader) {
		maxr *= TileDefinition.TILE_SIZE;
		fader *= TileDefinition.TILE_SIZE;
		for(int x=0; x<CHUNKSIZE_T; x++) {
			for(int y=0; y<CHUNKSIZE_T; y++) {
				Tile tile = new Tile(TMP_T);
				float tx = this.x + x*TileDefinition.TILE_SIZE;
				float ty = this.y + y*TileDefinition.TILE_SIZE;
				float txw = tx - TileDefinition.TILE_SIZE/2;
				float tyw = ty - TileDefinition.TILE_SIZE/2;

				if(txw*txw+tyw*tyw>maxr*maxr) {
					continue;
				}
				if(txw*txw+tyw*tyw>fader*fader) {
					float distancesq = 1-((float)org.joml.Math.sqrt(txw*txw+tyw*tyw)-(fader))/(maxr-fader);
					tile.setColor(new Color(1, 1, 1, random.nextFloat()*distancesq>e(distancesq)?1:random.nextFloat()*distancesq));
				}

				tile.getTransform().setPosition(tx, ty);
				if(tiles.get(tile.getTexture())==null) {
					tiles.put(tile.getTexture(), new ArrayList<>());
				}
				tiles.get(tile.getTexture()).add(tile);
			}
		}
		return this;
	}
	
	private float e(float x) {
		if(x>=1) {
			return 0;
		}
		return (float) Math.pow(Math.E, -(x*x));
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
	
	private int getSize(RenderMap<Texture, ArrayList<Tile>> tiles) {
		int fsize=0;
		for(Texture t : tiles.keysArray()) {
			fsize+=tiles.get(t).size();
		}
		
		return fsize;
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
