package de.pcfreak9000.se2d.mod;

import de.pcfreak9000.se2d.game.launch.SpaceExplorer2D;
import omnikryptec.event.eventV2.Event;

public class EventTest extends Event {

	public EventTest() {
		super(SpaceExplorer2D.getSpaceExplorer2D().getEventBus());
		// setAsyncSubmission(true);
	}

}
