package de.pcfreak9000.spaceexplorer.game.core;

import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import de.omnikryptec.old.gameobject.Light2D;
import de.omnikryptec.old.gameobject.Sprite;
import de.omnikryptec.old.gameobject.component.PhysicsComponent2D;
import de.omnikryptec.old.main.OmniKryptecEngine;
import de.omnikryptec.old.physics.AdvancedBody;
import de.omnikryptec.old.physics.AdvancedRectangle;
import de.omnikryptec.old.settings.KeySettings;
import de.omnikryptec.old.util.EnumCollection.UpdateType;
import de.pcfreak9000.spaceexplorer.game.launch.KeyManager;
import de.pcfreak9000.spaceexplorer.game.launch.SpaceExplorer2D;
import de.omnikryptec.old.util.Instance;
import de.omnikryptec.old.resource.loader.ResourceLoader;

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
		body = new AdvancedBody();
		body.setBullet(true);
		body.setLinearDamping(20);
		body.addFixture(new AdvancedRectangle(getWidth(), getHeight() / 4f));
		body.setMass(MassType.FIXED_ANGULAR_VELOCITY);
		addComponent(new PhysicsComponent2D(body));
	}

	@Override
	protected void update() {
		Vector2 vel = new Vector2();
		float m = 1;
		if (keysettings.isPressed(KeyManager.KEY_FASTER)) {
			m = 20;
		}
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
		vel.multiply(m);
		// meh but makes it timeindependent
		body.applyVelocityImpulse(vel.multiply(Instance.getDeltaTimeSf() * 100));
		SpaceExplorer2D.getSpaceExplorer2D().getUniverse().getPlanetCamera().getTransform().setPosition(
				getTransform().getPosition(true).x + getWidth() / 2,
				getTransform().getPosition(true).y + getHeight() / 2, 0);
	}

}