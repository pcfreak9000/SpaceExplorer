package de.pcfreak9000.space.tileworld.ecs;

import de.omnikryptec.ecs.component.Component;
import de.omnikryptec.render3.d2.sprites.Sprite;

public class RenderComponent implements Component {
    
    public final Sprite sprite;
    
    public RenderComponent(Sprite sprite) {
        this.sprite = sprite;
    }
    
}
