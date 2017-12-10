package de.pcfreak9000.se2d.main;

import de.codemakers.io.file.AdvancedFile;
import omnikryptec.display.Display;
import omnikryptec.display.DisplayManager;
import omnikryptec.display.GLFWInfo;
import omnikryptec.settings.GameSettings;
import omnikryptec.util.AdvancedThreadFactory;
import omnikryptec.util.NativesLoader;
import omnikryptec.util.OSUtil;
import omnikryptec.util.logger.LogLevel;
import omnikryptec.util.logger.Logger;

public class Launcher {

	public static final String NAME = "SpaceExplorer2D"; 
	public static final AdvancedFile FOLDER = OSUtil.getAppDataFolder(NAME);
	public static final String NATIVES_DIR_NAME = "natives";
	public static final String RESOURCEPACKS = "resourcepacks";
	public static final double ASPECT_RATIO = 16/9.0;
	
	public static void main(String[] args) {
		new Launcher();
	}
	
	public Launcher() {
		NativesLoader.loadNatives(new AdvancedFile(FOLDER, NATIVES_DIR_NAME));
		Logger.enableLoggerRedirection(true);
        Logger.setDebugMode(true);
        Logger.setMinimumLogLevel(LogLevel.FINE);
        DisplayManager.createDisplay("SpaceExplorer2D",
				new GameSettings().setAnisotropicLevel(16).setMultisamples(16).setUseRenderChunking(true)
						.setBoolean(GameSettings.LIGHT_2D, true),
				new GLFWInfo(3, 2, true, false, 1280, 720));
		Display.setAspectRatio(ASPECT_RATIO, true);
		new Game(new AdvancedFile(FOLDER, RESOURCEPACKS));
	}

}
