package de.pcfreak9000.space.tileworld;

import de.omnikryptec.ecs.IECSManager;
import de.omnikryptec.event.Event;
import de.omnikryptec.render3.structure.ViewManager;

public class WorldEvents {
    
    public static class InitWorldManagerEvent extends Event {
        public final IECSManager ecsMgr;
        public final ViewManager viewMgr;
        
        public InitWorldManagerEvent(IECSManager mgr, ViewManager vmgr) {
            this.ecsMgr = mgr;
            this.viewMgr = vmgr;
        }
    }
    
    public static class SetWorldEvent extends Event {
        public final World worldOld;
        public final World worldNew;
        public final WorldManager worldMgr;
        
        public SetWorldEvent(WorldManager wmgr, World worldOld, World worldNew) {
            this.worldNew = worldNew;
            this.worldOld = worldOld;
            this.worldMgr = wmgr;
        }
        
        public TileWorld getTileWorldNew() {
            return worldNew == null ? null : worldNew.getTileWorld();
        }
    }
    
}
