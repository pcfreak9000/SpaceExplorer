package de.pcfreak9000.space.world;

import java.util.Comparator;

import de.omnikryptec.render.IProjection;
import de.omnikryptec.render.objects.ReflectiveSprite;
import de.omnikryptec.render.objects.Sprite;
import de.omnikryptec.render.renderer.LocalRendererContext;
import de.omnikryptec.render.renderer.ReflectedRenderer2D;
import de.omnikryptec.util.Profiler;
import de.omnikryptec.util.updater.Time;

public class WorldRenderer extends ReflectedRenderer2D {
    
    public WorldRenderer() {
        super(12*6*Chunk.CHUNK_TILE_SIZE*Chunk.CHUNK_TILE_SIZE);
        setSpriteComparator(COMP);
    }
    
    private static final Comparator<Sprite> COMP = new Comparator<Sprite>() {
        
        @Override
        public int compare(Sprite o1, Sprite o2) {
            if (o1.getLayer() != o2.getLayer()) {
                return (int) Math.signum(o1.getLayer() - o2.getLayer());
            }
            ReflectiveSprite s1 = (ReflectiveSprite) o1;
            ReflectiveSprite s2 = (ReflectiveSprite) o2;
            return (int) Math.signum(s2.getTransform().wPosition().y() - s1.getTransform().wPosition().y());
        }
    };
    
    @Override
    public void render(Time time, IProjection projection, LocalRendererContext renderer) {
        //TODO performance
        Profiler.begin("Render world");
        forceSort();
        super.render(time, projection, renderer);
        Profiler.end();
    }
    
}
