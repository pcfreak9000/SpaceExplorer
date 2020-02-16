package de.pcfreak9000.space.world.ecs;

import de.omnikryptec.ecs.Family;
import de.omnikryptec.ecs.IECSManager;
import de.omnikryptec.ecs.component.ComponentMapper;
import de.omnikryptec.ecs.system.AbstractComponentSystem;
import de.omnikryptec.util.updater.Time;
import de.pcfreak9000.space.core.Keys;

public class PlayerInputSystem extends AbstractComponentSystem {
    
    public PlayerInputSystem() {
        super(Family.of(PlayerInputComponent.class));
    }
        
    private ComponentMapper<PlayerInputComponent> mapper = new ComponentMapper<>(PlayerInputComponent.class);
    
    @Override
    public void update(IECSManager iecsManager, Time time) {
        PlayerInputComponent play = mapper.get(entities.get(0));
        float vy = 0;
        float vx = 0;
        if (Keys.WALK_FORWARD.isPressed()) {
            vy += play.maxYv;
        }
        if (Keys.WALK_BACKWARD.isPressed()) {
            vy -= play.maxYv;
        }
        if (Keys.WALK_LEFT.isPressed()) {
            vx -= play.maxXv;
        }
        if (Keys.WALK_RIGHT.isPressed()) {
            vx += play.maxXv;
        }
        play.cam.localspaceWrite().translate(-vx, -vy, 0);
    }
    
}
