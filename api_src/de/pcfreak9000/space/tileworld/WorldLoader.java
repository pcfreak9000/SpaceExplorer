package de.pcfreak9000.space.tileworld;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import de.omnikryptec.core.Omnikryptec;
import de.omnikryptec.core.Scene;
import de.omnikryptec.core.update.IUpdatable;
import de.omnikryptec.core.update.UContainer;
import de.omnikryptec.core.update.UpdateableFactory;
import de.omnikryptec.ecs.IECSManager;
import de.omnikryptec.render.renderer.AdvancedRenderer2D;
import de.omnikryptec.render.renderer.Renderer2D;
import de.omnikryptec.render.renderer.ViewManager;
import de.omnikryptec.util.profiling.Profiler;
import de.omnikryptec.util.updater.Time;
import de.pcfreak9000.space.core.Space;
import de.pcfreak9000.space.tileworld.ecs.CameraSystem;
import de.pcfreak9000.space.tileworld.ecs.ParallaxSystem;
import de.pcfreak9000.space.tileworld.ecs.PhysicsSystem;
import de.pcfreak9000.space.tileworld.ecs.PlayerInputSystem;
import de.pcfreak9000.space.tileworld.ecs.RenderSystem;
import de.pcfreak9000.space.tileworld.ecs.TickRegionSystem;

/**
 * Responsible for successful surface world loading and unloading, management of
 * loaded chunks
 *
 * @author pcfreak9000
 *
 */
public class WorldLoader {
    
    private WorldManager manager;
    
    private World currentWorld;
    private WorldLoadingFence worldLoadingFence;
    
    //TODO recache loaded regions if resources have been reloaded
    
    private final Set<Region> localLoadedChunks;
    
    public WorldLoader(WorldManager worldMgr) {
        this.localLoadedChunks = new HashSet<>();
        this.manager = worldMgr;
    }
    
    public void setWorld(World w) {
        if (hasCurrentWorld()) {
            unloadAllRegions();
            if (this.currentWorld.getBackground() != null) {
                manager.getECSManager().removeEntity(this.currentWorld.getBackground().getEntity());
            }
        }
        this.currentWorld = w;
        if (hasCurrentWorld()) {
            loadAllRegions();
            if (this.currentWorld.getBackground() != null) {
                manager.getECSManager().addEntity(this.currentWorld.getBackground().getEntity());
            }
        }
    }
    
    private boolean hasCurrentWorld() {
        return this.currentWorld != null;
    }
    
    public void setWorldUpdateFence(WorldLoadingFence fence) {
        if (!hasCurrentWorld()) {
            this.worldLoadingFence = fence;
            return;
        }
        unloadAllRegions();
        this.worldLoadingFence = fence;
        loadAllRegions();
    }
    
    private void loadAllRegions() {
        int xR = this.worldLoadingFence.getChunkRadiusRangeX();
        int yR = this.worldLoadingFence.getChunkRadiusRangeY();
        int xM = this.worldLoadingFence.getChunkMidpointX();
        int yM = this.worldLoadingFence.getChunkMidpointY();
        for (int i = 0; i <= 2 * xR; i++) {
            for (int j = 0; j <= 2 * yR; j++) {
                int rx = i - xR + xM;
                int ry = j - yR + yM;
                if (this.currentWorld.getTileWorld().inRegionBounds(rx, ry)) {
                    Region c = this.currentWorld.getTileWorld().requestRegion(rx, ry);
                    if (c != null) {
                        this.localLoadedChunks.add(c);
                        manager.getECSManager().addEntity(c.getECSEntity());
                    }
                }
            }
        }
    }
    
    private void unloadAllRegions() {
        Iterator<Region> it = this.localLoadedChunks.iterator();
        while (it.hasNext()) {
            Region c = it.next();
            manager.getECSManager().addEntity(c.getECSEntity());
            it.remove();
        }
    }
    
    //make sure that the chunks are updated for dynamics after the movement but before this
    public void loadChunks(Time time) {
        //TODO improve world chunk loading update -> unload all non-needed in update and load new needed
        Profiler.begin("Reload World");
        unloadAllRegions();
        loadAllRegions();
        Profiler.end();
        //System.out.println(time.ops);
    }
}
