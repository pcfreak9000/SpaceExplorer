package de.pcfreak9000.space.world;

import de.omnikryptec.core.scene.GameController;
import de.omnikryptec.core.scene.Scene;
import de.omnikryptec.core.update.UContainer;
import de.omnikryptec.core.update.UpdateableFactory;
import de.omnikryptec.ecs.IECSManager;
import de.omnikryptec.render.renderer.RendererContext;

public class GroundManager {
    
    private GameController controller;
    
    private WorldLoader worldUpdater;
    private IECSManager ecsManager;
    private RendererContext rendererContext;
    
    private Scene localScene;
    
    private TileWorld currentWorld;
    
    public GroundManager(GameController controller) {
        this.controller = controller;
        this.worldUpdater = new WorldLoader();
        this.ecsManager = UpdateableFactory.createDefaultIECSManager();
        this.rendererContext = new RendererContext();
        this.localScene = new Scene();
        UContainer updateables = new UContainer();
        updateables.setUpdatable(0, worldUpdater);
        updateables.setUpdatable(1, ecsManager);
        updateables.setUpdatable(2, rendererContext);
        this.localScene.setUpdateableSync(updateables);
        addDefaultRenderer();
        addDefaultECSSystems();
    }
    
    private void addDefaultECSSystems() {
    }
    
    private void addDefaultRenderer() {
    }
    
    public void setWorld(TileWorld w) {
        if (w == null) {
            controller.setLocalScene(null);
            //unload everything
        } else {
            if (currentWorld == null) {
                controller.setLocalScene(localScene);
            }
            this.currentWorld = w;
            this.worldUpdater.setWorld(w);
        }
    }
    
    public IECSManager getECSManager() {
        return ecsManager;
    }
    
    public TileWorld getCurrentWorld() {
        return currentWorld;
    }
    
    //set/get World
    //add/remove entities
    //GUI? where? -> shared GUI renderer
}
