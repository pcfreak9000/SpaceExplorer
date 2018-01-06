package de.pcfreak9000.se2d.main;

import de.codemakers.io.file.AdvancedFile;
import de.pcfreak9000.se2d.game.SpaceExplorer2D;
import de.pcfreak9000.se2d.planet.Chunk;
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

	public static final boolean DEBUG=true;
	
	public static final String NAME = "SpaceExplorer2D";
	public static final AdvancedFile FOLDER = OSUtil.getAppDataFolder(NAME);
	public static final String NATIVES_DIR_NAME = "natives";
	public static final String RESOURCEPACKS = "resourcepacks";
	public static final double ASPECT_RATIO = 16 / 9.0;

	public static void main(String[] args) {
		new Launcher();
	}

	public Launcher() {
		NativesLoader.loadNatives(null, new AdvancedFile(false, FOLDER, NATIVES_DIR_NAME));
		Logger.enableLoggerRedirection(true);
		Logger.setDebugMode(DEBUG);
		Logger.setMinimumLogLevel(LogLevel.FINE);
		DisplayManager.createDisplay("SpaceExplorer2D",
				new GameSettings().setAnisotropicLevel(16).setMultisamples(16).setUseRenderChunking(true).setUseFrustrumCulling(true)
						.setBoolean(GameSettings.LIGHT_2D, true)
						.setInteger(GameSettings.CHUNK_WIDTH_2D, (int) Chunk.CHUNKSIZE)
						.setInteger(GameSettings.CHUNK_HEIGHT_2D, (int) Chunk.CHUNKSIZE)
						.setInteger(GameSettings.CHUNK_OFFSET_2D_X, 1).setInteger(GameSettings.CHUNK_OFFSET_2D_Y, 1).setBoolean(GameSettings.DYN4J_PHYSICS_REMOVE_ADD_LIFECYCLE, true),
				new GLFWInfo(3, 2, true, false, 1280, 720));
		Display.setAspectRatio(ASPECT_RATIO, true);
		OmniKryptecEngine.instance().getGameSettings().setKeySettings(new KeySettings());
		KeyManager.init();
		new SpaceExplorer2D(new AdvancedFile(false, FOLDER, RESOURCEPACKS));
	}

}
