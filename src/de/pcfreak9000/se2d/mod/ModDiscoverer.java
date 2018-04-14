package de.pcfreak9000.se2d.mod;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModDiscoverer {

	private static Pattern zipJar = Pattern.compile("(.+).(zip|jar)$");	
	
	ModDiscoverer discover(List<File> candidates, File dir) {
		__discover(candidates, dir);
		return this;
	}
	
	private void __discover(List<File> files, File f) {
		if(f.isDirectory()) {
			File[] innerFiles = f.listFiles();
			for(File inner : innerFiles) {
				__discover(files, inner);
			}
		}else {
			Matcher matcher = zipJar.matcher(f.getName());
			if(matcher.matches()) {
				files.add(f);
			}
		}
	}
}
