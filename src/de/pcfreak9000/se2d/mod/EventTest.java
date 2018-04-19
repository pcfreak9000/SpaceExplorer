package de.pcfreak9000.se2d.mod;

import de.pcfreak9000.se2d.game.SpaceExplorer2D;
import omnikryptec.event.eventV2.Event;
import omnikryptec.event.eventV2.EventBus;

public class EventTest extends Event{

	public EventTest() {
		super(SpaceExplorer2D.getSpaceExplorer2D().getEventBus());
		//setAsyncSubmission(true);
	}
	
}
