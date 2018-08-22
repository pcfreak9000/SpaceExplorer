package de.pcfreak9000.se2d.game.launch;

import de.codemakers.io.file.AdvancedFile;
import de.pcfreak9000.se2d.mod.ModManager;
import de.pcfreak9000.se2d.universe.Universe;
import omnikryptec.event.eventV2.EventBus;
import omnikryptec.event.eventV2.EventSubscription;
import omnikryptec.event.eventV2.engineevents.FrameEvent;
import omnikryptec.event.eventV2.engineevents.FrameEvent.FrameType;
import omnikryptec.gui.TexturedGuiObject;
import omnikryptec.main.OmniKryptecEngine;
import omnikryptec.resource.loader.ResourceLoader;
import omnikryptec.resource.texture.Texture;
import omnikryptec.util.Instance;

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
		OmniKryptecEngine.instance().setGui(new TexturedGuiObject(ResourceLoader.MISSING_TEXTURE, 0.75f,0.75f));
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
			currentWorld.update();
		}
	}

}
