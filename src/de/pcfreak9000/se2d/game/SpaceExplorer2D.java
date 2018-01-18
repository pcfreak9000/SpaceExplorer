package de.pcfreak9000.se2d.game;

import de.codemakers.io.file.AdvancedFile;
import de.pcfreak9000.se2d.universe.Universe;
import de.pcfreak9000.se2d.universe.planet.biome.BiomeRegistry;
import de.pcfreak9000.se2d.universe.planet.biome.DefaultBiome;
import de.pcfreak9000.se2d.universe.planet.biome.DefaultBiome2;
import omnikryptec.event.event.Event;
import omnikryptec.event.event.EventType;
import omnikryptec.event.event.IEventHandler;
import omnikryptec.main.OmniKryptecEngine;
import omnikryptec.resource.loader.ResourceLoader;
import omnikryptec.resource.texture.Texture;

public class SpaceExplorer2D implements IEventHandler {

	private static final AdvancedFile RESOURCELOCATION = new AdvancedFile(true, "", "de", "pcfreak9000", "se2d", "res");
	private static final float[] PLANETPROJ = { -1920 / 2, -1080 / 2, 1920, 1080 };
	// private static final float[] PLANETPROJ = { -19200 / 2, -10800 / 2, 19200,
	// 10800 };

	private static SpaceExplorer2D instance;

	public static SpaceExplorer2D getSpaceExplorer2D() {
		return instance;
	}

	private AdvancedFile resourcepacks;
	private Universe currentWorld = null;

	public SpaceExplorer2D(AdvancedFile resourcepacks) {
		if (instance != null) {
			throw new IllegalStateException("SpaceExplorer2D is already created!");
		}
		instance = this;
		this.resourcepacks = resourcepacks;
		ResourceLoader.createInstanceDefault(true, false);
		OmniKryptecEngine.instance().getEventsystem().addEventHandler(this, EventType.BEFORE_FRAME);
		loadRes();
		BiomeRegistry.registerBiomeDefinition(1, new DefaultBiome());
		BiomeRegistry.registerBiomeDefinition(1, new DefaultBiome2());
		currentWorld = new Universe();
		currentWorld.loadWorld();
		OmniKryptecEngine.instance().startLoop();
	}

	private void loadRes() {
		ResourceLoader.currentInstance().clearStagedAdvancedFiles();
		ResourceLoader.currentInstance().stageAdvancedFiles(0, 0, resourcepacks);
		ResourceLoader.currentInstance().stageAdvancedFiles(1, ResourceLoader.LOAD_XML_INFO, RESOURCELOCATION);
		ResourceLoader.currentInstance().loadStagedAdvancedFiles(true);
		ResourceLoader.currentInstance().actions(Texture.class, (t) -> t.invertV());
	}

	public float[] getProjectionData() {
		return PLANETPROJ;
	}

	public Universe getUniverse() {
		return currentWorld;
	}

	@Override
	public void onEvent(Event ev) {
		if (ev.getType() == EventType.BEFORE_FRAME && currentWorld != null) {
			currentWorld.update();
		}
	}

}
