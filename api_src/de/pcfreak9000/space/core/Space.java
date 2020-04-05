package de.pcfreak9000.space.core;

import java.util.List;
import java.util.Random;

import de.codemakers.base.os.OSUtil;
import de.codemakers.io.file.AdvancedFile;
import de.omnikryptec.core.Omnikryptec;
import de.omnikryptec.event.EventBus;
import de.omnikryptec.libapi.exposed.LibAPIManager.LibSetting;
import de.omnikryptec.libapi.exposed.window.WindowSetting;
import de.omnikryptec.util.Logger;
import de.omnikryptec.util.math.MathUtil;
import de.omnikryptec.util.profiling.Profiler;
import de.omnikryptec.util.settings.IntegerKey;
import de.omnikryptec.util.settings.KeySettings;
import de.omnikryptec.util.settings.Settings;
import de.pcfreak9000.space.mod.ModLoader;
import de.pcfreak9000.space.voxelworld.GroundManager;
import de.pcfreak9000.space.voxelworld.TileWorldGenerator;
import de.pcfreak9000.space.voxelworld.TileWorldGenerator.GeneratorCapabilitiesBase;
import de.pcfreak9000.space.voxelworld.WorldInformationBundle;
import de.pcfreak9000.space.voxelworld.tile.Tile;

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

    private static final AdvancedFile DEFAULT_RES_LOC = new AdvancedFile("intern:/de/pcfreak9000/space/resources/");
    private static final AdvancedFile LOADING_RES_LOC = new AdvancedFile("intern:/de/pcfreak9000/space/loading/");

    public static final EventBus BUS = new EventBus();
    private static final Logger LOGGER = Logger.getLogger(Space.class);

    private static Space space;

    public static void main(String[] args) {
        new Space().start();
        System.exit(0);
    }

    public static Space getSpace() {
        return space;
    }

    private final ModLoader modloader = new ModLoader();

    private GroundManager groundManager;

    private Space() {
        space = this;
    }

    @Override
    protected void configure(Settings<LoaderSetting> loaderSettings, Settings<LibSetting> libSettings,
            Settings<WindowSetting> windowSettings, Settings<IntegerKey> apiSettings, KeySettings keys) {
        windowSettings.set(WindowSetting.Name, NAME + " " + VERSION);
        libSettings.set(LibSetting.LOGGING_MIN, Logger.LogType.Debug);
        Keys.applyDefaultKeyConfig(keys);
        Profiler.setEnabled(true);
    }

    @Override
    protected void onInitialized() {
        //getResourceManager().addCallback(LoadingProgressCallback.DEBUG_CALLBACK);
        preloadResources();
        LoadingScreen s = new LoadingScreen(9);
        getResourceManager().addCallback(new LoadingScreenCallback());
        s.begin();
        this.modloader.load(mkdirIfNonExisting(new AdvancedFile(FOLDER, MODS)));
        loadResources();
        s.end();
        this.groundManager = new GroundManager();//Hmmm...
        //TESTING:
        GameInstance ins = new GameInstance(this.groundManager);
        WorldInformationBundle testWorld = pickGenerator(
                GameRegistry.GENERATOR_REGISTRY.filtered(GeneratorCapabilitiesBase.LVL_ENTRY)).generateWorld(0);
        ins.visit(testWorld, 400 * Tile.TILE_SIZE, 3500);
        //***************
    }

    //TMP
    private TileWorldGenerator pickGenerator(List<TileWorldGenerator> list) {
        return MathUtil.getWeightedRandom(new Random(), list);
    }

    @Override
    protected void onShutdown() {
        System.out.println(Profiler.currentInfo());
    }

    public void reloadResources() {
        //TODO loading screen
        //Do this only if no scene is loaded
        getResourceManager().clearStaged();
        getResourceProvider().clear();
        getTextures().clearAndDeleteTextures();
        //loader.stageModResources(getResourceManager(), 1);
        preloadResources();
        loadResources();

    }

    private void preloadResources() {
        LOGGER.debug("Preloading resources");
        getResourceManager().stage(LOADING_RES_LOC);
        getResourceManager().processStaged(false, false);
        getResourceManager().clearStaged();
        LOGGER.debug("Finished preloading resources!");
    }

    private void loadResources() {
        LOGGER.info("Loading resources...");
        getResourceManager().stage(DEFAULT_RES_LOC);
        getResourceManager().stage(mkdirIfNonExisting(new AdvancedFile(FOLDER, RESOURCEPACKS)), 0);
        getResourceManager().processStaged(true, false);
        getResourceManager().clearStaged();
        LOGGER.info("Finished resource loading!");
        LoadingScreen.LOADING_STAGE_BUS.post(new LoadingScreen.LoadingEvent("Assigning resources"));
        BUS.post(new CoreEvents.AssignResourcesEvent(getTextures(), getSounds()));
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
