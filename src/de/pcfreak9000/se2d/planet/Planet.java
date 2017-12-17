package de.pcfreak9000.se2d.planet;

import java.time.Instant;
import java.util.Random;

import de.pcfreak9000.renderer.PlanetRenderer;
import de.pcfreak9000.se2d.game.Player;
import de.pcfreak9000.se2d.game.SpaceExplorer2D;
import omnikryptec.gameobject.Camera;
import omnikryptec.gameobject.GameObject2D;
import omnikryptec.gameobject.Sprite;
import omnikryptec.main.OmniKryptecEngine;
import omnikryptec.main.Scene2D;
import omnikryptec.resource.loader.ResourceLoader;
import omnikryptec.resource.texture.SimpleTexture;
import omnikryptec.util.EnumCollection.FixedSizeMode;
import omnikryptec.util.Maths;

public class Planet {
	
	public static final PlanetRenderer RENDERER = new PlanetRenderer();
	
	private Scene2D planet;
	private Instant id = Instant.now();
	private String name;
	private long radius=25;
	
	
	public Planet(String name) {
		planet = new Scene2D(name+id, SpaceExplorer2D.getSpaceExplorer2D().getPlanetCamera());
		planet.setRenderer(RENDERER);
		this.name = name;
	}
	
	public Planet setAsScene(Player p) {
		OmniKryptecEngine.instance().addAndSetScene(planet);
		planet.addGameObject(p);
		generateChunk(0, 0);
		return this;
	}
	
	
	public void generateChunk(float wx, float wy) {
		Chunk chunk = new Chunk(0, 0);
		chunk.generate(new Random(), radius, radius-10).preRender();
		chunk.add(planet);
	}
	
	public void getBiome(float x, float y) {
		
	}
	
}
