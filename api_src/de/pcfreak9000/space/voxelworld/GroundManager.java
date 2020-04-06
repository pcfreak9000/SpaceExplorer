package de.pcfreak9000.space.voxelworld;

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
import de.pcfreak9000.space.voxelworld.ecs.CameraSystem;
import de.pcfreak9000.space.voxelworld.ecs.ParallaxSystem;
import de.pcfreak9000.space.voxelworld.ecs.PhysicsSystem;
import de.pcfreak9000.space.voxelworld.ecs.PlayerInputSystem;
import de.pcfreak9000.space.voxelworld.ecs.RenderSystem;

/**
 * Responsible for successful surface world loading and unloading, management of
 * loaded chunks, updating and rendering the current world.
 *
 * @author pcfreak9000
 *
 */
public class GroundManager {

    //add/remove entities
    //GUI? where? -> shared GUI renderer

    private final IECSManager ecsManager;

    private final ViewManager viewManager;

    private final Scene localScene;

    private WorldInformationBundle currentWorld;
    private WorldLoadingFence worldLoadingFence;

    private final PlanetCamera planetCamera;

    private final Set<Region> localLoadedChunks;

    public GroundManager() {
        this.localLoadedChunks = new HashSet<>();
        this.ecsManager = UpdateableFactory.createDefaultIECSManager();
        this.localScene = new Scene();
        this.viewManager = this.localScene.getViewManager();
        this.planetCamera = new PlanetCamera();
        this.viewManager.getMainView().setProjection(this.planetCamera.getCameraActual());
        UContainer updateables = new UContainer();
        updateables.setUpdatable(0, new UpdaterClass());
        updateables.setUpdatable(1, this.ecsManager);
        this.localScene.setGameLogic(updateables);
        addDefaultECSSystems();
        Space.BUS.post(new VoxelworldEvents.InitGroundManagerEvent(this.ecsManager, this.viewManager));
    }

    public PlanetCamera getPlanetCamera() {
        return this.planetCamera;
    }

    private void addDefaultECSSystems() {
        AdvancedRenderer2D renderer = new AdvancedRenderer2D(12 * 6 * Region.REGION_TILE_SIZE);
        renderer.setEnableReflections(false);
        renderer.setUseExtendedLightRange(true);
        renderer.ambientLight().setAllRGB(0);
        Renderer2D backgroundRenderer = new Renderer2D(18);
        backgroundRenderer.setEnableTiling(true);
        backgroundRenderer.ambientLight().set(1, 1, 1);
        this.viewManager.addRenderer(backgroundRenderer);
        this.viewManager.addRenderer(renderer);
        this.ecsManager.addSystem(new RenderSystem(renderer, backgroundRenderer));
        this.ecsManager.addSystem(new PlayerInputSystem());
        this.ecsManager.addSystem(new PhysicsSystem());
        this.ecsManager.addSystem(new CameraSystem());
        this.ecsManager.addSystem(new ParallaxSystem());
    }

    public void setWorld(WorldInformationBundle w) {
        Space.BUS.post(new VoxelworldEvents.SetVoxelWorldEvent(this,
                this.getCurrentWorld() == null ? null : this.getCurrentWorld().getTileWorld(),
                w == null ? null : w.getTileWorld()));//TODO meh... use the WorldInformationBundle instead?
        if (w == null) {
            unloadAll();
            if (this.currentWorld != null && this.currentWorld.getBackground() != null) {
                getECSManager().removeEntity(this.currentWorld.getBackground().getEntity());
            }
            Omnikryptec.getGameS().removeScene(this.localScene);
            //unload everything
        } else {
            if (this.currentWorld == null) {
                Omnikryptec.getGameS().addScene(this.localScene);
            } else {
                if (this.currentWorld.getBackground() != null) {
                    getECSManager().removeEntity(this.currentWorld.getBackground().getEntity());
                }
                unloadAll();
            }
            this.currentWorld = w;
            if (this.currentWorld.getBackground() != null) {
                getECSManager().addEntity(this.currentWorld.getBackground().getEntity());
            }
            loadAll();
        }
    }

    public IECSManager getECSManager() {
        return this.ecsManager;
    }

    public WorldInformationBundle getCurrentWorld() {
        return this.currentWorld;
    }

    public void setWorldUpdateFence(WorldLoadingFence fence) {
        if (this.currentWorld == null) {
            this.worldLoadingFence = fence;
            return;
        }
        unloadAll();
        this.worldLoadingFence = fence;
        loadAll();
    }

    private void loadAll() {
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
                        c.addThisTo(this.ecsManager);
                    }
                }
            }
        }
    }

    private void unloadAll() {
        Iterator<Region> it = this.localLoadedChunks.iterator();
        while (it.hasNext()) {
            Region c = it.next();
            c.removeThisFrom(this.ecsManager);
            it.remove();
        }
    }

    private class UpdaterClass implements IUpdatable {
        @Override //make sure that the chunks are updated for dynamics after the movement but before this
        public void update(Time time) {
            //TODO improve world chunk loading update -> unload all non-needed in update and load new needed
            Profiler.begin("Reload World");
            unloadAll();
            loadAll();
            Profiler.end();
            //System.out.println(time.ops);
        }
    }
}
