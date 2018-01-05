package de.pcfreak9000.se2d.game;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;

import de.pcfreak9000.se2d.main.KeyManager;
import omnikryptec.gameobject.Light2D;
import omnikryptec.gameobject.Sprite;
import omnikryptec.gameobject.component.PhysicsComponent2D;
import omnikryptec.main.OmniKryptecEngine;
import omnikryptec.physics.Dyn4JPhysicsWorld;
import omnikryptec.resource.loader.ResourceLoader;
import omnikryptec.settings.KeySettings;
import omnikryptec.util.EnumCollection.UpdateType;
import omnikryptec.util.Instance;
import omnikryptec.util.SmoothFloat;

public class Player extends Sprite {

	private static final float DY_SPEED = 200;
	private static final float DX_SPEED = 200;

	private KeySettings keysettings;
	private Light2D light1;
	private Body body;
	
	public Player() {
		this.setUpdateType(UpdateType.DYNAMIC);
		this.keysettings = OmniKryptecEngine.instance().getGameSettings().getKeySettings();
		setTexture(ResourceLoader.currentInstance().getTexture("mensch.png"));
		getTransform().setScale(2f);
		setLayer(1);
		light1 = new Light2D(null, ResourceLoader.currentInstance().getTexture("light1.png"));
		light1.getTransform().setScale(1).increasePosition(getWidth()/2, getHeight()/2);
		light1.getColor().set(1, 0.5f, 0.5f);
		addChild(light1);
		body = new Body().setMass(MassType.NORMAL);
		body.addFixture(new Rectangle(20, 40));
		addComponent(new PhysicsComponent2D(body));
	}

	private SmoothFloat dx = new SmoothFloat(0, 10);
	private SmoothFloat dy = new SmoothFloat(0, 10);

	@Override
	protected void update() {
		Vector2 vel = new Vector2();
		if (keysettings.isPressed(KeyManager.KEY_PLAYER_MOVE_FORWARD)) {
			vel.y =DY_SPEED;
			//dy.setTarget(DY_SPEED*Instance.getDeltaTimeSf());
		} else if (keysettings.isPressed(KeyManager.KEY_PLAYER_MOVE_BACKWARD)) {
			vel.y = -DY_SPEED;
			//dy.setTarget(-DY_SPEED*Instance.getDeltaTimeSf());
		} else {
			//dy.setTarget(0);
		}
		if (keysettings.isPressed(KeyManager.KEY_PLAYER_MOVE_LEFT)) {
			vel.x = -DX_SPEED;
			//dx.setTarget(-DX_SPEED*Instance.getDeltaTimeSf());
		} else if (keysettings.isPressed(KeyManager.KEY_PLAYER_MOVE_RIGHT)) {
			vel.x = DX_SPEED;
			//dx.setTarget(DX_SPEED*Instance.getDeltaTimeSf());
		} else {
			//dx.setTarget(0);
		}
		//dx.update(Instance.getDeltaTimeSf());
		//dy.update(Instance.getDeltaTimeSf());
		body.setLinearVelocity(vel);
		System.out.println(body.getLinearVelocity());
		SpaceExplorer2D.getSpaceExplorer2D().getPlanetCamera().getTransform().setPosition(
				getTransform().getPosition(true).x + getWidth() / 2,
				getTransform().getPosition(true).y + getHeight() / 2, 0);
	}
	
}
