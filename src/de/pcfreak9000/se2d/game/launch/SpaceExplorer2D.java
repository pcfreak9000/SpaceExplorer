package de.pcfreak9000.se2d.game.launch;

import java.util.Timer;
import java.util.TimerTask;

import de.codemakers.io.file.AdvancedFile;
import de.omnikryptec.event.eventV2.EventBus;
import de.omnikryptec.event.eventV2.EventSubscription;
import de.omnikryptec.event.eventV2.engineevents.FrameEvent;
import de.omnikryptec.event.eventV2.engineevents.FrameEvent.FrameType;
import de.omnikryptec.event.input.InputManager;
import de.omnikryptec.gui.ProgressBar;
import de.omnikryptec.main.OmniKryptecEngine;
import de.omnikryptec.resource.loader.ResourceLoader;
import de.omnikryptec.util.Instance;
import de.pcfreak9000.se2d.mod.ModManager;
import de.pcfreak9000.se2d.universe.Universe;

public class SpaceExplorer2D {

	private static final AdvancedFile RESOURCELOCATION = new AdvancedFile(true, "", "de", "pcfreak9000", "se2d", "res");
	private static final float[] PLANETPROJ = { -1920 / 2, -1080 / 2, 1920, 1080 };
	// private static final float[] PLANETPROJ = { -19200 / 2, -10800 / 2, 19200,
	// 10800 };

	public static final String EVENTBUSNAME = "SpaceExplorer2D_EVENT_BUS-2718";

	private static SpaceExplorer2D instance;

	public static SpaceExplorer2D getSpaceExplorer2D() {
		return instance;
	}

	private AdvancedFile resourcepacks, modsfolder;

	private ModManager manager;
	private EventBus se2d_events;
	private Universe currentWorld = null;

	public SpaceExplorer2D(AdvancedFile resourcepacksp, AdvancedFile modsfolderp) {
		if (instance != null) {
			throw new IllegalStateException("SpaceExplorer2D is already created!");
		}
		se2d_events = new EventBus(EVENTBUSNAME, 2, 2);
		instance = this;
		this.resourcepacks = resourcepacksp;
		if (!resourcepacks.toFile().exists()) {
			resourcepacks.setShouldBeFile(false);
			resourcepacks.toFile().mkdirs();
		}
		this.modsfolder = modsfolderp;
		if (!modsfolder.toFile().exists()) {
			modsfolder.setShouldBeFile(false);
			modsfolder.toFile().mkdirs();
		}
		ResourceLoader.createInstanceDefault(true, false);
		manager = new ModManager();
		manager.load(modsfolder);
		loadRes();
		currentWorld = new Universe();
		currentWorld.loadWorld(1);
		Instance.engineBus().registerEventHandler(this);
		// TEST
		ProgressBar b = new ProgressBar(null, null, 0.25f, 0.25f);
		b.getColor().set(0, 0.5f, 0.5f);
		b.getBarColor().set(0, 0, 1);
		b.setW(0.5f).setH(0.05f);

		ProgressBar b2 = new ProgressBar(null, null, 0.25f, 0.5f);
		b2.getColor().set(0, 0.5f, 0.5f);
		b2.getBarColor().set(1, 0, 0);
		b2.setW(0.5f).setH(0.025f);

		b.add(b2);
		Timer ttt = new Timer();
		ttt.schedule(new TimerTask() {

			@Override
			public void run() {
				if (b.getValue() < 1) {
					b.setValue(b.getValue() + 0.02f);
				} else {
					b.setValue(0);
					if (b2.getValue() == 1) {
						b2.setValue(0);
					}
					b2.setValue(b2.getValue() + 0.1f);

				}
			}
		}, 0, 10);
		OmniKryptecEngine.instance().setGui(b);
		// OmniKryptecEngine.instance().setGui(new
		// TexturedGuiObject(ResourceLoader.MISSING_TEXTURE, 0.75f,0.75f));
		// TEST
		OmniKryptecEngine.instance().startLoop();
	}

	public EventBus getEventBus() {
		return se2d_events;
	}

	private void loadRes() {
		ResourceLoader.currentInstance().stageAdvancedFiles(0, ResourceLoader.LOAD_XML_INFO, resourcepacks);
		ResourceLoader.currentInstance().loadStagedAdvancedFiles(true);
		ResourceLoader.currentInstance().clearStagedAdvancedFiles();
		ResourceLoader.currentInstance().stageAdvancedFiles(1, ResourceLoader.LOAD_XML_INFO, RESOURCELOCATION);
		ResourceLoader.currentInstance().loadStagedAdvancedFiles(false);
		ResourceLoader.currentInstance().clearStagedAdvancedFiles();
		manager.stageRessourceLoading();
		ResourceLoader.currentInstance().loadStagedAdvancedFiles(false);
		ResourceLoader.currentInstance().clearStagedAdvancedFiles();
	}

	public float[] getProjectionData() {
		return PLANETPROJ;
	}

	public Universe getUniverse() {
		return currentWorld;
	}

	@EventSubscription
	public void someFrameUpdateEventHandlerMethod(FrameEvent ev) {
		if (ev.getType() == FrameType.PRE && currentWorld != null) {
			System.out.println(InputManager.getMouseHandler().getPosition());
			currentWorld.update();
		}
	}

}
