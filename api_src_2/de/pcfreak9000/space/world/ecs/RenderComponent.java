package de.pcfreak9000.space.world.ecs;

import de.omnikryptec.ecs.component.Component;
import de.omnikryptec.render.objects.ReflectiveSprite;

public class RenderComponent implements Component{

    public final ReflectiveSprite sprite;
    
    public RenderComponent(ReflectiveSprite sprite) {
        this.sprite = sprite;
    }
}
