package de.pcfreak9000.se2d.universe.objects;

import de.omnikryptec.old.gameobject.Sprite;
import de.omnikryptec.old.gameobject.component.PhysicsComponent2D;
import de.omnikryptec.old.physics.AdvancedBody;
import de.omnikryptec.old.util.ConverterUtil;
import de.omnikryptec.old.util.EnumCollection.UpdateType;
import de.omnikryptec.old.resource.loader.ResourceLoader;
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
