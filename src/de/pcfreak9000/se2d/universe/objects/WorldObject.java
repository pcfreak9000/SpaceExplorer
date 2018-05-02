package de.pcfreak9000.se2d.universe.objects;

import de.pcfreak9000.se2d.game.core.GameRegistry;
import omnikryptec.gameobject.Sprite;
import omnikryptec.gameobject.component.PhysicsComponent2D;
import omnikryptec.physics.AdvancedBody;
import omnikryptec.physics.AdvancedRectangle;
import omnikryptec.resource.loader.ResourceLoader;
import omnikryptec.util.ConverterUtil;

public class WorldObject extends Sprite {

	private WorldObjectDefinition mydef;

	public WorldObject(WorldObjectDefinition def) {
		this(def, 0, 0, 0, 0);
	}
	
	public WorldObject(WorldObjectDefinition def, float w, float h, float offsetx, float offsety) {
		super(ResourceLoader.MISSING_TEXTURE);
		GameRegistry.getWorldObjectRegistry().checkRegistered(def);
		this.mydef = def;
		addBody(w, h, offsetx, offsety);
	}

	private void addBody(float collisionWidth, float collisionHeight, float offsetx, float offsety) {
		if (collisionWidth != 0 && collisionHeight != 0) {
			AdvancedBody body = new AdvancedBody().setOffsetXY(offsetx, offsety);
			body.getTransform().setTranslation(ConverterUtil.convertToPhysics2D(this.getTransform().getPosition(true)));
			body.addFixture(new AdvancedRectangle(collisionWidth, collisionHeight));
			this.addComponent(new PhysicsComponent2D(body));
		}
	}

	public WorldObjectDefinition getDefinition() {
		return mydef;
	}
	
}
