package de.pcfreak9000.se2d.mod;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import de.codemakers.io.file.AdvancedFile;
import de.pcfreak9000.se2d.util.Se2Dlog;
import omnikryptec.resource.loader.Loader;
import omnikryptec.resource.loader.LoadingType;
import omnikryptec.resource.loader.ResourceLoader;

public class ModLoader {
	
	
	private List<Class<?>> modClasses;
	
	void classLoadMods(File moddir){
		List<File> candidates = new ArrayList<>();
		new ModDiscoverer().discover(candidates, moddir);
		load(candidates.toArray(new File[0]));
	}
	
	@SuppressWarnings("resource")
	private void load(File[] candidates) {
		URL[] urlarray = new URL[candidates.length];
		for(int i=0; i<urlarray.length; i++) {
			try {
				urlarray[i] = candidates[i].toURI().toURL();
			} catch (MalformedURLException e) {
				Se2Dlog.logErr("Could not create some URL: "+candidates[i], e);
			}
		}
		ClassLoader classloader = new URLClassLoader(urlarray);
		for(int i=0; i<candidates.length; i++) {
			JarFile jarfile;
			try {
				jarfile = new JarFile(candidates[i]);
			} catch (IOException e) {
				Se2Dlog.logErr("Could not read mod container: "+candidates[i], e);
				continue;
			}
			for(JarEntry entry : Collections.list(jarfile.entries())) {
				if(entry.getName().toLowerCase().endsWith(".class")) {
					Class<?> clazz = null;
					try {
						clazz = classloader.loadClass(entry.getName().replace("/", "."));
					} catch (ClassNotFoundException e) {
						Se2Dlog.logErr("Could not load: "+entry.getName(), e);
						continue;
					}
					if(clazz.isAnnotationPresent(Mod.class)) {
						modClasses.add(clazz);
					}
				}
			}
		}
	}
	
}
