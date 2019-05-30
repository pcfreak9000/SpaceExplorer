package de.pcfreak9000.space.world.ecs;

import org.lwjgl.glfw.GLFW;

import de.omnikryptec.ecs.Family;
import de.omnikryptec.ecs.IECSManager;
import de.omnikryptec.ecs.component.ComponentMapper;
import de.omnikryptec.ecs.system.ComponentSystem;
import de.omnikryptec.libapi.exposed.input.InputManager;
import de.omnikryptec.util.updater.Time;
import de.pcfreak9000.space.core.Space;

public class PlayerInputSystem extends ComponentSystem {
    
    public PlayerInputSystem() {
        super(Family.of(PlayerInputComponent.class));
    }
    
    private InputManager input = Space.getSpace().getGameInput();
    
    private ComponentMapper<PlayerInputComponent> mapper = new ComponentMapper<>(PlayerInputComponent.class);
    
    @Override
    public void update(IECSManager iecsManager, Time time) {
        PlayerInputComponent play = mapper.get(entities.get(0));
        float vy = 0;
        float vx = 0;
        if (input.isKeyboardKeyPressed(GLFW.GLFW_KEY_W)) {
            vy += play.maxYv;
        }
        if (input.isKeyboardKeyPressed(GLFW.GLFW_KEY_S)) {
            vy -= play.maxYv;
        }
        if (input.isKeyboardKeyPressed(GLFW.GLFW_KEY_A)) {
            vx -= play.maxXv;
        }
        if (input.isKeyboardKeyPressed(GLFW.GLFW_KEY_D)) {
            vx += play.maxXv;
        }
        play.cam.localspaceWrite().translate(-vx, -vy, 0);
    }
    
}
