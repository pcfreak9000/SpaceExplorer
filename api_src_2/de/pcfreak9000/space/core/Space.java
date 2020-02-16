package de.pcfreak9000.space.core;

import de.codemakers.base.os.OSUtil;
import de.codemakers.io.file.AdvancedFile;
import de.omnikryptec.core.Omnikryptec;
import de.omnikryptec.libapi.exposed.LibAPIManager.LibSetting;
import de.omnikryptec.libapi.exposed.window.WindowSetting;
import de.omnikryptec.resource.loadervpc.LoadingProgressCallback;
import de.omnikryptec.util.math.transform.Transform2Df;
import de.omnikryptec.util.profiling.Profiler;
import de.omnikryptec.util.settings.IntegerKey;
import de.omnikryptec.util.settings.KeySettings;
import de.omnikryptec.util.settings.Settings;
import de.pcfreak9000.space.mod.ModLoader;
import de.pcfreak9000.space.world.GeneratorTemplate.GeneratorCapabilitiesBase;
import de.pcfreak9000.space.world.GroundManager;
import de.pcfreak9000.space.world.TileWorld;
import de.pcfreak9000.space.world.WorldLoadingFence;

/**
 * The main class. General settings and ressource/mod loading.
 * 
 * @author pcfreak9000
 *
 */
public class Space extends Omnikryptec {
    public static final boolean DEBUG = true;
    
    public static final String NAME = "Space Awaits";
    public static final String VERSION = "pre-Alpha-0";
    
    public static final AdvancedFile FOLDER = new AdvancedFile(OSUtil.getAppDataSubDirectory("." + NAME));
    public static final String RESOURCEPACKS = "resourcepacks";
    public static final String MODS = "mods";
    public static final double ASPECT_RATIO = 16 / 9.0;
    
    private static Space space;
    
    public static void main(String[] args) {
        new Space().start();
        System.exit(0);
    }
    
    public static Space getSpace() {
        return space;
    }
    
    private ModLoader loader = new ModLoader();
    
    private GroundManager groundManager;
    
    private Space() {
        space = this;
    }
    
    @Override
    protected void configure(Settings<LoaderSetting> loaderSettings, Settings<LibSetting> libSettings,
            Settings<WindowSetting> windowSettings, Settings<IntegerKey> apiSettings, KeySettings keys) {
        windowSettings.set(WindowSetting.Name, NAME + " " + VERSION);
        Keys.applyDefaultKeyConfig(keys);
    }
    
    @Override
    protected void onInitialized() {
        groundManager = new GroundManager();
        loader.load(mkdirIfNonExisting(new AdvancedFile(FOLDER, MODS)));
        getResourceManager().addCallback(LoadingProgressCallback.DEBUG_CALLBACK);
        reloadResources();
        
        //TESTING:
        Transform2Df tr = new Transform2Df();
        tr.localspaceWrite().setTranslation(-100, -100);
        WorldLoadingFence f = new WorldLoadingFence(tr);
        f.setRange(5, 5);
        groundManager.setWorldUpdateFence(f);
        
        GameInstance ins = new GameInstance(groundManager);
        ins.visit(new TileWorld(10, GameRegistry.GENERATOR_REGISTRY.filtered(GeneratorCapabilitiesBase.LVL_ENTRY).get(0)
                .createGenerator(0)), 0, 0);
        //***************
    }
    
    @Override
    protected void onShutdown() {
        System.out.println(Profiler.currentInfo());
    }
    
    @Deprecated
    public GroundManager getGroundManager() {
        return groundManager;
    }
    
    public void reloadResources() {
        //Do this only if no scene is loaded
        getResourceManager().clearStaged();
        getResourceProvider().clear();
        getTextures().clearAndDeleteTextures();
        //loader.stageModResources(getResourceManager(), 1);
        getResourceManager().stage(mkdirIfNonExisting(new AdvancedFile(FOLDER, RESOURCEPACKS)), 0);
        getResourceManager().processStaged(true, false);
        getResourceManager().clearStaged();
        GameRegistry.TILE_REGISTRY.initAll(getTextures());
    }
    
    private AdvancedFile mkdirIfNonExisting(AdvancedFile file) {
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }
    
}
