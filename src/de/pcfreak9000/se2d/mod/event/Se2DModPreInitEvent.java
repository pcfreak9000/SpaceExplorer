package de.pcfreak9000.se2d.mod.event;

import de.omnikryptec.event.eventV2.Event;
import de.pcfreak9000.se2d.game.launch.SpaceExplorer2D;

/**
 * the first mod init event.
 * 
 * @author pcfreak9000
 *
 */
public class Se2DModPreInitEvent extends Event {

	public Se2DModPreInitEvent() {
		super(SpaceExplorer2D.getSpaceExplorer2D().getEventBus());
		this.consumeable = false;
	}
}
