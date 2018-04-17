package de.pcfreak9000.se2d.mod;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import de.pcfreak9000.se2d.game.Launcher;
import de.pcfreak9000.se2d.util.Se2Dlog;
import omnikryptec.event.eventV2.EventSystem;
import omnikryptec.util.logger.LogLevel;

public class ModLoader {

	private static Comparator<Class<?>> comp = new Comparator<Class<?>>() {

		@Override
		public int compare(Class<?> o1, Class<?> o2) {
			Mod m1 = o1.getAnnotation(Mod.class);
			Mod m2 = o2.getAnnotation(Mod.class);
			int vt = m1.id().compareToIgnoreCase(m2.id());
			if (vt != 0) {
				return vt;
			}
			long[] a1 = m1.version();
			long[] a2 = m2.version();
			for (int i = 0; i < Math.min(a1.length, a2.length); i++) {
				long d = a1[i] - a2[i];
				if (d != 0) {
					return (int) Math.signum(d);
				}
			}
			return a1.length - a2.length;
		}
	};

	private List<Class<?>> modClasses = new ArrayList<>();

	void preInit() {

	}

	void init() {

	}

	void postInit() {

	}

	private boolean contains(Object o, Object[] os) {
		for (Object po : os) {
			if (po.equals(o)) {
				return true;
			}
		}
		return false;
	}

	List<ModContainer> instantiate() {
		Se2Dlog.log("Instantiating mods...");
		List<ModContainer> containers = new ArrayList<>();
		for (Class<?> cl : modClasses) {
			Object instance = null;
			try {
				cl.getConstructor().setAccessible(true);
				instance = cl.newInstance();
			} catch (InstantiationException | NoSuchMethodException e) {
				Se2Dlog.logErr(
						"Mod could not be instantiated. Make sure you supply a nullary-constructor and your mod class is non-abstract etc: "
								+ cl.getAnnotation(Mod.class).id(),
						e);
				continue;
			} catch (IllegalAccessException | SecurityException e) {
				Se2Dlog.logErr("Illegal Access: " + cl.getAnnotation(Mod.class).id(), e);
				continue;
			}
			ModContainer container = new ModContainer(cl, cl.getAnnotation(Mod.class), instance);
			if (containers.contains(container)) {
				Se2Dlog.log(LogLevel.INFO, "Skipping already loaded mod: " + container.getMod().id() + " (version "
						+ Arrays.toString(container.getMod().version()) + ")");
				continue;
			} else {
				Se2Dlog.log("Found mod: "+container);
				containers.add(container);
			}
			if (!contains(Launcher.VERSION, container.getMod().se2dversion())) {
				Se2Dlog.log(LogLevel.WARNING,
						"The mod " + container + " may not be compatible with this Se2D-Version!");
			}
		}
		for (ModContainer container : containers) {
			Field[] fields = container.getModClass().getDeclaredFields();
			for (Field f : fields) {
				f.setAccessible(true);
				if (f.isAnnotationPresent(Instance.class)) {
					Instance wanted = f.getAnnotation(Instance.class);
					if (wanted.id().equals(container.getMod().id())) {
						try {
							f.set(container.getInstance(), container.getInstance());
						} catch (IllegalArgumentException e) {
							Se2Dlog.logErr("Wrong arg @ " + container, e);
						} catch (IllegalAccessException e) {
							Se2Dlog.logErr("Illegal access @ " + container, e);
						}
					} else {
						for (ModContainer wantedContainer : containers) {
							if (wantedContainer == container) {
								continue;
							}
							if (wantedContainer.getMod().id().equals(wanted.id())) {
								if (wanted.requiredVersion().length > 0) {
									if (!Arrays.equals(wantedContainer.getMod().version(), wanted.requiredVersion())) {
										Se2Dlog.log(LogLevel.WARNING,
												"The mod " + container + " requires the version "
														+ Arrays.toString(wanted.requiredVersion()) + " from the mod "
														+ wantedContainer);
										break;
									}
								}
								if (wantedContainer.getMod().accessible()) {
									try {
										f.set(container.getInstance(), wantedContainer.getInstance());
									} catch (IllegalArgumentException e) {
										Se2Dlog.logErr("Wrong arg @ " + container, e);
									} catch (IllegalAccessException e) {
										Se2Dlog.logErr("Illegal access @ " + container, e);
									}
								} else {
									Se2Dlog.log(wantedContainer + " is not accessible for the mod " + container + "!");
								}
								break;
							}
						}
					}
				}
			}
		}
		return containers;
	}

	void classLoadMods(File moddir) {
		List<File> candidates = new ArrayList<>();
		new ModDiscoverer().discover(candidates, moddir);
		load(candidates.toArray(new File[0]));
	}

	@SuppressWarnings("resource")
	private void load(File[] candidates) {
		URL[] urlarray = new URL[candidates.length];
		for (int i = 0; i < urlarray.length; i++) {
			try {
				urlarray[i] = candidates[i].toURI().toURL();
			} catch (MalformedURLException e) {
				Se2Dlog.logErr("Could not create some URL: " + candidates[i], e);
			}
		}
		ClassLoader classloader = new URLClassLoader(urlarray);
		for (int i = 0; i < candidates.length; i++) {
			JarFile jarfile;
			try {
				jarfile = new JarFile(candidates[i]);
			} catch (IOException e) {
				Se2Dlog.logErr("Could not read mod container: " + candidates[i], e);
				continue;
			}
			for (JarEntry entry : Collections.list(jarfile.entries())) {
				if (entry.getName().toLowerCase().endsWith(".class")) {
					Class<?> clazz = null;
					try {
						clazz = classloader.loadClass(entry.getName().replace("/", ".").replace(".class", ""));
					} catch (ClassNotFoundException e) {
						Se2Dlog.logErr("Could not load: " + entry.getName(), e);
						continue;
					}
					if (clazz.isAnnotationPresent(Mod.class)) {
//						Mod mod = clazz.getAnnotation(Mod.class);
//						Se2Dlog.log(LogLevel.FINE, "The mod with ID " + mod.id() + ", namely " + mod.name()
//								+ ", in version " + Arrays.toString(mod.version()) + " has been discovered!");
						modClasses.add(clazz);
					}
				}
			}
		}
		modClasses.sort(comp);
		Se2Dlog.log("Finding and adding event-handling methods...");
		EventSystem.findEventAnnotations(classloader, null);
	}

}
