package de.pcfreak9000.spaceexplorer.mod;

import java.util.ArrayList;
import java.util.List;

import de.codemakers.io.file.AdvancedFile;
import de.pcfreak9000.spaceexplorer.util.Private;

/**
 * coordinates mod discovering, loading and event steps.
 * 
 * @author pcfreak9000
 *
 */
@Private
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
