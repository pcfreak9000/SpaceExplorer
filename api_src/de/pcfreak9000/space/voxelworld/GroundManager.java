package de.pcfreak9000.space.voxelworld;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.joml.Matrix4f;

import de.omnikryptec.core.Omnikryptec;
import de.omnikryptec.core.Scene;
import de.omnikryptec.core.update.IUpdatable;
import de.omnikryptec.core.update.UContainer;
import de.omnikryptec.core.update.UpdateableFactory;
import de.omnikryptec.ecs.IECSManager;
import de.omnikryptec.render.AdaptiveCamera;
import de.omnikryptec.render.Camera;
import de.omnikryptec.render.renderer.AdvancedRenderer2D;
import de.omnikryptec.render.renderer.ViewManager;
import de.omnikryptec.util.math.MathUtil;
import de.omnikryptec.util.profiling.Profiler;
import de.omnikryptec.util.updater.Time;
import de.pcfreak9000.space.core.GameRegistry;
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
    
    private IECSManager ecsManager;
    
    private ViewManager viewManager;
    
    private Scene localScene;
    
    private TileWorld currentWorld;
    private WorldLoadingFence worldLoadingFence;
    
    private Camera planetCamera;
    
    private Set<Region> localLoadedChunks;
    
    public GroundManager() {
        this.localLoadedChunks = new HashSet<>();
        this.ecsManager = UpdateableFactory.createDefaultIECSManager();
        this.localScene = Omnikryptec.getGameS().createAndAddScene();
        this.viewManager = this.localScene.getViewManager();
        this.planetCamera = new AdaptiveCamera(this::createProjection);
        this.viewManager.getMainView().setProjection(planetCamera);
        UContainer updateables = new UContainer();
        updateables.setUpdatable(0, new UpdaterClass());
        updateables.setUpdatable(1, ecsManager);
        this.localScene.setGameLogic(updateables);
        addDefaultECSSystems();
        Space.BUS.post(new VoxelworldEvents.InitGroundManagerEvent(this.ecsManager, this.viewManager));
    }
    
    private Matrix4f createProjection(int width, int height) {
        int[] vp = MathUtil.calculateViewport(width / (double) height, 1920, 1920);
        return new Matrix4f().ortho2D(-vp[2] / 2f, vp[2] / 2f, -vp[3] / 2f, vp[3] / 2f);
    }
    
    public Camera getPlanetCamera() {
        return planetCamera;
    }
    
    private void addDefaultECSSystems() {
        AdvancedRenderer2D renderer = new AdvancedRenderer2D(12 * 6 * Region.REGION_TILE_SIZE);
        renderer.setEnableReflections(false);
        this.viewManager.addRenderer(renderer);
        ecsManager.addSystem(new RenderSystem(renderer));
        ecsManager.addSystem(new PlayerInputSystem());
        ecsManager.addSystem(new PhysicsSystem());
        ecsManager.addSystem(new CameraSystem());
        ecsManager.addSystem(new ParallaxSystem());
    }
    
    
    public void setWorld(TileWorld w) {
        Space.BUS.post(new VoxelworldEvents.SetVoxelWorldEvent(this, this.currentWorld, w));
        if (w == null) {
            unloadAll();
            Omnikryptec.getGameS().removeScene(localScene);
            //unload everything
        } else {
            if (currentWorld == null) {
                Omnikryptec.getGameS().addScene(localScene);
            } else {
                unloadAll();
            }
            this.ecsManager.addEntity(GameRegistry.BACKGROUND_REGISTRY.get("stars").getEntity());
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
        if (currentWorld == null) {
            this.worldLoadingFence = fence;
            return;
        }
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
                int rx = i - xR + xM;
                int ry = j - yR + yM;
                if (currentWorld.inBounds(rx, ry)) {
                    Region c = currentWorld.requestRegion(rx, ry);
                    if (c != null) {
                        localLoadedChunks.add(c);
                        c.addThis(ecsManager);
                    }
                }
            }
        }
    }
    
    private void unloadAll() {
        Iterator<Region> it = localLoadedChunks.iterator();
        while (it.hasNext()) {
            Region c = it.next();
            c.removeThis(ecsManager);
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
