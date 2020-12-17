package de.pcfreak9000.space.tileworld;

import de.omnikryptec.ecs.Entity;
import de.omnikryptec.render3.d2.sprites.Sprite;
import de.omnikryptec.resource.TextureConfig;
import de.omnikryptec.resource.TextureConfig.WrappingMode;
import de.omnikryptec.resource.helper.TextureHelper;
import de.pcfreak9000.space.tileworld.ecs.ParallaxComponent;
import de.pcfreak9000.space.tileworld.ecs.RenderComponent;

public class Background {
    
    private static final TextureConfig BACKGROUND_CONFIG = new TextureConfig().wrappingMode(WrappingMode.Repeat);//TODO move
    
    private final String texture;
    
    private final Entity entity;
    private final Sprite sprite;
    
    public Background(String texture, float aspect, float tilingFactor, float xMov, float yMov) {
        this.texture = texture;
        this.entity = new Entity();
        this.sprite = new Sprite();
        this.entity.addComponent(new RenderComponent(this.sprite));
        this.entity.addComponent(new ParallaxComponent(xMov, yMov, aspect));
        this.sprite.setWidth(1920 * 2 * aspect);
        this.sprite.setHeight(1920 * 2);
        this.sprite.getRenderData().setTilingFactor(tilingFactor);
        this.sprite.setLayer(-2);//TODO layer
    }
    
    public void initTextures(TextureHelper textures) {
        this.sprite.getRenderData().setUVAndTexture(textures.get(this.texture, BACKGROUND_CONFIG));
    }
    
    public Entity getEntity() {
        return this.entity;
    }
}
