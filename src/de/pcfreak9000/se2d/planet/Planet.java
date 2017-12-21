package de.pcfreak9000.se2d.planet;

import java.time.Instant;
import java.util.Random;

import de.pcfreak9000.se2d.game.Player;
import de.pcfreak9000.se2d.game.SpaceExplorer2D;
import de.pcfreak9000.se2d.renderer.PlanetRenderer;
import omnikryptec.main.OmniKryptecEngine;
import omnikryptec.main.Scene2D;
import omnikryptec.util.logger.LogLevel;
import omnikryptec.util.logger.Logger;

public class Planet {
	
	private class PlanetScene extends Scene2D{
		
		PlanetScene(String name){
			super(name, SpaceExplorer2D.getSpaceExplorer2D().getPlanetCamera());
			setRenderer(RENDERER);
		}
		
		@Override
		protected void update() {
			int camX = Chunk.toChunk(getCamera().getTransform().getPosition(true).x);
			int camY = Chunk.toChunk(getCamera().getTransform().getPosition(true).y);
			for(int i=-1; i<=1; i++) {
				for(int j=-1; j<=1; j++) {
					getChunk(camX+i, camY+j);
				}
			}
		}
		
		
	}
	
	public static final PlanetRenderer RENDERER = new PlanetRenderer();
	
	private Scene2D planet;
	private long id = Instant.now().toEpochMilli();
	private String name;
	private long radius=100;
	
	
	private Chunk[][] chunks;
	private int chunksSize;
	private Random random;
	
	public Planet(String name) {
		planet = new PlanetScene(name+id);
		chunksSize = (int) Math.ceil(radius/Chunk.CHUNKSIZE_T);
		if(chunksSize>(Integer.MAX_VALUE>>1)-10) {
			Logger.log("Planetsize exceeds Integer.MAX_VALUE!", LogLevel.WARNING);
		}
		chunksSize <<= 1;
		chunks = new Chunk[chunksSize][chunksSize];
		random = new Random();
		this.name = name;
	}
	
	public Planet setAsScene(Player p) {
		OmniKryptecEngine.instance().addAndSetScene(planet);
		planet.addGameObject(p);
		return this;
	}
	
	
	public Chunk getChunk(int cx, int cy) {
		if(cx>=(chunksSize>>1)||cy>=(chunksSize>>1)||cx<-(chunksSize>>1)||cy<-(chunksSize>>1)) {
			return null;
		}
		if(chunks[cx+(chunksSize>>1)][cy+(chunksSize>>1)]==null) {
			chunks[cx+(chunksSize>>1)][cy+(chunksSize>>1)] = new Chunk(cx, cy).generate(random, radius, radius-40).preRender().addTo(planet);
		}
		return chunks[cx+(chunksSize>>1)][cy+(chunksSize>>1)];
	}

	
}
