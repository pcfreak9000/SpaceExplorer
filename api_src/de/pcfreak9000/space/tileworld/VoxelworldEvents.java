package de.pcfreak9000.space.tileworld;

import de.omnikryptec.ecs.IECSManager;
import de.omnikryptec.event.Event;
import de.omnikryptec.render.renderer.ViewManager;

public class VoxelworldEvents {

    public static class InitGroundManagerEvent extends Event {
        public final IECSManager ecsMgr;
        public final ViewManager viewMgr;

        public InitGroundManagerEvent(IECSManager mgr, ViewManager vmgr) {
            this.ecsMgr = mgr;
            this.viewMgr = vmgr;
        }
    }

    public static class SetVoxelWorldEvent extends Event {
        public final TileWorld tileWorldOld;
        public final TileWorld tileWorldNew;
        public final GroundManager groundMgr;

        public SetVoxelWorldEvent(GroundManager gmgr, TileWorld two, TileWorld nwo) {
            this.tileWorldOld = two;
            this.tileWorldNew = nwo;
            this.groundMgr = gmgr;
        }
    }

}
