package de.pcfreak9000.space.core;

import de.codemakers.base.os.OSUtil;
import de.codemakers.io.file.AdvancedFile;
import de.omnikryptec.core.EngineLoader;
import de.omnikryptec.core.update.IUpdatable;
import de.omnikryptec.core.update.UContainer;
import de.omnikryptec.ecs.Entity;
import de.omnikryptec.libapi.exposed.LibAPIManager.LibSetting;
import de.omnikryptec.libapi.exposed.input.InputManager;
import de.omnikryptec.libapi.exposed.window.WindowSetting;
import de.omnikryptec.resource.loadervpc.LoadingProgressCallback;
import de.omnikryptec.util.Profiler;
import de.omnikryptec.util.math.transform.Transform2Df;
import de.omnikryptec.util.settings.IntegerKey;
import de.omnikryptec.util.settings.Settings;
import de.omnikryptec.util.updater.Time;
import de.pcfreak9000.space.mod.ModLoader;
import de.pcfreak9000.space.world.Chunk;
import de.pcfreak9000.space.world.GroundManager;
import de.pcfreak9000.space.world.IGenerator;
import de.pcfreak9000.space.world.TileWorld;
import de.pcfreak9000.space.world.WorldLoadingFence;
import de.pcfreak9000.space.world.ecs.PlayerInputComponent;
import de.pcfreak9000.space.world.tile.Tile;

public class Space extends EngineLoader {
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
    
    //Confusing to use?
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
        createInputmanagers();
        groundManager = new GroundManager(getGameController());
        loader.load(mkdirIfNonExisting(new AdvancedFile(FOLDER, MODS)));
        getResourceManager().addCallback(LoadingProgressCallback.DEBUG_CALLBACK);
        reloadResources();
        Transform2Df tr = new Transform2Df();
        tr.localspaceWrite().setTranslation(-100, -100);
        WorldLoadingFence f = new WorldLoadingFence(tr);
        f.setRange(5, 5);
        Entity player = new Entity();
        PlayerInputComponent comp = new PlayerInputComponent();
        comp.cam = groundManager.getPlanetCamera().getTransform();
        comp.maxXv = 10;
        comp.maxYv = 10;
        player.addComponent(comp);
        groundManager.getECSManager().addEntity(player);
        groundManager.setWorldUpdateFence(f);
        groundManager.setWorld(new TileWorld(1000, new IGenerator() {
            
            @Override
            public void generateChunk(Chunk chunk) {
                for (int i = 0; i < Chunk.CHUNK_TILE_SIZE; i++) {
                    for (int j = 0; j < Chunk.CHUNK_TILE_SIZE; j++) {
                        chunk.setTile(new Tile(GameRegistry.TILE_REGISTRY.get("Kek vom Dienst"),
                                i * chunk.getChunkX() * Chunk.CHUNK_TILE_SIZE,
                                j * chunk.getChunkY() * Chunk.CHUNK_TILE_SIZE), i, j);
                    }
                }
            }
        }));
    }
    
    @Override
    protected void onShutdown() {
        System.out.println(Profiler.currentInfo());   
    }
    
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
    
    public InputManager getGameInput() {
        return gameInput;
    }
}
