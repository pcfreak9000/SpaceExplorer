package de.pcfreak9000.se2d.main;

import de.codemakers.io.file.AdvancedFile;
import de.pcfreak9000.se2d.planet.Planet;
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
		new Planet("Deine Mutter").setAsScene();
	}
	
}
