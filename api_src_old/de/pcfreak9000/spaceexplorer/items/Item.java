package de.pcfreak9000.spaceexplorer.items;

/**
 * represents an Item
 *
 * @author pcfreak9000
 *
 */
public class Item {

    private String name;
    private String description;
    private final int maxstacksize = ItemStack.MAX_STACKSIZE;
    private String texture;

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public int getMaxStackSize() {
        return this.maxstacksize;
    }

    public String getTexture() {
        return this.texture;
    }

}
