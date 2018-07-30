package de.pcfreak9000.se2d.universe.tiles;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.MassType;
import org.joml.Vector2f;

import omnikryptec.physics.AdvancedRectangle;
import omnikryptec.physics.Dyn4JPhysicsWorld;
import omnikryptec.util.ConverterUtil;
import omnikryptec.util.Instance;

public class StaticRectCollider {

	private Body body;

	public StaticRectCollider(float w, float h, Vector2f pos) {
		body = new Body();
		body.addFixture(new AdvancedRectangle(TileDefinition.TILE_SIZE, TileDefinition.TILE_SIZE));
		body.getFixture(0).setRestitution(0.1);
		body.getTransform().setTranslation(ConverterUtil.convertToPhysics2D(pos));
		// body.getTransform().translate(ConverterUtil.convertToPhysics2D(0),
		// ConverterUtil.convertToPhysics2D(0));
		body.setMass(MassType.INFINITE);
	}

	public StaticRectCollider add() {
		((Dyn4JPhysicsWorld) Instance.getCurrent2DScene().getPhysicsWorld()).getWorld().addBody(body);
		return this;
	}

	public Body getBody() {
		return body;
	}

}
