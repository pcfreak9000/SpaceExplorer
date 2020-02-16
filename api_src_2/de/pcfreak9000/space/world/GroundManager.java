package de.pcfreak9000.space.world;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.joml.Matrix4f;

import de.omnikryptec.core.Omnikryptec;
import de.omnikryptec.core.Scene;
import de.omnikryptec.core.update.IUpdatable;
import de.omnikryptec.core.update.UContainer;
import de.omnikryptec.core.update.UpdateableFactory;
import de.omnikryptec.ecs.Entity;
import de.omnikryptec.ecs.IECSManager;
import de.omnikryptec.render.AdaptiveCamera;
import de.omnikryptec.render.Camera;
import de.omnikryptec.render.renderer.ViewManager;
import de.omnikryptec.util.math.MathUtil;
import de.omnikryptec.util.profiling.Profiler;
import de.omnikryptec.util.updater.Time;
import de.pcfreak9000.space.world.ecs.PlayerInputSystem;
import de.pcfreak9000.space.world.ecs.RenderSystem;

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
    
    private Set<Chunk> localLoadedChunks;
    
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
        //        //Test code
        //        Entity test = new Entity();
        //        ReflectiveSprite s = new ReflectiveSprite();
        //        s.setTexture(GameRegistry.TILE_REGISTRY.get("Kek vom Dienst").getTexture());
        //        s.setWidth(1000);
        //        s.setHeight(1000);
        //        s.setReflectionType(Reflection2DType.Disable);
        //        test.addComponent(new RenderComponent(s));
        //        ecsManager.addEntity(test);
        //        controller.setLocalScene(localScene);
    }
    
    private Matrix4f createProjection(int width, int height) {
        int[] vp = MathUtil.calculateViewport(width / (double) height, 1920, 1920);
        return new Matrix4f().ortho2D(-vp[2] / 2, vp[2] / 2, -vp[3] / 2, vp[3] / 2);
    }
    
    public Camera getPlanetCamera() {
        return planetCamera;
    }
    
    private void addDefaultECSSystems() {
        WorldRenderer renderer = new WorldRenderer();
        this.viewManager.addRenderer(renderer);
        ecsManager.addSystem(new RenderSystem(renderer));
        ecsManager.addSystem(new PlayerInputSystem());
    }
    
    public void setWorld(TileWorld w) {
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
            this.currentWorld = w;
            loadAll();
        }
    }
    
    public IECSManager getECSManager() {
        return ecsManager;
    }
    
    public void addStaticEntity(Entity e) {
        currentWorld.addStaticEntity(e);
        ecsManager.addEntity(e);
    }
    
    public void addDynamicEntity(Entity e) {
        currentWorld.addDynamicEntity(e);
        ecsManager.addEntity(e);
    }
    
    public void removeStaticEntity(Entity e) {
        currentWorld.removeStaticEntity(e);
        ecsManager.removeEntity(e);
    }
    
    public void removeDynamicEntity(Entity e) {
        currentWorld.removeDynamicEntity(e);
        ecsManager.removeEntity(e);
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
                int cx = i - xR + xM;
                int cy = j - yR + yM;
                if (currentWorld.inBounds(cx, cy)) {
                    Chunk c = currentWorld.requireGetc(cx, cy);
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
