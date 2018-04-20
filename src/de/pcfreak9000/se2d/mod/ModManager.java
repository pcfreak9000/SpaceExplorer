package de.pcfreak9000.se2d.mod;

import java.util.ArrayList;
import java.util.List;

import de.codemakers.io.file.AdvancedFile;

public class ModManager {

	private ModLoader loader;
	private List<ModContainer> mods;

	public ModManager() {
		loader = new ModLoader();
	}

	public void load(AdvancedFile modsfolder) {
		mods = new ArrayList<>();
		loader.classLoadMods(modsfolder.toFile());
		loader.instantiate(mods);
		loader.dispatchInstances(mods);
		loader.registerEvents(mods);
		loader.preInit();
		loader.init();
		loader.postInit();
	}

	public void stageRessourceLoading() {
		loader.stageRes();
	}

	
	public List<ModContainer> getMods() {
		return mods;
	}

}
