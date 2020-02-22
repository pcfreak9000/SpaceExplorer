package de.pcfreak9000.space.voxelworld.ecs;

import de.omnikryptec.ecs.component.Component;
import de.omnikryptec.render.objects.AdvancedSprite;

public class RenderComponent implements Component{

    public final AdvancedSprite sprite;
    
    public RenderComponent(AdvancedSprite sprite) {
        this.sprite = sprite;
    }
}
