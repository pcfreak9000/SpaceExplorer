package de.pcfreak9000.space.core;

import de.omnikryptec.core.Omnikryptec;
import de.omnikryptec.ecs.Entity;
import de.omnikryptec.render3.d2.sprites.Sprite;
import de.pcfreak9000.space.item.Inventory;
import de.pcfreak9000.space.tileworld.ecs.PhysicsComponent;
import de.pcfreak9000.space.tileworld.ecs.PlayerInputComponent;
import de.pcfreak9000.space.tileworld.ecs.RenderComponent;
import de.pcfreak9000.space.tileworld.ecs.TransformComponent;
import de.pcfreak9000.space.tileworld.tile.Tile;

/**
 * Information about the player: level, ships, inventory, etc. Also the player
 * entity for surface worlds.
 *
 * @author pcfreak9000
 *
 */
public class Player {
    
    private final Entity playerEntity;
    
    private Inventory inventory;
    
    public Player() {
        this.playerEntity = createRawPlayerEntity();
        this.inventory = new Inventory();
    }
    
    private Entity createRawPlayerEntity() {
        Entity e = new Entity();
        PlayerInputComponent pic = new PlayerInputComponent();
        pic.maxXv = 100;
        pic.maxYv = 100;
        e.addComponent(pic);
        PhysicsComponent pc = new PhysicsComponent();
        Sprite sprite = new Sprite() {
            @Override
            public void draw() {
                super.draw();
                //batch.color().set(0, 0, 1);
                //batch.drawRect(pc.x, pc.y, pc.w, pc.h);
            }
        };
        sprite.setWidth(Tile.TILE_SIZE * 2);
        sprite.setHeight(Tile.TILE_SIZE * 4);
        //FIXME resource reloading
        sprite.getRenderData().setUVAndTexture(Omnikryptec.getTexturesS().get("mensch.png"));
        sprite.setLayer(100);
//        SimpleSprite light = new SimpleSprite();
//        light.setTexture(Omnikryptec.getTexturesS().get("light_2.png"));
//        light.setWidth(Tile.TILE_SIZE * 80);
//        light.setHeight(Tile.TILE_SIZE * 80);
//        //light.setColor(new Color());
//        //light.getColor().set(-100, 1, 1);
//        light.getTransform().localspaceWrite().setTranslation(-light.getWidth() / 2 + sprite.getWidth() / 2,
//                -light.getHeight() / 2 + sprite.getHeight() / 2);
        RenderComponent rc = new RenderComponent(sprite);
//        rc.light = light;
        e.addComponent(rc);
        e.addComponent(new TransformComponent());
        e.addComponent(pc);
        pc.w = Tile.TILE_SIZE * 2;
        pc.h = Tile.TILE_SIZE * 4 * 0.95f;//FIXME get width and height
        return e;
    }
    
    public Entity getPlayerEntity() {
        return this.playerEntity;
    }
    
    public Inventory getInventory() {
        return this.inventory;
    }
}
