package de.pcfreak9000.spaceexplorer.util;

import de.omnikryptec.old.util.logger.LogLevel;
import de.omnikryptec.old.util.logger.Logger;

public class Se2Dlog {

	public static void log(Object msg) {
		Logger.log(msg);
	}

	public static void log(LogLevel logLevel, Object msg) {
		Logger.log(msg, logLevel);
	}

	public static void logErr(Object msg, Exception ex) {
		Logger.logErr(msg, ex);
	}

}