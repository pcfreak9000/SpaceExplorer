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
import omnikryptec.util.Color;
import omnikryptec.util.EnumCollection.FixedSizeMode;
import omnikryptec.util.Maths;

public class Planet {
	
	public static final PlanetRenderer RENDERER = new PlanetRenderer();
	
	private Scene2D planet;
	private long id = Instant.now().toEpochMilli();
	private String name;
	private long radius=30;
	
	
	public Planet(String name) {
		planet = new Scene2D(name+id, SpaceExplorer2D.getSpaceExplorer2D().getPlanetCamera());
		planet.setRenderer(RENDERER);
		this.name = name;
	}
	
	public Planet setAsScene(Player p) {
		OmniKryptecEngine.instance().addAndSetScene(planet);
		planet.addGameObject(p);
//		Sprite sp = new Sprite(ResourceLoader.currentInstance().getTexture("sdfsdf")).setLayer(10);
//		sp.getTransform().setScale(100).setPosition(-100, -100);
//		sp.setColor(new Color(1, 1, 1, 0.5f));
//		planet.addGameObject(sp);
		generateChunk(0, 0);
		return this;
	}
	
	
	public void generateChunk(long cx, long cy) {
		Chunk chunk = new Chunk(cx, cy);
		chunk.generate(new Random(), radius, radius-10).preRender();
		chunk.add(planet);
	}
	
	public void getBiome(float x, float y) {
		
	}
	
}
