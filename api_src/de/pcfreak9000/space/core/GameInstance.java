package de.pcfreak9000.space.core;

import de.omnikryptec.ecs.component.ComponentType;
import de.pcfreak9000.space.tileworld.GroundManager;
import de.pcfreak9000.space.tileworld.WorldInformationBundle;
import de.pcfreak9000.space.tileworld.WorldLoadingFence;
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

    private final GroundManager groundManager;

    public GameInstance(GroundManager gmgr) {
        this.groundManager = gmgr;
        this.playerStats = new PlayerStats(); //TODO playerstats creation
    }

    public void visit(WorldInformationBundle world, float x, float y) {
        //TODO set player coords
        TransformComponent tc = this.playerStats.getPlayerEntity()
                .getComponent(ComponentType.of(TransformComponent.class));
        tc.transform.localspaceWrite().setTranslation(x, y);
        this.groundManager.setWorldUpdateFence(new WorldLoadingFence(tc.transform));
        this.groundManager.getECSManager().addEntity(this.playerStats.getPlayerEntity());
        this.groundManager.setWorld(world);
    }

}
