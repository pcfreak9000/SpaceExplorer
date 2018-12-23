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
    
    private final ModLoader loader;
    private List<ModContainer> mods;
    
    public ModManager() {
        this.loader = new ModLoader();
    }
    
    public void load(final AdvancedFile modsfolder) {
        this.mods = new ArrayList<>();
        this.loader.classLoadMods(modsfolder.toFile());
        this.loader.instantiate(this.mods);
        this.loader.dispatchInstances(this.mods);
        this.loader.registerEvents(this.mods);
        this.loader.preInit();
        this.loader.init();
        this.loader.postInit();
    }
    
    public void stageRessourceLoading() {
        this.loader.stageRes();
    }
    
    public List<ModContainer> getMods() {
        return this.mods;
    }
    
}
