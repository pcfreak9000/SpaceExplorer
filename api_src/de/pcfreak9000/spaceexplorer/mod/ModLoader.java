package de.pcfreak9000.spaceexplorer.mod;

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

import de.codemakers.io.file.AdvancedFile;
import de.omnikryptec.old.util.logger.LogLevel;
import de.omnikryptec.old.util.logger.Logger;
import de.pcfreak9000.spaceexplorer.game.launch.Launcher;
import de.pcfreak9000.spaceexplorer.game.launch.SpaceExplorer2D;
import de.pcfreak9000.spaceexplorer.mod.event.Se2DModInitEvent;
import de.pcfreak9000.spaceexplorer.mod.event.Se2DModPostInitEvent;
import de.pcfreak9000.spaceexplorer.mod.event.Se2DModPreInitEvent;
import de.pcfreak9000.spaceexplorer.util.Private;
import de.pcfreak9000.spaceexplorer.util.Se2Dlog;
import de.omnikryptec.old.resource.loader.ResourceLoader;

/**
 * loads mods.
 * 
 * @author pcfreak9000
 *
 */
@Private
public class ModLoader {

	public static final String THIS_INSTANCE_ID = "this";
	
	private static class TmpHolder {
		private File file;
		private Class<?> modclass;

		private TmpHolder(Class<?> clazz, File file) {
			this.file = file;
			this.modclass = clazz;
		}
	}

	private static Comparator<TmpHolder> comp = new Comparator<TmpHolder>() {

		@Override
		public int compare(TmpHolder o1, TmpHolder o2) {
			Mod m1 = o1.modclass.getAnnotation(Mod.class);
			Mod m2 = o2.modclass.getAnnotation(Mod.class);
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

	private List<TmpHolder> modClasses = new ArrayList<>();

	void preInit() {
		Logger.log("Pre-Init Event...");
		new Se2DModPreInitEvent().call();
	}

	void init() {
		Logger.log("Init Event...");
		new Se2DModInitEvent().call();
	}

	void postInit() {
		Logger.log("Post-Init Event...");
		new Se2DModPostInitEvent().call();
	}

	private boolean contains(Object o, Object[] os) {
		for (Object po : os) {
			if (po.equals(o)) {
				return true;
			}
		}
		return false;
	}

	void instantiate(List<ModContainer> containers) {
		Logger.log("Instantiating mods...");
		for (TmpHolder th : modClasses) {
			Class<?> cl = th.modclass;
			Object instance = null;
			try {
				cl.getConstructor().setAccessible(true);
				instance = cl.newInstance();
			} catch (InstantiationException | NoSuchMethodException e) {
				Se2Dlog.log(LogLevel.ERROR,
						"Mod could not be instantiated. Make sure a nullary-constructor is available and your mod class is non-abstract etc: "
								+ cl.getAnnotation(Mod.class).id());
				continue;
			} catch (IllegalAccessException | SecurityException e) {
				Se2Dlog.logErr("Illegal Access: " + cl.getAnnotation(Mod.class).id(), e);
				continue;
			} catch (LinkageError e) {
				Se2Dlog.log(LogLevel.ERROR, "Incompatible Mod: " + th.modclass);
				continue;
			}
			if(cl.getAnnotation(Mod.class).id().equals(THIS_INSTANCE_ID)) {
				Se2Dlog.log(LogLevel.ERROR, "The String \""+THIS_INSTANCE_ID+"\" can not be used as Mod-ID: "+cl);
				continue;
			}
			ModContainer container = new ModContainer(cl, cl.getAnnotation(Mod.class), instance);
			if (containers.contains(container)) {
				Se2Dlog.log(LogLevel.INFO, "Skipping already loaded mod: " + container.getMod().id() + " (version "
						+ Arrays.toString(container.getMod().version()) + ")");
				continue;
			} else {
				Logger.log("Instantiated mod: " + container);
				containers.add(container);
			}
			if (!contains(Launcher.VERSION, container.getMod().se2dversion())) {
				Se2Dlog.log(LogLevel.WARNING,
						"The mod " + container + " may not be compatible with this Se2D-Version!");
			}
		}
	}

	void stageRes() {
		for (TmpHolder th : modClasses) {
			ResourceLoader.currentInstance().stageAdvancedFiles(1, ResourceLoader.LOAD_XML_INFO,
					new AdvancedFile(false, th.file.getAbsolutePath()));
		}
	}

	void registerEvents(List<ModContainer> containers) {
		Logger.log("Registering container event handlers...");
		for (ModContainer container : containers) {
			SpaceExplorer2D.getSpaceExplorer2D().getEventBus().registerEventHandler(container.getInstance());
		}
	}

	void dispatchInstances(List<ModContainer> containers) {
		Logger.log("Dispatching instances...");
		for (ModContainer container : containers) {
			Field[] fields = container.getModClass().getDeclaredFields();
			for (Field f : fields) {
				f.setAccessible(true);
				if (f.isAnnotationPresent(Instance.class)) {
					Instance wanted = f.getAnnotation(Instance.class);
					if (wanted.id().equals(container.getMod().id()) || wanted.id().equals(THIS_INSTANCE_ID)) {
						try {
							f.set(container.getInstance(), container.getInstance());
						} catch (IllegalArgumentException e) {
							Se2Dlog.log(LogLevel.WARNING, "Wrong arg @ " + container);
						} catch (IllegalAccessException e) {
							Se2Dlog.log(LogLevel.WARNING, "Illegal access @ " + container);
						}
					} else {
						boolean found = false;
						for (ModContainer wantedContainer : containers) {
							if (wantedContainer == container) {
								continue;
							}
							if (wantedContainer.getMod().id().equals(wanted.id())) {
								found = true;
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
										Se2Dlog.log(LogLevel.WARNING, "Wrong arg @ " + container);
									} catch (IllegalAccessException e) {
										Se2Dlog.log(LogLevel.WARNING, "Illegal access @ " + container);
									}
								} else {
									Logger.log(wantedContainer + " is not accessible for the mod " + container);
								}
								break;
							}
						}
						if (!found) {
							Se2Dlog.log(LogLevel.WARNING, "Could not find " + wanted.id());
						}
					}
				}
			}
		}
	}

	void classLoadMods(File moddir) {
		List<File> candidates = new ArrayList<>();
		new ModDiscoverer().discover(candidates, moddir);
		load(candidates.toArray(new File[candidates.size()]));
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
		URLClassLoader classloader = new URLClassLoader(urlarray);
		for (int i = 0; i < candidates.length; i++) {
			JarFile jarfile = null;
			try {
				jarfile = new JarFile(candidates[i]);

				for (JarEntry entry : Collections.list(jarfile.entries())) {
					if (entry.getName().toLowerCase().endsWith(".class")) {
						Class<?> clazz = null;
						try {
							clazz = classloader.loadClass(entry.getName().replace("/", ".").replace(".class", ""));
						} catch (ClassNotFoundException e) {
							Se2Dlog.log(LogLevel.ERROR, "Could not load: " + entry.getName());
							continue;
						} catch (LinkageError e) {
							Se2Dlog.log(LogLevel.INFO, "Unexpected behaviour of a class detected: "
									+ entry.getName().replace("/", ".").replace(".class", ""));
							continue;
						}
						if (clazz.isAnnotationPresent(Mod.class)) {
							modClasses.add(new TmpHolder(clazz, candidates[i]));
						}
					}
				}
			} catch (IOException e) {
				Se2Dlog.log(LogLevel.WARNING, "Could not read mod container: " + candidates[i]);
				continue;
			} finally {
				if (jarfile != null) {
					try {
						jarfile.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		try {
			classloader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Naja ist doof...
		// SpaceExplorer2D.getSpaceExplorer2D().getEventBus().findStaticEventAnnotations(classloader,
		// null);
		modClasses.sort(comp);
	}

}
