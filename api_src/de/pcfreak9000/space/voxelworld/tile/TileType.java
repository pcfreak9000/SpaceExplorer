package de.pcfreak9000.space.voxelworld.tile;

import de.omnikryptec.libapi.exposed.render.Texture;
import de.omnikryptec.resource.helper.TextureHelper;
import de.pcfreak9000.space.util.RegisterSensitive;

@RegisterSensitive(registry = "TILE_REGISTRY")
public class TileType {
    
    private String textureName = "";
    private Texture texture = null;
    
    private boolean canBreak = true;
    
    private float bouncyness = 0;
    
    public void setTexture(String name) {
        this.textureName = name;
    }
    
    public Texture getTexture() {
        return texture;
    }
    
    public void setBouncyness(float b) {
        this.bouncyness = b;
    }
    
    public float getBouncyness() {
        return this.bouncyness;
    }

    public void setCanBreak(boolean b) {
        this.canBreak = b;
    }
    
    public boolean canBreak() {
        return canBreak;
    }
    
    public void init(TextureHelper tileTextures) {
        this.texture = tileTextures.get(textureName);
    }
    
    @Override
    public String toString() {
        return String.format("TileType[texture=%s]", this.textureName);
    }
}
