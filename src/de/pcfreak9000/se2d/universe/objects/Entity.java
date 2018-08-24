package de.pcfreak9000.se2d.universe.objects;

import de.omnikryptec.gameobject.Sprite;
import de.omnikryptec.gameobject.component.PhysicsComponent2D;
import de.omnikryptec.physics.AdvancedBody;
import de.omnikryptec.resource.loader.ResourceLoader;
import de.omnikryptec.util.ConverterUtil;
import de.omnikryptec.util.EnumCollection.UpdateType;
import de.pcfreak9000.se2d.game.core.GameRegistry;

public class Entity extends Sprite {

	private EntityDefinition mydef;
	private Sprite sprite;

	public Entity(EntityDefinition def) {
		super(ResourceLoader.currentInstance().getTexture(def.getTexture()));
		GameRegistry.getWorldObjectRegistry().checkRegistered(def);
		this.mydef = def;
		sprite.setUpdateType(UpdateType.STATIC);
		addBody();
	}

	private void addBody() {
		AdvancedBody body = new AdvancedBody();
		mydef.configureBody(body);
		body.getTransform().setTranslation(ConverterUtil.convertToPhysics2D(sprite.getTransform().getPosition(true)));
		sprite.addComponent(new PhysicsComponent2D(body));

	}

	public EntityDefinition getDefinition() {
		return mydef;
	}

}
