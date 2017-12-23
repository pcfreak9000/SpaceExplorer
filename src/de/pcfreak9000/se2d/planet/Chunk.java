package de.pcfreak9000.se2d.planet;

import java.util.ArrayList;
import java.util.Random;

import org.joml.Math;

import de.pcfreak9000.se2d.main.Launcher;
import omnikryptec.gameobject.Sprite;
import omnikryptec.graphics.SpriteBatch;
import omnikryptec.main.Scene2D;
import omnikryptec.renderer.d3.RenderMap;
import omnikryptec.resource.loader.ResourceLoader;
import omnikryptec.resource.texture.Texture;
import omnikryptec.util.Maths;

public class Chunk extends Sprite {

	public static final int CHUNKSIZE_T = 20;
	public static final float CHUNKSIZE = CHUNKSIZE_T * TileDefinition.TILE_SIZE;

	private RenderMap<Texture, float[]> data = new RenderMap<>(Texture.class);
	private int x, y;

	public Chunk(int x, int y) {
		getTransform().setPosition(x*CHUNKSIZE+000.1f, y*CHUNKSIZE+000.1f);
		this.x = x;
		this.y = y;
	}

	private RenderMap<Texture, ArrayList<Tile>> tiles = new RenderMap<>(Texture.class);
	private Tile[][] array = new Tile[CHUNKSIZE_T][CHUNKSIZE_T];
	private ArrayList<Sprite> others = new ArrayList<>();

	private TileDefinition TMP_T = new TileDefinition(ResourceLoader.currentInstance().getTexture("grassy.png"));

	public Chunk generate(Random random, long maxr, long fader) {
		maxr *= TileDefinition.TILE_SIZE;
		fader *= TileDefinition.TILE_SIZE;
		float tx, ty, txw, tyw, distancesq, randfl;
		Tile tile;
		for (int x = 0; x < CHUNKSIZE_T; x++) {
			for (int y = 0; y < CHUNKSIZE_T; y++) {
				tile = new Tile(TMP_T);
				tx = this.x * CHUNKSIZE + x * TileDefinition.TILE_SIZE;
				ty = this.y * CHUNKSIZE + y * TileDefinition.TILE_SIZE;
				txw = tx - TileDefinition.TILE_SIZE / 2;
				tyw = ty - TileDefinition.TILE_SIZE / 2;
				if (txw * txw + tyw * tyw > maxr * maxr) {
					continue;
				}
				if (txw * txw + tyw * tyw > fader * fader) {
					tile.invalidate();
					distancesq = 1 - ((float) Math.sqrt(txw * txw + tyw * tyw) - (fader)) / (maxr - fader);
					randfl = random.nextFloat();
					if (randfl * distancesq <= e(distancesq)) {
						tile.getColor().set(1, 1, 1, randfl * distancesq * distancesq * distancesq);
					} else {
						tile.getColor().setAll(1);
					}
				}
				tile.getTransform().setPosition(tx, ty);
				array[x][y] = tile;
				if (tile.getDefinition().isPrerenderable()) {
					if (tiles.get(tile.getTexture()) == null) {
						tiles.put(tile.getTexture(), new ArrayList<>());
					}
					tiles.get(tile.getTexture()).add(tile);
				} else {
					others.add(tile);
				}
			}
		}
		int max=100;
		for (int i = 0; i < 30; i++) {
			float x = random.nextFloat() * CHUNKSIZE;
			float y = random.nextFloat() * CHUNKSIZE;
			Tile t = array[(int) (x / TileDefinition.TILE_SIZE)][(int) (y / TileDefinition.TILE_SIZE)];
			if (t!=null&&t.isValid()) {
				Sprite sprite = new Sprite(ResourceLoader.currentInstance().getTexture("treetest.png"));
				sprite.getTransform().setPosition(x+this.x*CHUNKSIZE, y+this.y*CHUNKSIZE);
				sprite.setLayer(1);
				others.add(sprite);
			} else {
				max--;
				if(max<0) {
					break;
				}
				i--;
			}
		}
		return this;
	}

	private float e(float x) {
		if (x >= 1) {
			return 0;
		}
		return (float) java.lang.Math.pow(Maths.E, -(x * x))*1.5f;
	}

	
	public Tile getTile(int crtx, int crty) {
		return array[crtx][crty];
	}

	public Chunk preRender() {
		SpriteBatch batch = new SpriteBatch(getSize(tiles));
		batch.begin(true);
		for (Texture t : tiles.keysArray()) {
			for (Tile tile : tiles.get(t)) {
				batch.draw(tile);
			}
			data.put(t, batch.getData());
		}
		batch.end();
		return this;
	}

	private int getSize(RenderMap<Texture, ArrayList<Tile>> tiles) {
		int fsize = 0;
		for (Texture t : tiles.keysArray()) {
			fsize += tiles.get(t).size();
		}
		return fsize;
	}

	public Chunk addTo(Scene2D s) {
		s.addGameObject(this);
		for (Sprite sc : others) {
			s.addGameObject(sc);
		}
		return this;
	}

	public Chunk removeFrom(Scene2D s) {
		s.removeGameObject(this);
		for (Sprite sc : others) {
			s.addGameObject(sc);
		}
		return this;
	}

	@Override
	public void paint(SpriteBatch batch) {
		batch.color().set(1, 1, 1, 1);
		for (Texture t : data.keysArray()) {
			t.bindToUnitOptimized(0);
			batch.drawPolygon(t, data.get(t), data.get(t).length / SpriteBatch.FLOATS_PER_VERTEX);
		}
		if(Launcher.DEBUG) {
			batch.drawRect(x*CHUNKSIZE, y*CHUNKSIZE, CHUNKSIZE, CHUNKSIZE);
		}
	}

	@Override
	public float getWidth() {
		return CHUNKSIZE;
	}
	
	@Override
	public float getHeight() {
		return CHUNKSIZE;
	}
	
	public static final int toChunk(float f) {
		return (int) Maths.fastFloor(f/CHUNKSIZE);
	}
	
}
