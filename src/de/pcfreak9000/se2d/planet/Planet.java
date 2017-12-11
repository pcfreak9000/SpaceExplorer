package de.pcfreak9000.se2d.planet;

import java.time.Instant;

import de.pcfreak9000.se2d.game.Player;
import omnikryptec.gameobject.Camera;
import omnikryptec.gameobject.GameObject2D;
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
		planet = new Scene2D(name+id);
		this.name = name;
	}
	
	public Planet setAsScene() {
		OmniKryptecEngine.instance().addAndSetScene(planet);
		planet.addGameObject((GameObject2D) new Sprite(ResourceLoader.currentInstance().getTexture("topdownfighter.png")).setFixedSize(1000, 1000).setLayer(-1).setFixedSizeMode(FixedSizeMode.ON).setGlobal(true));
		return this;
	}
	
	public Planet setPlayer(Player p) {
		planet.setCamera(p.getPlanetCamera());
		planet.addGameObject(p);
		return this;
	}
	
}
