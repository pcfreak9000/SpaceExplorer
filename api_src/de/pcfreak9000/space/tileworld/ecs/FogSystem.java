package de.pcfreak9000.space.tileworld.ecs;

import java.util.BitSet;
import java.util.Random;

import de.omnikryptec.core.Omnikryptec;
import de.omnikryptec.ecs.Entity;
import de.omnikryptec.ecs.IECSManager;
import de.omnikryptec.ecs.system.AbstractComponentSystem;
import de.omnikryptec.render.objects.AdvancedSprite;
import de.omnikryptec.util.data.Color;
import de.omnikryptec.util.updater.Time;

public class FogSystem extends AbstractComponentSystem {
    
    public FogSystem() {
        super(new BitSet());
    }
    
    @Override
    public void update(IECSManager iecsManager, Time time) {
        if (time.opCount % 10 == 0) {
            AdvancedSprite sprite = new AdvancedSprite();
            Random rand = new Random();
            sprite.setTexture(Omnikryptec.getTexturesS().get("fog:fog_" + (rand.nextInt(5) + 1) + ".png"));
            sprite.setHeight(450);
            sprite.setWidth(1500);
            sprite.setLayer(200);
            sprite.setColor(new Color(0, 0.5f, 0.5f));
            RenderComponent rc = new RenderComponent(sprite);
            PhysicsComponent pc = new PhysicsComponent();
            pc.w = 0;
            pc.h = 0;
            pc.velocity.set(rand.nextInt(800), rand.nextInt(500));
            TransformComponent tc = new TransformComponent();
            Entity entity = new Entity();
            entity.addComponent(pc).addComponent(rc).addComponent(tc);
            iecsManager.addEntity(entity);
        }
    }
}
