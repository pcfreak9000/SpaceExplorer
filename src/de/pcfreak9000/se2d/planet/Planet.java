package de.pcfreak9000.se2d.planet;

import java.time.Instant;

import omnikryptec.gameobject.Camera;
import omnikryptec.main.OmniKryptecEngine;
import omnikryptec.main.Scene2D;

public class Planet {

	private Scene2D planet;
	private Instant id = Instant.now();
	private String name;
	
	public Planet(String name) {
		planet = new Scene2D(name+id, new Camera().setOrthographicProjection2D(0, 0, 1920, 1080));
		this.name = name;
	}
	
	public void setAsScene() {
		OmniKryptecEngine.instance().addAndSetScene(planet);
	}
	
}
