package de.pcfreak9000.se2d.game;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;
import org.joml.Vector2f;

import de.pcfreak9000.se2d.main.KeyManager;
import omnikryptec.gameobject.Light2D;
import omnikryptec.gameobject.Sprite;
import omnikryptec.gameobject.component.PhysicsComponent2D;
import omnikryptec.main.OmniKryptecEngine;
import omnikryptec.physics.AdvancedBody;
import omnikryptec.physics.AdvancedRectangle;
import omnikryptec.physics.Dyn4JPhysicsWorld;
import omnikryptec.resource.loader.ResourceLoader;
import omnikryptec.settings.KeySettings;
import omnikryptec.util.EnumCollection.UpdateType;
import omnikryptec.util.ConverterUtil;
import omnikryptec.util.Instance;
import omnikryptec.util.SmoothFloat;

public class Player extends Sprite {

	private static final float DY_SPEED = 2f;
	private static final float DX_SPEED = 2f;

	private KeySettings keysettings;
	private Light2D light1;
	private AdvancedBody body;

	public Player() {
		this.setUpdateType(UpdateType.DYNAMIC);
		this.keysettings = OmniKryptecEngine.instance().getGameSettings().getKeySettings();
		setTexture(ResourceLoader.currentInstance().getTexture("mensch.png"));
		getTransform().setScale(2f);
		setLayer(1);
		light1 = new Light2D(null, ResourceLoader.currentInstance().getTexture("light1.png"));
		light1.getTransform().setScale(1).increasePosition(getWidth() / 2, getHeight() / 2);
		light1.getColor().set(1, 1, 1);
		addChild(light1);
		body = new AdvancedBody().setOffsetXY(0, getHeight()/4);
		body.setLinearDamping(20);
		body.addFixture(new AdvancedRectangle(getWidth(), getHeight()/4));
		body.setMass(MassType.FIXED_ANGULAR_VELOCITY);
		addComponent(new PhysicsComponent2D(body));
	}

	@Override
	protected void update() { 
		Vector2 vel = new Vector2();
		if (keysettings.isPressed(KeyManager.KEY_PLAYER_MOVE_FORWARD)) {
			vel.y = DY_SPEED;
		} else if (keysettings.isPressed(KeyManager.KEY_PLAYER_MOVE_BACKWARD)) {
			vel.y = -DY_SPEED;
		}
		if (keysettings.isPressed(KeyManager.KEY_PLAYER_MOVE_LEFT)) {
			vel.x = -DX_SPEED;
		} else if (keysettings.isPressed(KeyManager.KEY_PLAYER_MOVE_RIGHT)) {
			vel.x = DX_SPEED;
		}
		//getTransform().increasePosition((float)vel.x, (float)vel.y);
		body.applyVelocityImpulse(vel);
		SpaceExplorer2D.getSpaceExplorer2D().getPlanetCamera().getTransform().setPosition(
				getTransform().getPosition(true).x + getWidth() / 2,
				getTransform().getPosition(true).y + getHeight() / 2, 0);
	}

}
