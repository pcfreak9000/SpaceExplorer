package de.pcfreak9000.space.core;

import de.omnikryptec.ecs.component.ComponentType;
import de.pcfreak9000.space.voxelworld.GroundManager;
import de.pcfreak9000.space.voxelworld.TileWorld;
import de.pcfreak9000.space.voxelworld.WorldInformationBundle;
import de.pcfreak9000.space.voxelworld.ecs.TransformComponent;

/**
 * The currently loaded level. Information about the player, the world,
 * and world generation.
 * 
 * @author pcfreak9000
 *
 */
public class GameInstance {
    
    private PlayerStats playerStats;
    
    private GroundManager groundManager;
    
    public GameInstance(GroundManager gmgr) {
        this.groundManager = gmgr;
        this.playerStats = new PlayerStats(); //TODO playerstats creation
    }
    
    public void visit(WorldInformationBundle world, float x, float y) {
        //TODO set player coords
        TransformComponent tc = playerStats.getPlayerEntity().getComponent(ComponentType.of(TransformComponent.class));
        tc.transform.localspaceWrite().setTranslation(x, y);
        groundManager.getECSManager().addEntity(playerStats.getPlayerEntity());
        groundManager.setWorld(world);
    }
    
}
