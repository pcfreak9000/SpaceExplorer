package de.pcfreak9000.se2d.universe.worlds;

import de.pcfreak9000.se2d.game.core.Player;
import de.pcfreak9000.se2d.game.launch.SpaceExplorer2D;
import de.pcfreak9000.se2d.renderer.PlanetRenderer;
import de.pcfreak9000.se2d.universe.tiles.Tile;
import de.pcfreak9000.se2d.util.Private;
import omnikryptec.main.OmniKryptecEngine;
import omnikryptec.main.Scene2D;
import omnikryptec.physics.Dyn4JPhysicsWorld;
import omnikryptec.util.logger.LogLevel;
import omnikryptec.util.logger.Logger;

/**
 * The data structure representing a World of {@link Tile}s and other objects.
 * 
 * @author pcfreak9000
 *
 */
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
					generateNeeded(camX + i, camY + j);
				}
			}
		}

	}

	@Private
	public static final PlanetRenderer RENDERER = new PlanetRenderer();

	private Generatable generator;
	private WorldScene scene;
	private Chunk[][] chunks;

	private int chunksSize;

	/**
	 * 
	 * @param name       the name of this Worlds Scene
	 * @param generator  the {@link Generatable} of this {@link World}
	 * @param tileRadius the positive radius of this {@link World} in {@link Tile}s
	 */
	public World(String name, Generatable generator, int tileRadius) {
		this.chunksSize = (int) Math.ceil((double) tileRadius / Chunk.CHUNKSIZE_T);
		this.generator = generator;
		if (chunksSize > (Integer.MAX_VALUE >> 1) - 10) {
			// Should not happen
			Logger.log("Planetsize exceeds Integer#MAX_VALUE!", LogLevel.WARNING);
		}
		chunksSize <<= 1;
		chunks = new Chunk[chunksSize][chunksSize];
		scene = new WorldScene(name);
	}

	/**
	 * sets this {@link World} as displayed {@link World}
	 * 
	 * @param p
	 */
	public void load(Player p) {
		OmniKryptecEngine.instance().addAndSetScene(scene);
		scene.addGameObject(p);
	}

	/**
	 * removes this {@link World} from the Display
	 * 
	 * @param p
	 */
	public void unload(Player p) {
		OmniKryptecEngine.instance().setScene2D(null);
		scene.removeGameObject(p);
	}

	/**
	 * 
	 * @param cx global chunk x
	 * @param cy global chunk y
	 * @return a Chunk
	 */
	public Chunk getChunk(int cx, int cy) {
		if (!inBounds(cx, cy)) {
			return null;
		}
		return chunks[cx + (chunksSize >> 1)][cy + (chunksSize >> 1)];
	}

	/**
	 * Is the chunk position contained in the boundaries of this {@link World}?
	 * 
	 * @param cx global chunk x
	 * @param cy global chunk y
	 * @return boolean
	 */
	public boolean inBounds(int cx, int cy) {
		return !(cx >= (chunksSize >> 1) || cy >= (chunksSize >> 1) || cx < -(chunksSize >> 1)
				|| cy < -(chunksSize >> 1));
	}

	/**
	 * 
	 * @param cx global chunk x
	 * @param cy global chunk y
	 */
	public void generateNeeded(int cx, int cy) {
		if (inBounds(cx, cy) && getChunk(cx, cy) == null) {
			Chunk newChunk = new Chunk(cx, cy);
			generator.generateChunk(newChunk);
			newChunk.compile();
			chunks[cx + (chunksSize >> 1)][cy + (chunksSize >> 1)] = newChunk;
			newChunk.addTo(scene);
		}
	}

	public int getChunkRadius() {
		return chunksSize >> 1;
	}

}
