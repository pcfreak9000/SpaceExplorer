package de.pcfreak9000.se2d.game;

import org.lwjgl.glfw.GLFWKeyCallback;

import de.pcfreak9000.se2d.main.Launcher;
import de.pcfreak9000.se2d.planet.Planet;
import omnikryptec.display.GLFWInfo;
import omnikryptec.event.input.InputManager;
import omnikryptec.gameobject.Camera;
import omnikryptec.gameobject.Sprite;
import omnikryptec.main.OmniKryptecEngine;
import omnikryptec.resource.loader.ResourceLoader;
import omnikryptec.settings.KeySettings;
import omnikryptec.util.EnumCollection.UpdateType;
import omnikryptec.util.Instance;
import omnikryptec.util.SmoothFloat;

public class Player extends Sprite{
	
	private Planet currentPlanet;
	private KeySettings keysettings;
	
	private Camera planetCamera;
	
	public Player(Camera planetcam) {
		this.setUpdateType(UpdateType.DYNAMIC);
		this.setGlobal(true);
		this.keysettings = OmniKryptecEngine.instance().getGameSettings().getKeySettings();
		this.planetCamera = planetcam;
		setTexture(ResourceLoader.currentInstance().getTexture("sfdsdfsdf"));
	}
	
	private SmoothFloat dx = new SmoothFloat(0, 10), dy = new SmoothFloat(0, 10);
	private static final float SPEED = 2;
	
	@Override
	protected void update() {
		if(keysettings.isPressed(Launcher.KEY_PLAYER_MOVE_FORWARD)) {
			dy.setTarget(SPEED);
		}else if(keysettings.isPressed(Launcher.KEY_PLAYER_MOVE_BACKWARD)) {
			dy.setTarget(-SPEED);
		}else {
			dy.setTarget(0);
		}
		if(keysettings.isPressed(Launcher.KEY_PLAYER_MOVE_LEFT)) {
			dx.setTarget(-SPEED);
		}else if(keysettings.isPressed(Launcher.KEY_PLAYER_MOVE_RIGHT)) {
			dx.setTarget(SPEED);
		}else {
			dx.setTarget(0);
		}
		dx.update();
		dy.update();
		getTransform().increasePosition(dx.get(), dy.get());
		planetCamera.getTransform().setPosition(getTransform().getPosition(true).x, getTransform().getPosition(true).y, 0);
	}
	
	public Camera getPlanetCamera() {
		return planetCamera;
	}
	
}
