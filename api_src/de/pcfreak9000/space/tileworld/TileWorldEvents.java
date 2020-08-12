package de.pcfreak9000.space.tileworld;

import de.omnikryptec.ecs.IECSManager;
import de.omnikryptec.event.Event;
import de.omnikryptec.render.renderer.ViewManager;

public class TileWorldEvents {

    public static class InitWorldLoaderEvent extends Event {
        public final IECSManager ecsMgr;
        public final ViewManager viewMgr;

        public InitWorldLoaderEvent(IECSManager mgr, ViewManager vmgr) {
            this.ecsMgr = mgr;
            this.viewMgr = vmgr;
        }
    }

    public static class SetTileWorldEvent extends Event {
        public final TileWorld tileWorldOld;
        public final TileWorld tileWorldNew;
        public final WorldLoader groundMgr;

        public SetTileWorldEvent(WorldLoader gmgr, TileWorld two, TileWorld nwo) {
            this.tileWorldOld = two;
            this.tileWorldNew = nwo;
            this.groundMgr = gmgr;
        }
    }

}
