package de.pcfreak9000.se2d.util;

import omnikryptec.util.logger.Logger;

public class Se2Dlog {
	
	public static void log(Object msg) {
		Logger.log(msg);
	}
	
	public static void logErr(Object msg, Exception ex) {
		Logger.logErr(msg, ex);
	}
	
}
