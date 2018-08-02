package de.pcfreak9000.se2d.items;

/**
 * represents an Item
 * 
 * @author pcfreak9000
 *
 */
public class Item {

	private String name;
	private String description;
	private int maxstacksize = ItemStack.MAX_STACKSIZE;
	private String texture;

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public int getMaxStackSize() {
		return maxstacksize;
	}

	public String getTexture() {
		return texture;
	}

}
