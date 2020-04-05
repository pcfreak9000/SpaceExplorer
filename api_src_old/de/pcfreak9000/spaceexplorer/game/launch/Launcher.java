package de.pcfreak9000.spaceexplorer.game.launch;

import java.io.File;
import java.util.logging.Logger;

import de.codemakers.base.logger.LogLevel;
import de.codemakers.base.os.OSUtil;
import de.codemakers.io.file.AdvancedFile;
import de.omnikryptec.core.DefaultGameLoop;
import de.omnikryptec.graphics.display.Display;
import de.omnikryptec.graphics.display.DisplayManager;
import de.omnikryptec.libapi.exposed.window.WindowInfo;
import de.omnikryptec.old.main.OmniKryptecEngine;
import de.omnikryptec.old.postprocessing.stages.Light2DProcessor;
import de.omnikryptec.old.settings.GameSettings;
import de.omnikryptec.old.util.NativesLoader;
import de.omnikryptec.util.settings.KeySettings;
import de.pcfreak9000.spaceexplorer.universe.tiles.TileDefinition;
import de.pcfreak9000.spaceexplorer.universe.worlds.Chunk;
import de.pcfreak9000.spaceexplorer.universe.worlds.World;

public class Launcher {

    public static final boolean DEBUG = true;

    public static final String NAME = "SpaceExplorer2D";
    public static final AdvancedFile FOLDER = OSUtil.getAppDataFolder(NAME);
    public static final String NATIVES_DIR_NAME = "natives";
    public static final String RESOURCEPACKS = "resourcepacks";
    public static final String MODS = "mods";
    public static final double ASPECT_RATIO = 16 / 9.0;

    private static final double PIXELS_PER_METER = TileDefinition.TILE_SIZE;
    public static final String VERSION = "0.0.1";

    public static void main(final String[] args) {
        new Launcher();
    }

    public Launcher() {
        // double all = 0;
        // double min = double.POSITIVE_INFINITY;
        // double max = double.NEGATIVE_INFINITY;
        // int am=1000;
        // Random random = new Random();
        // for (int i = 0; i < am; i++) {
        // double d = /*random.nextDouble()*(10000000000L-10000)+10000;*/Math.pow(10,
        // random.nextInt(6))*10000;
        //
        // //System.out.println(d);
        // min = Math.min(min, d);
        // max = Math.max(max, d);
        // all += d;
        // }
        // System.out.println("Avg: " + (all / am) + " Max: " + max + " Min: " + min);
        ////
        //// for(int i=0; i<20; i++) {
        //// System.out.println();
        //// System.out.println();
        //// }
        NativesLoader.loadNatives(null, new AdvancedFile(false, FOLDER, NATIVES_DIR_NAME));
        Logger.enableLoggerRedirection(true);
        Logger.setDebugMode(DEBUG);
        Logger.setMinimumLogLevel(LogLevel.FINE);
        DisplayManager.createDisplay("SpaceExplorer2D",
                new GameSettings().setAnisotropicLevel(16).setMultisamples(16).setUseRenderChunking(true)
                        .setUseFrustrumCulling(true).setInteger(GameSettings.FPS_CAP, 0)
                        .setInteger(GameSettings.DYN4J_MAX_SUBSTEPS, 100).setBoolean(GameSettings.LIGHT_2D, true)
                        .setInteger(GameSettings.CHUNK_WIDTH_2D, (int) Chunk.CHUNKSIZE)
                        .setInteger(GameSettings.CHUNK_HEIGHT_2D, (int) Chunk.CHUNKSIZE)
                        .setInteger(GameSettings.CHUNK_OFFSET_2D_X, 1).setInteger(GameSettings.CHUNK_OFFSET_2D_Y, 1)
                        .setBoolean(GameSettings.DYN4J_PHYSICS_REMOVE_ADD_LIFECYCLE, false)// <<-- WTF wieso war das
                        // true?
                        .setBoolean(GameSettings.DYN4J_PHYSICS_VAR_TS, false).setDouble(GameSettings.PIXELS_PER_METER,
                                PIXELS_PER_METER),
                new WindowInfo(3, 2, true, false, 1280, 720));
        Display.setAspectRatio(ASPECT_RATIO);
        OmniKryptecEngine.instance().setFboModes(OmniKryptecEngine.FboModes.SCENE);
        OmniKryptecEngine.instance().refreshFbos();
        final DefaultGameLoop l = (DefaultGameLoop) OmniKryptecEngine.instance().getLoop();
        l.setMode(DefaultGameLoop.MODE_2D | DefaultGameLoop.MODE_GUI | DefaultGameLoop.MODE_PP
                | DefaultGameLoop.MODE_GL_TASKS);
        OmniKryptecEngine.instance().getGameSettings().setKeySettings(new KeySettings());
        // OmniKryptecEngine.instance().getPostprocessor().addStage(new
        // ContrastchangeStage(-100));

        OmniKryptecEngine.instance().getPostprocessor().addStage(new Light2DProcessor(World.RENDERER));
        // OmniKryptecEngine.instance().getPostprocessor().addStage(new BloomStage(new
        // CompleteGaussianBlurStage(true, 100, 100), new Vector2f(1)));
        // OmniKryptecEngine.instance().getPostprocessor().addStage(new
        // PostProcessingDebugStage());

        KeyManager.init();
        new SpaceExplorer2D(new AdvancedFile(false, FOLDER, RESOURCEPACKS),
                new AdvancedFile(false, FOLDER.toString() + File.separator + MODS));
    }

}
