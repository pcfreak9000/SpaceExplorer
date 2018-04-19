package de.pcfreak9000.se2d.mod.event;

import de.pcfreak9000.se2d.game.SpaceExplorer2D;
import omnikryptec.event.eventV2.Event;

public class Se2DModPostInitEvent extends Event{

	public Se2DModPostInitEvent() {
		super(SpaceExplorer2D.getSpaceExplorer2D().getEventBus());
	}

}
