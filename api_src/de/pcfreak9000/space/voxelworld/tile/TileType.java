package de.pcfreak9000.space.voxelworld.tile;

import de.omnikryptec.libapi.exposed.render.Texture;
import de.omnikryptec.resource.helper.TextureHelper;
import de.omnikryptec.util.data.Color;
import de.pcfreak9000.space.util.RegisterSensitive;

@RegisterSensitive(registry = "TILE_REGISTRY")
public class TileType {
    
    private String textureName = "";
    private Texture texture = null;
    
    private boolean canBreak = true;
    private boolean opaque = true;
    
    private Color lightColor;
    private Color filterColor;
    
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
        return this.canBreak;
    }
    
    public void setOpaque(boolean b) {
        this.opaque = b;
    }
    
    public boolean isOpaque() {
        return this.opaque;
    }
    
    public void setLightColor(Color color) {
        this.lightColor = color;
    }
    
    public Color getLightColor() {
        return this.lightColor;
    }
    
    public boolean hasLight() {
        return this.lightColor != null;
    }
    
    public void setFilterColor(Color color) {
        this.filterColor = color;
    }
    
    public Color getFilterColor() {
        return this.filterColor;
    }
    
    public boolean hasLightFilter() {
        return this.filterColor != null;
    }
    
    public void init(TextureHelper tileTextures) {
        this.texture = tileTextures.get(textureName);
    }
    
    @Override
    public String toString() {
        return String.format("TileType[texture=%s]", this.textureName);
    }
}
