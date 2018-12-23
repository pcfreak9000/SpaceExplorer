package de.pcfreak9000.spaceexplorer.mod.event;

import de.pcfreak9000.spaceexplorer.game.launch.SpaceExplorer2D;

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
