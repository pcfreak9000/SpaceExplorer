package de.pcfreak9000.space.tileworld;

import de.omnikryptec.core.Omnikryptec;
import de.omnikryptec.core.Scene;
import de.omnikryptec.core.update.UContainer;
import de.omnikryptec.core.update.UpdateableFactory;
import de.omnikryptec.ecs.IECSManager;
import de.omnikryptec.render.postprocessing.BrightnessAccent;
import de.omnikryptec.render.postprocessing.EffectMixer;
import de.omnikryptec.render.postprocessing.GaussianBlur;
import de.omnikryptec.render.postprocessing.PostprocessingBundle;
import de.omnikryptec.render.renderer.AdvancedRenderer2D;
import de.omnikryptec.render.renderer.Renderer2D;
import de.omnikryptec.render.renderer.ViewManager;
import de.pcfreak9000.space.core.Space;
import de.pcfreak9000.space.tileworld.ecs.CameraSystem;
import de.pcfreak9000.space.tileworld.ecs.FogSystem;
import de.pcfreak9000.space.tileworld.ecs.ParallaxSystem;
import de.pcfreak9000.space.tileworld.ecs.PhysicsSystem;
import de.pcfreak9000.space.tileworld.ecs.PlayerInputSystem;
import de.pcfreak9000.space.tileworld.ecs.RenderSystem;
import de.pcfreak9000.space.tileworld.ecs.TickRegionSystem;

public class WorldManager {
    
    private final IECSManager ecsManager;
    
    private final ViewManager viewManager;
    
    private final Scene localScene;
    
    private final PlanetCamera planetCamera;
    
    private final WorldLoader worldLoader;
    
    private World currentWorld;
    
    public WorldManager() {
        this.ecsManager = UpdateableFactory.createDefaultIECSManager();
        this.localScene = new Scene();
        this.viewManager = this.localScene.getViewManager();
        this.planetCamera = new PlanetCamera();
        this.viewManager.getMainView().setProjection(this.planetCamera.getCameraActual());
        this.worldLoader = new WorldLoader(this);
        UContainer updateables = new UContainer();
        updateables.setUpdatable(0, (t) -> worldLoader.loadChunks(t));
        updateables.setUpdatable(1, this.ecsManager);
        this.localScene.setGameLogic(updateables);
        addDefaultECSSystems();
        Space.BUS.post(new WorldEvents.InitWorldManagerEvent(this.ecsManager, this.viewManager));
    }
    
    private void addDefaultECSSystems() {
        AdvancedRenderer2D renderer = new AdvancedRenderer2D(12 * 6 * Region.REGION_TILE_SIZE);
        renderer.setEnableReflections(false);
        renderer.setUseExtendedLightRange(false);
        renderer.ambientLight().setAllRGB(0);
        Renderer2D backgroundRenderer = new Renderer2D(18);
        backgroundRenderer.setEnableTiling(true);
        backgroundRenderer.ambientLight().set(1, 1, 1);
        this.viewManager.addRenderer(backgroundRenderer);
        this.viewManager.addRenderer(renderer);
        PostprocessingBundle bund = new PostprocessingBundle();
        bund.add(new BrightnessAccent());
        bund.add(GaussianBlur.createGaussianBlurBundle(1));
        bund.add(GaussianBlur.createGaussianBlurBundle(0.85f));
        bund.add(GaussianBlur.createGaussianBlurBundle(0.4f));
        EffectMixer eff = new EffectMixer(bund);
        eff.setWeightSource(0.6f);
        eff.setWeightEffect(0.5f);
        this.viewManager.getMainView().setPostprocessor(eff);
        this.ecsManager.addSystem(new RenderSystem(renderer, backgroundRenderer));
        this.ecsManager.addSystem(new PlayerInputSystem());
        this.ecsManager.addSystem(new TickRegionSystem());
        this.ecsManager.addSystem(new PhysicsSystem());
        this.ecsManager.addSystem(new CameraSystem());
        this.ecsManager.addSystem(new ParallaxSystem());
        this.ecsManager.addSystem(new FogSystem());
    }
    
    public void setWorld(World world) {
        if (currentWorld == null) {
            addWorldScene();
        }
        Space.BUS.post(new WorldEvents.SetWorldEvent(this, currentWorld, world));
        this.currentWorld = world;
        this.worldLoader.setWorld(this.currentWorld);
        if (this.currentWorld == null) {
            removeWorldScene();
        }
    }
    
    private void addWorldScene() {
        Omnikryptec.getGameS().addScene(localScene);
    }
    
    private void removeWorldScene() {
        Omnikryptec.getGameS().removeScene(localScene);
    }
    
    public IECSManager getECSManager() {
        return this.ecsManager;
    }
    
    public PlanetCamera getPlanetCamera() {
        return this.planetCamera;
    }
    
    public WorldLoader getLoader() {
        return worldLoader;
    }
}
