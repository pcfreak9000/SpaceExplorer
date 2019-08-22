package de.pcfreak9000.space.core;

import de.omnikryptec.ecs.Entity;
import de.pcfreak9000.space.world.ecs.PlayerInputComponent;

//Level, Stuff, Ships, Inventory, etc
public class PlayerStats {
    
    private Entity playerEntity;
    
    public PlayerStats() {
        this.playerEntity = createRawPlayerEntity();
    }
    
    private Entity createRawPlayerEntity() {
        Entity e = new Entity();
        PlayerInputComponent pic = new PlayerInputComponent();
        pic.cam = Space.getSpace().getGroundManager().getPlanetCamera().getTransform();//TODO move/change
        pic.maxXv = 10;
        pic.maxYv = 10;
        e.addComponent(pic);
        return e;
    }
    
    public Entity getPlayerEntity() {
        return playerEntity;
    }
}
