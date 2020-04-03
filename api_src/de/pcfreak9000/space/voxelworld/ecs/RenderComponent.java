package de.pcfreak9000.space.voxelworld.ecs;

import de.omnikryptec.ecs.component.Component;
import de.omnikryptec.render.objects.AdvancedSprite;
import de.omnikryptec.render.objects.SimpleSprite;
import de.omnikryptec.render.objects.Sprite;

public class RenderComponent implements Component{

    public final AdvancedSprite sprite;
    public Sprite light; 
    
    public final boolean asBackground;
    
    public RenderComponent(AdvancedSprite sprite) {
        this.sprite = sprite;
        this.asBackground = false;
    }
    
    public RenderComponent(AdvancedSprite sprite, boolean asBackground) {
        this.sprite = sprite;
        this.asBackground = asBackground;
    }
}
