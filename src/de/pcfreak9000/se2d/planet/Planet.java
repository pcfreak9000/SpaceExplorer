package de.pcfreak9000.se2d.planet;

import java.time.Instant;

import omnikryptec.gameobject.Camera;
import omnikryptec.gameobject.Sprite;
import omnikryptec.main.OmniKryptecEngine;
import omnikryptec.main.Scene2D;
import omnikryptec.resource.loader.ResourceLoader;
import omnikryptec.util.EnumCollection.FixedSizeMode;

public class Planet {

	private Scene2D planet;
	private Instant id = Instant.now();
	private String name;
	
	public Planet(String name) {
		planet = new Scene2D(name+id, new Camera().setOrthographicProjection2D(0, 0, 1280, 720));
		this.name = name;
	}
	
	public void setAsScene() {
		OmniKryptecEngine.instance().addAndSetScene(planet);
		planet.addGameObject(new Sprite(ResourceLoader.currentInstance().getTexture("topdownfighter.png")).setFixedHeightAR(1000).setFixedSizeMode(FixedSizeMode.ON));
	}
	
}
