package de.pcfreak9000.se2d.mod.event;

import de.pcfreak9000.se2d.game.launch.SpaceExplorer2D;
import omnikryptec.event.eventV2.Event;

/**
 * the second mod init event
 * 
 * @author pcfreak9000
 *
 */
public class Se2DModInitEvent extends Event {

	public Se2DModInitEvent() {
		super(SpaceExplorer2D.getSpaceExplorer2D().getEventBus());
		this.consumeable = false;
	}

}
