package de.pcfreak9000.space.world;

import java.util.Map;

import org.joml.FrustumIntersection;

import de.omnikryptec.libapi.exposed.render.Texture;
import de.omnikryptec.render.batch.Batch2D;
import de.omnikryptec.render.objects.ReflectiveSprite;
import de.omnikryptec.render.renderer.RendererUtil;
import de.pcfreak9000.space.world.tile.Tile;

public class ChunkSprite extends ReflectiveSprite {
    
    private final Map<Texture, float[]> cache;
    
    private final int cx, cy;
    
    public ChunkSprite(Map<Texture, float[]> cache, int cx, int cy) {
        this.cache = cache;
        this.cx = cx;
        this.cy = cy;
        setReflectionType(Reflection2DType.Receive);
    }
    
    @Override
    public void draw(Batch2D batch) {
        RendererUtil.drawUnorderedCache(batch, cache);
    }
    
    @Override
    public boolean isVisible(FrustumIntersection frustum) {
        return frustum.testAab(cx, cy, 0, cx + Chunk.CHUNK_TILE_SIZE * Tile.TILE_SIZE,
                cy + Chunk.CHUNK_TILE_SIZE * Tile.TILE_SIZE, 0);
    }
}
