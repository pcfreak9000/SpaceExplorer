package de.pcfreak9000.space.core;

import de.omnikryptec.resource.helper.TextureHelper;
import de.pcfreak9000.space.world.tile.TileType;

public class TileRegistry extends GameRegistry<TileType> {
    
    public void initAll(TextureHelper tileTextures) {
        LOGGER.info("Dispatching tile textures...");
        registered.forEach((s, t) -> t.init(tileTextures));
    }
}
