package de.pcfreak9000.se2d.game;

import de.codemakers.io.file.AdvancedFile;
import de.pcfreak9000.se2d.planet.Planet;
import omnikryptec.event.event.EventSystem;
import omnikryptec.event.event.EventType;
import omnikryptec.gameobject.Camera;
import omnikryptec.gameobject.GameObject3D;
import omnikryptec.main.OmniKryptecEngine;
import omnikryptec.resource.loader.ResourceLoader;
import omnikryptec.resource.texture.Texture;
import omnikryptec.util.Instance;

public class SpaceExplorer2D {

	private static final AdvancedFile RESOURCELOCATION = new AdvancedFile(true, "", "de", "pcfreak9000", "se2d", "res");
	private static final float[] PLANETPROJ = {-1920/2,-1080/2,1920, 1080};
	
	private static SpaceExplorer2D instance;
	
	public static SpaceExplorer2D getSpaceExplorer2D() {
		return instance;
	}
	
//	public static EventType EVENT_RES_RELOADING = new EventType("EVENT_RES_RELOADING", true);
//	
//	static {
//		Instance.getEventSystem().addEventType(EVENT_RES_RELOADING);
//	}
	
	private AdvancedFile resourcepacks;
	private Player currentPlayer = null;
	private Camera planetCamera;
	
	public SpaceExplorer2D(AdvancedFile resourcepacks) {
		if(instance!=null) {
			throw new IllegalStateException("SpaceExplorer2D is already created!");
		}
		instance = this;
		this.resourcepacks = resourcepacks;
		ResourceLoader.createInstanceDefault(true);
		loadRes();
		loadWorld();
		OmniKryptecEngine.instance().startLoop();
	}

	private void loadRes() {
		ResourceLoader.currentInstance().clearStagedAdvancedFiles();
		ResourceLoader.currentInstance().stageAdvancedFiles(0, resourcepacks);
		ResourceLoader.currentInstance().stageAdvancedFiles(1, RESOURCELOCATION);
		ResourceLoader.currentInstance().loadStagedAdvancedFiles(true);
		ResourceLoader.currentInstance().actions(Texture.class, (t)->t.invertV());
	}
	
	private void loadWorld() {
		planetCamera = new Camera().setOrthographicProjection2D(PLANETPROJ);
		currentPlayer = new Player();
		setPlanetAndPlayer(new Planet("Deine Mutter"), currentPlayer);
	}
	
	
	private void setPlanetAndPlayer(Planet planet, Player player) {
		planet.setAsScene(player);
	}

	public Camera getPlanetCamera() {
		return planetCamera;
	}
	
	public float[] getProjectionData() {
		return PLANETPROJ;
	}
	
}
