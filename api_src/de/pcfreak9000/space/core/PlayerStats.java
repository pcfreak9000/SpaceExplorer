package de.pcfreak9000.space.core;

import de.omnikryptec.core.Omnikryptec;
import de.omnikryptec.ecs.Entity;
import de.omnikryptec.render.objects.AdvancedSprite;
import de.omnikryptec.render.objects.SimpleSprite;
import de.pcfreak9000.space.voxelworld.ecs.PhysicsComponent;
import de.pcfreak9000.space.voxelworld.ecs.PlayerInputComponent;
import de.pcfreak9000.space.voxelworld.ecs.RenderComponent;
import de.pcfreak9000.space.voxelworld.ecs.TransformComponent;
import de.pcfreak9000.space.voxelworld.tile.Tile;

/**
 * Information about the player: level, ships, inventory, etc. Also the player
 * entity for surface worlds.
 *
 * @author pcfreak9000
 *
 */
public class PlayerStats {

    private final Entity playerEntity;

    public PlayerStats() {
        this.playerEntity = createRawPlayerEntity();
    }

    private Entity createRawPlayerEntity() {
        Entity e = new Entity();
        PlayerInputComponent pic = new PlayerInputComponent();
        pic.maxXv = 100;
        pic.maxYv = 100;
        e.addComponent(pic);
        AdvancedSprite sprite = new AdvancedSprite();
        sprite.setWidth(Tile.TILE_SIZE * 2);
        sprite.setHeight(Tile.TILE_SIZE * 4);
        //FIXME resource reloading
        sprite.setTexture(Omnikryptec.getTexturesS().get("mensch.png"));
        sprite.setLayer(100);
        SimpleSprite light = new SimpleSprite();
        light.setTexture(Omnikryptec.getTexturesS().get("light_2.png"));
        light.setWidth(Tile.TILE_SIZE * 8);
        light.setHeight(Tile.TILE_SIZE * 8);
        light.getTransform().localspaceWrite().setTranslation(-light.getWidth() / 2 + sprite.getWidth() / 2,
                -light.getHeight() / 2 + sprite.getHeight() / 2);
        RenderComponent rc = new RenderComponent(sprite);
        rc.light = light;
        e.addComponent(rc);
        e.addComponent(new TransformComponent());
        PhysicsComponent pc = new PhysicsComponent();
        e.addComponent(pc);
        pc.w = sprite.getWidth();
        pc.h = sprite.getHeight() * 0.95f;
        return e;
    }

    public Entity getPlayerEntity() {
        return this.playerEntity;
    }
}
