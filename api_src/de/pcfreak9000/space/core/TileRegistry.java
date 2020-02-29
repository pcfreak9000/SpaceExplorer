package de.pcfreak9000.space.core;

import de.omnikryptec.event.EventSubscription;
import de.pcfreak9000.space.voxelworld.tile.TileType;

public class TileRegistry extends GameRegistry<TileType> {
    
    public TileRegistry() {
        Space.BUS.register(this);
    }
    
    @EventSubscription
    public void assignTextures(CoreEvents.AssignResourcesEvent ev) {
        LOGGER.info("Dispatching tile textures...");
        registered.forEach((s, t) -> t.init(ev.textures));
    }
}
