package de.pcfreak9000.se2d.mod.event;

import de.omnikryptec.old.event.eventV2.Event;
import de.pcfreak9000.se2d.game.launch.SpaceExplorer2D;

/**
 * the third mod init event.
 * 
 * @author pcfreak9000
 *
 */
public class Se2DModPostInitEvent extends Event {

	public Se2DModPostInitEvent() {
		super(SpaceExplorer2D.getSpaceExplorer2D().getEventBus());
		this.consumeable = false;
	}

}
