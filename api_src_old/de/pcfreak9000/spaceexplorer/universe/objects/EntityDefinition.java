package de.pcfreak9000.spaceexplorer.universe.objects;

import de.omnikryptec.old.physics.AdvancedBody;
import de.pcfreak9000.space.util.RegisterSensitive;

@RegisterSensitive
public class EntityDefinition {
    
    private final String texture;
    
    public EntityDefinition(final String texture) {
        this.texture = texture;
    }
    
    public String getTexture() {
        return this.texture;
    }
    
    public Entity get(final float x, final float y) {
        final Entity r = new Entity(this);
        r.getTransform().setPosition(x, y);
        return r;
    }
    
    public void configureBody(final AdvancedBody body) {
        
    }
}
