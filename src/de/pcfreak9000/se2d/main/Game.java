package de.pcfreak9000.se2d.main;

import de.pcfreak9000.se2d.planet.Planet;
import omnikryptec.main.OmniKryptecEngine;

public class Game {

	public Game() {
		loadRes();
		loadWorld();
		OmniKryptecEngine.instance().startLoop();
	}

	private void loadRes() {
		
	}
	
	private void loadWorld() {
		new Planet("Deine Mutter").setAsScene();
	}
	
}
