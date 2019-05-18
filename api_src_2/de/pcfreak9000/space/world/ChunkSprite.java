package de.pcfreak9000.space.world;

import java.util.Map;

import de.omnikryptec.libapi.exposed.render.Texture;
import de.omnikryptec.render.batch.Batch2D;
import de.omnikryptec.render.objects.ReflectiveSprite;
import de.omnikryptec.render.renderer.RendererUtil;

public class ChunkSprite extends ReflectiveSprite {
    
    private final Map<Texture, float[]> cache;
    
    public ChunkSprite(Map<Texture, float[]> cache) {
        this.cache = cache;
        setReflectionType(Reflection2DType.Receive);
    }
    
    @Override
    public void draw(Batch2D batch) {
        RendererUtil.drawUnorderedCache(batch, cache);
    }
}
