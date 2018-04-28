package de.pcfreak9000.se2d.universe.celestial;

import java.util.ArrayList;

import de.pcfreak9000.se2d.game.launch.Launcher;
import omnikryptec.gameobject.Sprite;
import omnikryptec.graphics.SpriteBatch;
import omnikryptec.main.Scene2D;
import omnikryptec.renderer.d3.RenderMap;
import omnikryptec.resource.texture.Texture;
import omnikryptec.util.Maths;

public class Chunk extends Sprite {

	public static final int CHUNKSIZE_T = 53;
	public static final float CHUNKSIZE = CHUNKSIZE_T * TileDefinition.TILE_SIZE;

	private Tile[][] array = new Tile[CHUNKSIZE_T][CHUNKSIZE_T];
	private RenderMap<Texture, float[]> data = new RenderMap<>(Texture.class);

	private RenderMap<Texture, ArrayList<Tile>> tiles = new RenderMap<>(Texture.class);
	private ArrayList<Sprite> others = new ArrayList<>();

	private int cx, cy;
	private boolean compiled = false;

	public Chunk(int x, int y) {
		getTransform().setPosition(x * CHUNKSIZE, y * CHUNKSIZE);
		this.cx = x;
		this.cy = y;
	}

	public Tile getTile(int crtx, int crty) {
		return array[crtx][crty];
	}

	public void addTile(Tile tile, int crtx, int crty) {
		if (compiled) {
			throw new ChunkCompilationStatusException("Can't add tiles to an already compiled Chunk!");
		}
		array[crtx][crty] = tile;
		if (tile.getDefinition().isPrerenderable()) {
			if (tiles.get(tile.getTexture()) == null) {
				tiles.put(tile.getTexture(), new ArrayList<>());
			}
			tiles.get(tile.getTexture()).add(tile);
		} else {
			others.add(tile);
		}
	}

	public void compile() {
		if (!compiled) {
			compiled = true;
			SpriteBatch batch = new SpriteBatch(getSize(tiles));
			batch.begin(true);
			for (Texture t : tiles.keysArray()) {
				for (Tile tile : tiles.get(t)) {
					batch.draw(tile);
				}
				data.put(t, batch.getData());
			}
			batch.end();
			tiles.clear();
		}
	}

	public boolean isCompiled() {
		return compiled;
	}

	private int getSize(RenderMap<Texture, ArrayList<Tile>> tiles) {
		int fsize = 0;
		for (Texture t : tiles.keysArray()) {
			fsize += tiles.get(t).size();
		}
		return fsize;
	}

	public Chunk addTo(Scene2D s) {
		if (!compiled) {
			throw new ChunkCompilationStatusException("Can't add an uncompiled Chunk!");
		}
		s.addGameObject(this);
		for (Sprite sc : others) {
			s.addGameObject(sc);
		}
		return this;
	}

	public Chunk removeFrom(Scene2D s) {
		if (!compiled) {
			throw new ChunkCompilationStatusException("Can't remove an uncompiled Chunk!");
		}
		s.removeGameObject(this);
		for (Sprite sc : others) {
			s.addGameObject(sc);
		}
		return this;
	}

	@Override
	public void paint(SpriteBatch batch) {
		if (!compiled) {
			throw new ChunkCompilationStatusException("Can't paint an uncompiled Chunk!");
		}
		batch.color().set(1, 1, 1, 1);
		for (Texture t : data.keysArray()) {
			// t.bindToUnitOptimized(0); <- everything explodes!
			batch.drawPolygon(t, data.get(t), data.get(t).length / SpriteBatch.FLOATS_PER_VERTEX);
		}
		if (Launcher.DEBUG) {
			batch.drawRect(cx * CHUNKSIZE, cy * CHUNKSIZE, CHUNKSIZE, CHUNKSIZE);
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

	public int getChunkX() {
		return cx;
	}
	
	public int getChunkY() {
		return cy;
	}
	
	public static final int toChunk(float f) {
		return (int) Maths.fastFloor(f / CHUNKSIZE);
	}

	public static final int tileToChunk(int t) {
		return (int) Maths.fastFloor(t / (float) CHUNKSIZE_T);
	}

	// public Chunk generate(Random random, Planet planet) {
	// float maxr = planet.getPlanetData().getMaxRadius() *
	// TileDefinition.TILE_SIZE;
	// float fader = planet.getPlanetData().getFadeRadius() *
	// TileDefinition.TILE_SIZE;
	// int countvalid = 0;
	// // line(true, 0, random, planet, maxr, fader);
	// for (int x = 0; x < CHUNKSIZE_T; x++) {
	// for (int y = 0; y < CHUNKSIZE_T; y++) {
	// placeTile(x, y, random, planet, maxr, fader);
	// }
	// }
	// validratio = countvalid / (double) (CHUNKSIZE_T * CHUNKSIZE_T);
	// // int max = 100;
	// // for (int i = 0; i < 30 * validratio; i++) {
	// // float x = random.nextFloat() * CHUNKSIZE;
	// // float y = random.nextFloat() * CHUNKSIZE;
	// // Tile t = array[(int) (x / TileDefinition.TILE_SIZE)][(int) (y /
	// // TileDefinition.TILE_SIZE)];
	// // if (t != null && t.isValid()) {
	// // Sprite sprite = new
	// // Sprite(ResourceLoader.currentInstance().getTexture("treetest.png"));
	// // sprite.getTransform().setPosition(x + this.x * CHUNKSIZE, y + this.y *
	// // CHUNKSIZE);
	// // sprite.setLayer(1);
	// // sprite.setColor(new Color(1, 1, 1, 0.9f));
	// // others.add(sprite);
	// // AdvancedBody body = new AdvancedBody().setOffsetXY(-sprite.getWidth() / 2
	// +
	// // 20, 5);
	// // body.getTransform()
	// //
	// .setTranslation(ConverterUtil.convertToPhysics2D(sprite.getTransform().getPosition(true)));
	// // body.addFixture(new AdvancedRectangle(20f, 8f));
	// // sprite.addComponent(new PhysicsComponent2D(body));
	// // } else {
	// // max--;
	// // if (max < 0) {
	// // break;
	// // }
	// // i--;
	// // }
	// // }
	// return this;
	// }
	//
	// private void line(boolean incrY, int n, Random random, Planet planet, float
	// maxr, float fader) {
	// for (int i = 0; i < CHUNKSIZE_T; i++) {
	// placeTile(incrY ? n : i, incrY ? i : n, random, planet, maxr, fader);
	// }
	// }
	//
	// private void placeTile(int x, int y, Random random, Planet planet, float
	// maxr, float fader) {
	// float randfl, distancesq;
	// // Tile coords
	// int tx = this.x * CHUNKSIZE_T + x;
	// int ty = this.y * CHUNKSIZE_T + y;
	// // World coords and world coords in the middle of the tile
	// float wx = tx * TileDefinition.TILE_SIZE;
	// float wy = ty * TileDefinition.TILE_SIZE;
	// float txwh = wx + TileDefinition.TILE_SIZE / 2;
	// float tywh = wy + TileDefinition.TILE_SIZE / 2;
	// if (txwh * txwh + tywh * tywh > maxr * maxr) {
	// return;
	// }
	// // get BiomeDefinition for this tile
	// BiomeDefinition biomedef = checkNeighbours(planet, tx, ty);
	// Tile tile = new Tile(biomedef.getTileDefinition(planet.getPlanetData(), tx,
	// ty), biomedef);
	// // tile is to be faded?
	// if (txwh * txwh + tywh * tywh > fader * fader) {
	// // on this tile no decoration is allowed
	// tile.invalidate();
	// distancesq = 1 - ((float) Math.sqrt(txwh * txwh + tywh * tywh) - (fader)) /
	// (maxr - fader);
	// randfl = random.nextFloat();
	// if (randfl * distancesq <= e(distancesq)) {
	// tile.getColor().set(1, 1, 1, randfl * distancesq * distancesq * distancesq);
	// } else {
	// tile.getColor().setAll(1);
	// }
	// }
	// tile.getTransform().setPosition(wx, wy);
	// array[x][y] = tile;
	// if (tile.getDefinition().isPrerenderable()) {
	// if (tiles.get(tile.getTexture()) == null) {
	// tiles.put(tile.getTexture(), new ArrayList<>());
	// }
	// tiles.get(tile.getTexture()).add(tile);
	// } else {
	// others.add(tile);
	// }
	// }
	//
	// private BiomeDefinition checkNeighbours(Planet planet, int i, int j) {
	// // BiomeDefinition[] defs = new BiomeDefinition[4];
	// // defs[0] = planet.getTile(i - 1, j) == null ? null : planet.getTile(i - 1,
	// // j).getBiome();
	// // defs[1] = planet.getTile(i + 1, j) == null ? null : planet.getTile(i + 1,
	// // j).getBiome();
	// // defs[2] = planet.getTile(i, j + 1) == null ? null : planet.getTile(i, j +
	// // 1).getBiome();
	// // defs[3] = planet.getTile(i, j - 1) == null ? null : planet.getTile(i, j -
	// // 1).getBiome();
	// // // if neighbour has a biome and that is applicable for T(i, j) that biome
	// // will
	// // // be used.
	// // //System.out.println(planet.getTile(i, j-1));
	// // for (BiomeDefinition d : defs) {
	// // if (d != null) {
	// // if (d.isFlagSet(BiomeRegistry.ENVIRONMENT_UNSENSITIVE) ||
	// // d.likes(planet.getPlanetData(), i, j)) {
	// // return d;
	// // }
	// // }
	// // }
	// // System.out.println("sdfsdfsdfsdfsdfsdfsdfsdfsdf");
	// // no matching biomes around T(i, j) found, get a new one
	// return BiomeRegistry.getBiomeDefinition(planet.getPlanetData(), i, j);
	// }
	//
	// private float e(float x) {
	// if (x >= 1) {
	// return 0;
	// }
	// return (float) java.lang.Math.pow(Maths.E, -(x * x)) * 1.5f;
	// }

}
