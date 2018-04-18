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
		loader.classLoadMods(modsfolder.toFile());
		mods = new ArrayList<>();
		loader.instantiate(mods);
		loader.dispatchInstances(mods);
		loader.registerEvents(mods);
	}

	public List<ModContainer> getMods() {
		return mods;
	}

}
