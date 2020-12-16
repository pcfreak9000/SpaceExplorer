package de.pcfreak9000.spaceexplorer.universe.objects;

import de.omnikryptec.old.gameobject.component.PhysicsComponent2D;
import de.omnikryptec.old.physics.AdvancedBody;
import de.omnikryptec.old.util.ConverterUtil;
import de.omnikryptec.old.util.EnumCollection.UpdateType;
import de.omnikryptec.render.objects.Sprite;
import de.pcfreak9000.space.core.registry.GameRegistry;

public class Entity extends Sprite {

    private final EntityDefinition mydef;
    private Sprite sprite;

    public Entity(final EntityDefinition def) {
        super(ResourceLoader.currentInstance().getTexture(def.getTexture()));
        GameRegistry.getWorldObjectRegistry().checkRegistered(def);
        this.mydef = def;
        this.sprite.setUpdateType(UpdateType.STATIC);
        addBody();
    }

    private void addBody() {
        final AdvancedBody body = new AdvancedBody();
        this.mydef.configureBody(body);
        body.getTransform()
                .setTranslation(ConverterUtil.convertToPhysics2D(this.sprite.getTransform().getPosition(true)));
        this.sprite.addComponent(new PhysicsComponent2D(body));

    }

    public EntityDefinition getDefinition() {
        return this.mydef;
    }

}
