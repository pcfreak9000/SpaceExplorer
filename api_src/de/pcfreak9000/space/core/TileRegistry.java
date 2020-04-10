package de.pcfreak9000.space.core;

import de.omnikryptec.event.EventSubscription;
import de.pcfreak9000.space.tileworld.tile.Tile;

public class TileRegistry extends GameRegistry<Tile> {

    public TileRegistry() {
        Space.BUS.register(this);
    }

    @EventSubscription
    public void assignTextures(CoreEvents.AssignResourcesEvent ev) {
        this.LOGGER.info("Dispatching tile textures...");
        this.registered.forEach((s, t) -> t.init(ev.textures));
    }
}
