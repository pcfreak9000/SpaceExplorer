package de.pcfreak9000.spaceexplorer.mod;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.pcfreak9000.spaceexplorer.util.Private;

/**
 * Discovers mods
 *
 * @author pcfreak9000
 *
 */
@Private
public class ModDiscoverer {
    
    private static Pattern zipJar = Pattern.compile("(.+).(zip|jar)$");
    
    ModDiscoverer discover(final List<File> candidates, final File dir) {
        __discover(candidates, dir);
        return this;
    }
    
    private void __discover(final List<File> files, final File f) {
        if (f.isDirectory()) {
            final File[] innerFiles = f.listFiles();
            for (final File inner : innerFiles) {
                __discover(files, inner);
            }
        } else {
            final Matcher matcher = zipJar.matcher(f.getName());
            if (matcher.matches()) {
                files.add(f);
            }
        }
    }
}
