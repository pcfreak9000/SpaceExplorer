package de.pcfreak9000.se2d.universe.objects;

import de.pcfreak9000.se2d.game.core.GameRegistry;
import omnikryptec.gameobject.Sprite;
import omnikryptec.gameobject.component.PhysicsComponent2D;
import omnikryptec.physics.AdvancedBody;
import omnikryptec.physics.AdvancedRectangle;
import omnikryptec.resource.loader.ResourceLoader;
import omnikryptec.util.ConverterUtil;
import omnikryptec.util.EnumCollection.UpdateType;

public class Entity {

	private EntityDefinition mydef;
	private Sprite sprite;
	
	public Entity(EntityDefinition def) {
		sprite = new Sprite(ResourceLoader.MISSING_TEXTURE);
		//super(ResourceLoader.MISSING_TEXTURE);
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
