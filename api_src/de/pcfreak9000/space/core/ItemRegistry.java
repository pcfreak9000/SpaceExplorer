package de.pcfreak9000.space.core;

import de.omnikryptec.event.EventSubscription;
import de.pcfreak9000.space.item.Item;

public class ItemRegistry extends GameRegistry<Item> {
    
    public ItemRegistry() {
        Space.BUS.register(this);
    }
    
    @EventSubscription
    public void assignTextures(CoreEvents.AssignResourcesEvent ev) {
        this.LOGGER.info("Dispatching item textures...");
        this.registered.forEach((s, i) -> i.init(ev.textures));
    }
}
