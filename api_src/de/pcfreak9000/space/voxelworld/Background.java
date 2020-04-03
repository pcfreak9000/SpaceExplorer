package de.pcfreak9000.space.voxelworld;

import de.omnikryptec.ecs.Entity;
import de.omnikryptec.render.objects.AdvancedSprite;
import de.omnikryptec.resource.TextureConfig;
import de.omnikryptec.resource.TextureConfig.WrappingMode;
import de.omnikryptec.resource.helper.TextureHelper;
import de.omnikryptec.util.data.Color;
import de.pcfreak9000.space.voxelworld.ecs.ParallaxComponent;
import de.pcfreak9000.space.voxelworld.ecs.RenderComponent;

public class Background {
    
    private static final TextureConfig BACKGROUND_CONFIG = new TextureConfig().wrappingMode(WrappingMode.Repeat);//TODO move
    
    private final String texture;
    
    private final Entity entity;
    private final AdvancedSprite sprite;
    
    public Background(String texture, float aspect, float tilingFactor, float xMov, float yMov) {
        this.texture = texture;
        this.entity = new Entity();
        this.sprite = new AdvancedSprite();
        this.entity.addComponent(new RenderComponent(sprite, true));
        this.entity.addComponent(new ParallaxComponent(xMov, yMov, aspect));
        this.sprite.setHeight(1920 * 2);
        this.sprite.setWidth(1920 * 2 * aspect);
        this.sprite.setTilingFactor(tilingFactor);
        this.sprite.setLayer(-2);//TODO layer
    }
    
    public void initTextures(TextureHelper textures) {
        sprite.setTexture(textures.get(this.texture, BACKGROUND_CONFIG));
    }
    
    public Entity getEntity() {
        return entity;
    }
}
