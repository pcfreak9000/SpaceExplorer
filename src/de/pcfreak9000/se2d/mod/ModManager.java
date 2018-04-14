package de.pcfreak9000.se2d.mod;

import de.pcfreak9000.se2d.game.Launcher;
import de.pcfreak9000.se2d.game.SpaceExplorer2D;

public class ModManager {
	
	private ModLoader loader;
	
	public ModManager() {
		loader = new ModLoader();
		loader.classLoadMods(Launcher.FOLDER.toFile());
	}
	
	
}
