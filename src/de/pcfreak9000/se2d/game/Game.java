package de.pcfreak9000.se2d.game;

import de.codemakers.io.file.AdvancedFile;
import de.pcfreak9000.se2d.planet.Planet;
import omnikryptec.gameobject.Camera;
import omnikryptec.main.OmniKryptecEngine;
import omnikryptec.resource.loader.ResourceLoader;

public class Game {

	private static final AdvancedFile RESOURCELOCATION = new AdvancedFile("de", "pcfreak9000", "se2d", "res");
	private AdvancedFile resourcepacks;
	
	public Game(AdvancedFile resourcepacks) {
		this.resourcepacks = resourcepacks;
		ResourceLoader.createInstanceDefault(true);
		loadRes();
		loadWorld();
		OmniKryptecEngine.instance().startLoop();
	}

	private void loadRes() {
		ResourceLoader.currentInstance().clearStagedAdvancedFiles();
		ResourceLoader.currentInstance().stageAdvancedFiles(0, resourcepacks);
		ResourceLoader.currentInstance().stageAdvancedFiles(1, RESOURCELOCATION);
		ResourceLoader.currentInstance().loadStagedAdvancedFiles(true);
	}
	
	private void loadWorld() {
		new Planet("Deine Mutter").setAsScene().setPlayer(new Player(new Camera().setOrthographicProjection2D(0, 0, 1000, 1000)));
	}
	
}
