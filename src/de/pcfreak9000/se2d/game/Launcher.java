package de.pcfreak9000.se2d.game;

import de.codemakers.io.file.AdvancedFile;
import de.pcfreak9000.se2d.universe.planet.Chunk;
import de.pcfreak9000.se2d.universe.planet.TileDefinition;
import omnikryptec.display.Display;
import omnikryptec.display.DisplayManager;
import omnikryptec.display.GLFWInfo;
import omnikryptec.main.OmniKryptecEngine;
import omnikryptec.settings.GameSettings;
import omnikryptec.settings.KeySettings;
import omnikryptec.util.NativesLoader;
import omnikryptec.util.OSUtil;
import omnikryptec.util.logger.LogLevel;
import omnikryptec.util.logger.Logger;

public class Launcher {

	public static final boolean DEBUG = true;

	public static final String NAME = "SpaceExplorer2D";
	public static final AdvancedFile FOLDER = OSUtil.getAppDataFolder(NAME);
	public static final String NATIVES_DIR_NAME = "natives";
	public static final String RESOURCEPACKS = "resourcepacks";
	public static final double ASPECT_RATIO = 16 / 9.0;

	private static final double PIXELS_PER_METER = TileDefinition.TILE_SIZE;

	public static void main(String[] args) {
		new Launcher();
	}

	public Launcher() {
		// double all = 0;
		// double min = Double.POSITIVE_INFINITY;
		// double max = Double.NEGATIVE_INFINITY;
		// int am=20;
		// for (int i = 0; i < am; i++) {
		// double d = new PlanetData(new Random().nextInt()).getsmth();
		// System.out.println(d);
		// min = Math.min(min, d);
		// max = Math.max(max, d);
		// all += d;
		// }
		// System.out.println("Avg: " + (all / am) + " Max: " + max + " Min: " + min);
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
						.setBoolean(GameSettings.DYN4J_PHYSICS_REMOVE_ADD_LIFECYCLE, true)
						.setBoolean(GameSettings.DYN4J_PHYSICS_VAR_TS, false).setPixelsPerMeter(PIXELS_PER_METER),
				new GLFWInfo(3, 2, true, false, 1280, 720));
		Display.setAspectRatio(ASPECT_RATIO);
		OmniKryptecEngine.instance().getGameSettings().setKeySettings(new KeySettings());
		KeyManager.init();
		new SpaceExplorer2D(new AdvancedFile(false, FOLDER, RESOURCEPACKS));
	}

}
