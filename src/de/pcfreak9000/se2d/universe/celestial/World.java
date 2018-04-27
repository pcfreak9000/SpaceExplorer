package de.pcfreak9000.se2d.universe.celestial;

import omnikryptec.util.logger.LogLevel;
import omnikryptec.util.logger.Logger;

public class World {
	
	private Generator<? extends CelestialBody> generator;
	private Chunk[][] chunks;
	private int chunksSize;

	public World(Generator<? extends CelestialBody> generator, int tileRadius) {
		this.chunksSize = (int) Math.ceil((double) tileRadius / Chunk.CHUNKSIZE_T);
		this.generator = generator;
		if (chunksSize > (Integer.MAX_VALUE >> 1) - 10) {
			Logger.log("Planetsize exceeds Integer#MAX_VALUE!", LogLevel.WARNING);
		}
		chunksSize <<= 1;
		chunks = new Chunk[chunksSize][chunksSize];
	}

	public Chunk getChunk(int cx, int cy) {
		if (cx >= (chunksSize >> 1) || cy >= (chunksSize >> 1) || cx < -(chunksSize >> 1) || cy < -(chunksSize >> 1)) {
			return null;
		}
		// if (chunks[cx + (chunksSize >> 1)][cy + (chunksSize >> 1)] == null) {
		// Chunk chunk = new Chunk(cx, cy);
		// chunks[cx + (chunksSize >> 1)][cy + (chunksSize >> 1)] = chunk;
		// chunk.generate(planetData.getChunkRandom(cx, cy),
		// this).preRender().addTo(planet);
		// }
		return chunks[cx + (chunksSize >> 1)][cy + (chunksSize >> 1)];
	}

	public void generateNeeded(int cx, int cy) {
		if (getChunk(cx, cy) == null) {
			Chunk newChunk = new Chunk(cx, cy);
			generator.populateChunk(c);
		}
	}

	public int getRadius() {
		return chunksSize;
	}
}
