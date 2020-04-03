package de.pcfreak9000.space.voxelworld.tile;

import de.omnikryptec.libapi.exposed.render.Texture;
import de.omnikryptec.resource.helper.TextureHelper;
import de.omnikryptec.util.Util;
import de.omnikryptec.util.data.Color;
import de.pcfreak9000.space.core.GameRegistry;
import de.pcfreak9000.space.util.RegisterSensitive;

@RegisterSensitive(registry = "TILE_REGISTRY")
public class TileType {
    
    public static final TileType AIR = new TileType();
    static {
        AIR.setBouncyness(0);
        AIR.setCanBreak(false);
        AIR.setFilterColor(null);
        AIR.setLightColor(null);
        AIR.setOpaque(false);
        AIR.setTexture(null);
        GameRegistry.TILE_REGISTRY.register("air", AIR);
    }
    
    private String textureName = null;
    private Texture texture = null;
    
    private boolean canBreak = true;
    private boolean opaque = false;
    
    public static final int MAX_LIGHT_VALUE = 16;
    private Color lightColor;
    private int lightValue = 10;
    private float attenuationFactor = 1;
    
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
    
    public int getLightRange() {
        return lightValue;
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
        if (this.textureName != null) {
            this.texture = tileTextures.get(textureName);
        }
    }
    
    @Override
    public String toString() {
        return String.format("TileType[texture=%s]", this.textureName);
    }
}
