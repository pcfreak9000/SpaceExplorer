package de.pcfreak9000.space.core;

import de.omnikryptec.core.Omnikryptec;
import de.omnikryptec.ecs.Entity;
import de.omnikryptec.render.objects.AdvancedSprite;
import de.omnikryptec.resource.TextureConfig;
import de.omnikryptec.resource.TextureConfig.MagMinFilter;
import de.pcfreak9000.space.voxelworld.ecs.PhysicsComponent;
import de.pcfreak9000.space.voxelworld.ecs.PlayerInputComponent;
import de.pcfreak9000.space.voxelworld.ecs.RenderComponent;
import de.pcfreak9000.space.voxelworld.ecs.TransformComponent;

/**
 * Information about the player: level, ships, inventory, etc. Also the player
 * entity for surface worlds.
 * 
 * @author pcfreak9000
 *
 */
public class PlayerStats {
    
    private Entity playerEntity;
    
    public PlayerStats() {
        this.playerEntity = createRawPlayerEntity();
    }
    
    private Entity createRawPlayerEntity() {
        Entity e = new Entity();
        PlayerInputComponent pic = new PlayerInputComponent();
        pic.cam = Space.getSpace().getGroundManager().getPlanetCamera().getTransform();//TODO move/change
        pic.maxXv = 100;
        pic.maxYv = 100;
        e.addComponent(pic);
        AdvancedSprite sprite = new AdvancedSprite();
        sprite.setWidth(100);
        sprite.setHeight(200);
        //FIXME resource reloading
        sprite.setTexture(Omnikryptec.getTexturesS().get("mensch.png"));
        sprite.setLayer(100);
        e.addComponent(new RenderComponent(sprite));
        e.addComponent(new TransformComponent());
        PhysicsComponent pc = new PhysicsComponent();
        e.addComponent(pc);
        return e;
    }
    
    public Entity getPlayerEntity() {
        return playerEntity;
    }
}
