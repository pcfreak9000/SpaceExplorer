package de.pcfreak9000.se2d.universe.worlds;

import de.pcfreak9000.se2d.game.core.Player;
import de.pcfreak9000.se2d.game.launch.SpaceExplorer2D;
import de.pcfreak9000.se2d.renderer.PlanetRenderer;
import omnikryptec.main.OmniKryptecEngine;
import omnikryptec.main.Scene2D;
import omnikryptec.physics.Dyn4JPhysicsWorld;
import omnikryptec.util.logger.LogLevel;
import omnikryptec.util.logger.Logger;

public class World {

	private class WorldScene extends Scene2D {

		WorldScene(String name) {
			super(name, SpaceExplorer2D.getSpaceExplorer2D().getUniverse().getPlanetCamera());
			setRenderer(RENDERER);
			setAmbientColor(0.2f, 0.2f, 0.2f);
			Dyn4JPhysicsWorld phw = new Dyn4JPhysicsWorld();
			setPhysicsWorld(phw);
			phw.getWorld().getSettings().setStepFrequency(1 / 400.0);
		}

		@Override
		protected void update() {
			int camX = Chunk.toChunk(getCamera().getTransform().getPosition(true).x);
			int camY = Chunk.toChunk(getCamera().getTransform().getPosition(true).y);
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					generateNeeded(camX+i, camY+j);
				}
			}
		}

	}

	public static final PlanetRenderer RENDERER = new PlanetRenderer();

	private ChunkGenerator generator;
	private WorldScene scene;
	private Chunk[][] chunks;

	private int chunksSize;

	public World(String name, ChunkGenerator generator, int tileRadius) {
		this.chunksSize = (int) Math.ceil((double) tileRadius / Chunk.CHUNKSIZE_T);
		this.generator = generator;
		if (chunksSize > (Integer.MAX_VALUE >> 1) - 10) {
			Logger.log("Planetsize exceeds Integer#MAX_VALUE!", LogLevel.WARNING);
		}
		chunksSize <<= 1;
		chunks = new Chunk[chunksSize][chunksSize];
		scene = new WorldScene(name);
	}

	public void load(Player p) {
		OmniKryptecEngine.instance().addAndSetScene(scene);
		scene.addGameObject(p);
	}

	public void unload(Player p) {
		OmniKryptecEngine.instance().setScene2D(null);
		scene.removeGameObject(p);
	}

	public Chunk getChunk(int cx, int cy) {
		if (!inBounds(cx, cy)) {
			return null;
		}
		return chunks[cx + (chunksSize >> 1)][cy + (chunksSize >> 1)];
	}

	public boolean inBounds(int cx, int cy) {
		return !(cx >= (chunksSize >> 1) || cy >= (chunksSize >> 1) || cx < -(chunksSize >> 1)
				|| cy < -(chunksSize >> 1));
	}

	public void generateNeeded(int cx, int cy) {
		if (inBounds(cx, cy) && getChunk(cx, cy) == null) {
			Chunk newChunk = new Chunk(cx, cy);
			generator.generateChunk(newChunk);
			newChunk.compile();
			chunks[cx + (chunksSize >> 1)][cy + (chunksSize >> 1)] = newChunk;
			newChunk.addTo(scene);
		}
	}

	public int getRadius() {
		return chunksSize;
	}

}
