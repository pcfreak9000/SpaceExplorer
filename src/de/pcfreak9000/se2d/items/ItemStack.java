package de.pcfreak9000.se2d.items;

import de.pcfreak9000.se2d.game.core.GameRegistry;

/**
 * a Stack of {@link Item}s
 * 
 * @author pcfreak9000
 *
 */
public class ItemStack {

	public static final int MAX_STACKSIZE = 128;

	private Item item;
	private int count;

	public ItemStack(Item item, int count) {
		GameRegistry.getItemRegistry().checkRegistered(item);
		this.item = item;
		this.count = count;
	}

	public Item getItem() {
		return item;
	}

	public int getCount() {
		return count;
	}

}
