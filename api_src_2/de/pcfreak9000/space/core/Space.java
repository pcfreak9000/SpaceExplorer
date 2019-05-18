package de.pcfreak9000.space.core;

import de.codemakers.base.os.OSUtil;
import de.codemakers.io.file.AdvancedFile;
import de.omnikryptec.core.EngineLoader;
import de.omnikryptec.core.update.UContainer;
import de.omnikryptec.libapi.exposed.LibAPIManager.LibSetting;
import de.omnikryptec.libapi.exposed.input.InputManager;
import de.omnikryptec.libapi.exposed.window.WindowSetting;
import de.omnikryptec.resource.loadervpc.LoadingProgressCallback;
import de.omnikryptec.util.settings.IntegerKey;
import de.omnikryptec.util.settings.Settings;
import de.pcfreak9000.space.mod.ModLoader;
import de.pcfreak9000.space.world.GroundManager;

public class Space extends EngineLoader {
    public static final boolean DEBUG = true;
    
    public static final String NAME = "Space Awaits";
    public static final String VERSION = "pre-Alpha-0";
    
    public static final AdvancedFile FOLDER = new AdvancedFile(OSUtil.getAppDataSubDirectory(NAME));
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
    
    private InputManager guiInput;
    private InputManager gameInput;
    
    private Space() {
        space = this;
    }
    
    @Override
    protected void configure(Settings<LoaderSetting> loaderSettings, Settings<LibSetting> libSettings,
            Settings<WindowSetting> windowSettings, Settings<IntegerKey> apiSettings) {
        windowSettings.set(WindowSetting.Name, NAME + " " + VERSION);
    }
    
    @Override
    protected void onInitialized() {
        loader.load(mkdirIfNonExisting(new AdvancedFile(FOLDER, MODS)));
        getResourceManager().addCallback(LoadingProgressCallback.DEBUG_CALLBACK);
        reloadResources();
        createInputmanagers();
        groundManager = new GroundManager(getGameController());
    }
    
    public GroundManager getGroundManager() {
        return groundManager;
    }
    
    public void reloadResources() {
        getResourceManager().clearStaged();
        loader.stageModResources(getResourceManager(), 1);
        getResourceManager().stage(mkdirIfNonExisting(new AdvancedFile(FOLDER, RESOURCEPACKS)), 0);
        getResourceManager().processStaged(true, false);
        getResourceManager().clearStaged();
    }
    
    private void createInputmanagers() {
        UContainer updt = new UContainer();
        guiInput = new InputManager(null);
        gameInput = new InputManager(null);
        updt.setUpdatable(0, guiInput);
        updt.setUpdatable(1, gameInput);
        getGameController().getGlobalScene().setUpdateableSync(updt);
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
