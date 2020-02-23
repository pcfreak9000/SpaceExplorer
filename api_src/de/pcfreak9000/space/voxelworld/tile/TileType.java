package de.pcfreak9000.space.voxelworld.tile;

import de.omnikryptec.libapi.exposed.render.Texture;
import de.omnikryptec.resource.helper.TextureHelper;
import de.pcfreak9000.space.util.RegisterSensitive;

@RegisterSensitive(registry = "TILE_REGISTRY")
public class TileType {
    
    private String textureName = "";
    private Texture texture = null;
    
    public void setTexture(String name) {
        this.textureName = name;
    }
    
    public Texture getTexture() {
        return texture;
    }
    
    public void init(TextureHelper tileTextures) {
        this.texture = tileTextures.get(textureName);
    }
    
    @Override
    public String toString() {
        return String.format("TileType[texture=%s]", this.textureName);
    }
}
