package de.pcfreak9000.space.item;

import de.omnikryptec.libapi.exposed.render.Texture;
import de.omnikryptec.resource.helper.TextureHelper;

/**
 * represents an Item
 *
 * @author pcfreak9000
 *
 */
public class Item {
    
    private String textureName;
    private Texture texture;
    
    private String displayName;
    private String description;
    private int maxstacksize = ItemStack.MAX_STACKSIZE;
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public void setDisplayName(String name) {
        this.displayName = name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String desc) {
        this.description = desc;
    }
    
    public int getMaxStackSize() {
        return this.maxstacksize;
    }
    
    public void setMaxStackSize(int i) {
        this.maxstacksize = i;
    }
    
    public Texture getTexture() {
        return texture;
    }
    
    public void init(TextureHelper tileTextures) {
        if (this.textureName != null) {
            this.texture = tileTextures.get(this.textureName);
        }
    }
}
