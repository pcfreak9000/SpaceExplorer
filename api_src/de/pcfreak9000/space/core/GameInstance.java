package de.pcfreak9000.space.core;

import de.omnikryptec.ecs.component.ComponentType;
import de.pcfreak9000.space.tileworld.World;
import de.pcfreak9000.space.tileworld.WorldLoader;
import de.pcfreak9000.space.tileworld.WorldLoadingFence;
import de.pcfreak9000.space.tileworld.WorldManager;
import de.pcfreak9000.space.tileworld.ecs.TransformComponent;

/**
 * The currently loaded level. Information about the player, the world, and
 * world generation.
 *
 * @author pcfreak9000
 *
 */
public class GameInstance {
    
    private final PlayerStats playerStats;
    
    private final WorldManager groundManager;
    
    public GameInstance(WorldManager gmgr) {
        this.groundManager = gmgr;
        this.playerStats = new PlayerStats(); //TODO playerstats creation
    }
    
    public void visit(World world, float x, float y) {
        //TODO set player coords
        TransformComponent tc = this.playerStats.getPlayerEntity()
                .getComponent(ComponentType.of(TransformComponent.class));
        tc.transform.localspaceWrite().setTranslation(x, y);
        this.groundManager.getLoader().setWorldUpdateFence(new WorldLoadingFence(tc.transform));
        this.groundManager.getECSManager().addEntity(this.playerStats.getPlayerEntity());
        this.groundManager.setWorld(world);
    }
    
}
