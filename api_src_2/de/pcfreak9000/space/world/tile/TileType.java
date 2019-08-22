package de.pcfreak9000.space.world.tile;

import de.omnikryptec.libapi.exposed.render.Texture;
import de.omnikryptec.resource.loadervpc.TextureHelper;
import de.omnikryptec.util.data.Color;
import de.pcfreak9000.space.util.RegisterSensitive;

@RegisterSensitive(registry = "TILE_REGISTRY")
public class TileType {
    
    private String textureName = "";
    private Texture texture = null;
    
    private Color reflectiveness = new Color(0, 0, 0);
    
    public Color getReflectiveness() {
        return reflectiveness;
    }
    
    public void setTexture(String name) {
        this.textureName = name;
    }
    
    public void setReflectiveness(Color c) {
        this.reflectiveness = c;
    }
    
    public Texture getTexture() {
        return texture;
    }
    
    public void init(TextureHelper tileTextures) {
        this.texture = tileTextures.get(textureName);
    }
    
}
