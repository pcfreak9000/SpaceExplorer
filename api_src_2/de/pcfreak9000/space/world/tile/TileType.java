package de.pcfreak9000.space.world.tile;

import de.omnikryptec.libapi.exposed.render.Texture;
import de.omnikryptec.resource.loadervpc.TextureHelper;
import de.omnikryptec.util.data.Color;
import de.pcfreak9000.space.util.RegisterSensitive;

@RegisterSensitive
public class TileType {
    
    private String textureName;
    private Texture texture;
    
    private Color reflectiveness;
    
    public Color getReflectiveness() {
        return reflectiveness;
    }
    
    public Texture getTexture() {
        return texture;
    }
    
    public void init(TextureHelper tileTextures) {
        this.texture = tileTextures.get(textureName);
    }
    
}
