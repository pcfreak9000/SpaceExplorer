package de.pcfreak9000.space.world;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import de.omnikryptec.core.scene.GameController;
import de.omnikryptec.core.scene.Scene;
import de.omnikryptec.core.update.IUpdatable;
import de.omnikryptec.core.update.UContainer;
import de.omnikryptec.core.update.UpdateableFactory;
import de.omnikryptec.ecs.IECSManager;
import de.omnikryptec.render.renderer.LocalRendererContext;
import de.omnikryptec.render.renderer.RendererContext;
import de.omnikryptec.util.updater.Time;
import de.pcfreak9000.space.world.ecs.RenderSystem;

public class GroundManager {
    //add/remove entities
    //GUI? where? -> shared GUI renderer
    
    private GameController controller;
    
    private IECSManager ecsManager;
    private RendererContext rendererContext;
    private LocalRendererContext localContext;
    
    private Scene localScene;
    
    private TileWorld currentWorld;
    private WorldLoadingFence worldLoadingFence;
    
    private Set<Chunk> localLoadedChunks;
    
    public GroundManager(GameController controller) {
        this.controller = controller;
        this.localLoadedChunks = new HashSet<>();
        this.ecsManager = UpdateableFactory.createDefaultIECSManager();
        this.rendererContext = UpdateableFactory.createRendererContext();
        this.localContext = this.rendererContext.createLocal();
        this.localScene = new Scene();
        UContainer updateables = new UContainer();
        updateables.setUpdatable(0, new UpdaterClass());
        updateables.setUpdatable(1, ecsManager);
        updateables.setUpdatable(2, rendererContext);
        this.localScene.setUpdateableSync(updateables);
        addDefaultRenderer();
        addDefaultECSSystems();
    }
    
    private void addDefaultECSSystems() {
        ecsManager.addSystem(new RenderSystem(this.localContext.getIRenderedObjectManager()));
    }
    
    private void addDefaultRenderer() {
        //localContext.addRenderer....
    }
    
    public void setWorld(TileWorld w) {
        if (w == null) {
            unloadAll();
            controller.setLocalScene(null);
            //unload everything
        } else {
            if (currentWorld == null) {
                controller.setLocalScene(localScene);
            } else {
                unloadAll();
            }
            this.currentWorld = w;
            loadAll();
        }
    }
    
    public IECSManager getECSManager() {
        return ecsManager;
    }
    
    public TileWorld getCurrentWorld() {
        return currentWorld;
    }
    
    public void setWorldUpdateFence(WorldLoadingFence fence) {
        unloadAll();
        this.worldLoadingFence = fence;
        loadAll();
    }
    
    private void loadAll() {
        int xR = worldLoadingFence.getChunkRadiusRangeX();
        int yR = worldLoadingFence.getChunkRadiusRangeY();
        int xM = worldLoadingFence.getChunkMidpointX();
        int yM = worldLoadingFence.getChunkMidpointY();
        for (int i = 0; i <= 2 * xR; i++) {
            for (int j = 0; j <= 2 * yR; j++) {
                int cx = i - xR + xM;
                int cy = j - yR + yM;
                if (currentWorld.inBounds(cx, cy)) {
                    Chunk c = currentWorld.requireGet(cx, cy);
                    localLoadedChunks.add(c);
                    c.addThis(ecsManager);
                }
            }
        }
    }
    
    private void unloadAll() {
        Iterator<Chunk> it = localLoadedChunks.iterator();
        while (it.hasNext()) {
            Chunk c = it.next();
            c.removeThis(ecsManager);
            it.remove();
        }
    }
    
    private class UpdaterClass implements IUpdatable {
        @Override
        public void update(Time time) {
            //TODO improve world chunk loading update -> unload all non-needed in update and load new needed
            unloadAll();
            loadAll();
        }
    }
}
