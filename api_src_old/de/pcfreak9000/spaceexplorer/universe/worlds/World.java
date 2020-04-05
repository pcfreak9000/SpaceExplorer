package de.pcfreak9000.spaceexplorer.universe.worlds;

import java.util.logging.Logger;

import de.codemakers.base.logger.LogLevel;
import de.omnikryptec.old.main.OmniKryptecEngine;
import de.omnikryptec.old.main.Scene2D;
import de.omnikryptec.old.physics.Dyn4JPhysicsWorld;
import de.pcfreak9000.spaceexplorer.game.core.Player;
import de.pcfreak9000.spaceexplorer.game.launch.SpaceExplorer2D;
import de.pcfreak9000.spaceexplorer.renderer.PlanetRenderer;
import de.pcfreak9000.spaceexplorer.universe.tiles.Tile;
import de.pcfreak9000.spaceexplorer.util.Private;

/**
 * The data structure representing a World of {@link Tile}s and other objects.
 *
 * @author pcfreak9000
 *
 */
public class World {

    private class WorldScene extends Scene2D {

        WorldScene(final String name) {
            super(name, SpaceExplorer2D.getSpaceExplorer2D().getUniverse().getPlanetCamera());
            setRenderer(RENDERER);
            setAmbientColor(0.2f, 0.2f, 0.2f);
            final Dyn4JPhysicsWorld phw = new Dyn4JPhysicsWorld();
            setPhysicsWorld(phw);
            phw.getWorld().getSettings().setStepFrequency(1 / 400.0);
        }

        @Override
        protected void update() {
            final int camX = Chunk.toChunk(getCamera().getTransform().getPosition(true).dx);
            final int camY = Chunk.toChunk(getCamera().getTransform().getPosition(true).dy);
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    generateNeeded(camX + i, camY + j);
                }
            }
        }

    }

    @Private
    public static final PlanetRenderer RENDERER = new PlanetRenderer();

    private final Generatable generator;
    private final WorldScene scene;
    private final Chunk[][] chunks;

    private int chunksSize;

    /**
     *
     * @param name       the name of this Worlds Scene
     * @param generator  the {@link Generatable} of this {@link World}
     * @param tileRadius the positive radius of this {@link World} in {@link Tile}s
     */
    public World(final String name, final Generatable generator, final int tileRadius) {
        this.chunksSize = (int) Math.ceil((double) tileRadius / Chunk.CHUNKSIZE_T);
        this.generator = generator;
        if (this.chunksSize > (Integer.MAX_VALUE >> 1) - 10) {
            // Should not happen
            Logger.log("Planetsize exceeds Integer#MAX_VALUE!", LogLevel.WARNING);
        }
        this.chunksSize <<= 1;
        this.chunks = new Chunk[this.chunksSize][this.chunksSize];
        this.scene = new WorldScene(name);
    }

    /**
     * sets this {@link World} as displayed {@link World}
     *
     * @param p
     */
    public void load(final Player p) {
        OmniKryptecEngine.instance().addAndSetScene(this.scene);
        this.scene.addGameObject(p);
    }

    /**
     * removes this {@link World} from the Display
     *
     * @param p
     */
    public void unload(final Player p) {
        OmniKryptecEngine.instance().setScene2D(null);
        this.scene.removeGameObject(p);
    }

    /**
     *
     * @param cx global chunk x
     * @param cy global chunk y
     * @return a Chunk
     */
    public Chunk getChunk(final int cx, final int cy) {
        if (!inBounds(cx, cy)) {
            return null;
        }
        return this.chunks[cx + (this.chunksSize >> 1)][cy + (this.chunksSize >> 1)];
    }

    /**
     * Is the chunk position contained in the boundaries of this {@link World}?
     *
     * @param cx global chunk x
     * @param cy global chunk y
     * @return boolean
     */
    public boolean inBounds(final int cx, final int cy) {
        return !(cx >= (this.chunksSize >> 1) || cy >= (this.chunksSize >> 1) || cx < -(this.chunksSize >> 1)
                || cy < -(this.chunksSize >> 1));
    }

    /**
     *
     * @param cx global chunk x
     * @param cy global chunk y
     */
    public void generateNeeded(final int cx, final int cy) {
        if (inBounds(cx, cy) && getChunk(cx, cy) == null) {
            final Chunk newChunk = new Chunk(cx, cy);
            this.generator.generateChunk(newChunk);
            newChunk.compile();
            this.chunks[cx + (this.chunksSize >> 1)][cy + (this.chunksSize >> 1)] = newChunk;
            newChunk.addTo(this.scene);
        }
    }

    public int getChunkRadius() {
        return this.chunksSize >> 1;
    }

}
