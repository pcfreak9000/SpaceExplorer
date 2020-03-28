package de.pcfreak9000.space.core;

import de.omnikryptec.event.EventSubscription;
import de.pcfreak9000.space.voxelworld.Background;

public class BackgroundRegistry extends GameRegistry<Background> {
    
    public BackgroundRegistry() {
        Space.BUS.register(this);
    }
    
    @EventSubscription
    public void assignTextures(CoreEvents.AssignResourcesEvent ev) {
        LOGGER.info("Dispatching background textures...");
        registered.forEach((s, t) -> t.initTextures(ev.textures));
    }
    
}
