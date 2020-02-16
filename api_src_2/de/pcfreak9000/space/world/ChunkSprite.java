package de.pcfreak9000.space.world;

import org.joml.FrustumIntersection;

import de.omnikryptec.render.batch.Batch2D;
import de.omnikryptec.render.batch.vertexmanager.OrderedCachedVertexManager;
import de.omnikryptec.render.objects.AdvancedSprite;
import de.pcfreak9000.space.world.tile.Tile;

public class ChunkSprite extends AdvancedSprite {
    
    private final OrderedCachedVertexManager cache;
    
    private final int cx, cy;
    
    public ChunkSprite(OrderedCachedVertexManager ocvm, int cx, int cy) {
        this.cache = ocvm;
        this.cx = cx;
        this.cy = cy;
        setReflectionType(Reflection2DType.Receive);
    }
    
    @Override
    public void draw(Batch2D batch) {
        this.cache.draw(batch);
        //TODO debug chunk border draw
    }
    
    @Override
    public boolean isVisible(FrustumIntersection frustum) {
        return frustum.testAab(cx * Chunk.CHUNK_TILE_SIZE * Tile.TILE_SIZE, cy * Chunk.CHUNK_TILE_SIZE * Tile.TILE_SIZE,
                0, cx * Chunk.CHUNK_TILE_SIZE * Tile.TILE_SIZE + Chunk.CHUNK_TILE_SIZE * Tile.TILE_SIZE,
                cy * Chunk.CHUNK_TILE_SIZE * Tile.TILE_SIZE + Chunk.CHUNK_TILE_SIZE * Tile.TILE_SIZE, 0);
    }
}
