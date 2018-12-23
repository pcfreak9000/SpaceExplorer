package de.pcfreak9000.spaceexplorer.util;

import java.util.logging.Logger;

import de.omnikryptec.old.util.logger.LogLevel;

public class Se2Dlog {
    
    public static void log(final Object msg) {
        Logger.log(msg);
    }
    
    public static void log(final LogLevel logLevel, final Object msg) {
        Logger.log(msg, logLevel);
    }
    
    public static void logErr(final Object msg, final Exception ex) {
        Logger.logErr(msg, ex);
    }
    
}
