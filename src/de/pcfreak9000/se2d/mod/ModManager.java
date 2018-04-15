package de.pcfreak9000.se2d.mod;

import java.util.List;

import de.codemakers.io.file.AdvancedFile;
import de.pcfreak9000.se2d.game.Launcher;
import de.pcfreak9000.se2d.game.SpaceExplorer2D;

public class ModManager {
	
	private ModLoader loader;
	private List<ModContainer> mods;
	
	public ModManager() {
		loader = new ModLoader();
	}
	
	public void load(AdvancedFile modsfolder) {
		loader.classLoadMods(modsfolder.toFile());
		mods = loader.instantiate();
	}
	
}
