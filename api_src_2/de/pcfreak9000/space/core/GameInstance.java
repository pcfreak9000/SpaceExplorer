package de.pcfreak9000.space.core;

import de.pcfreak9000.space.voxelworld.GroundManager;
import de.pcfreak9000.space.voxelworld.TileWorld;

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
    
    public void visit(TileWorld world, float x, float y) {
        //TODO set player coords
        groundManager.getECSManager().addEntity(playerStats.getPlayerEntity());
        groundManager.setWorld(world);
    }
    
}
