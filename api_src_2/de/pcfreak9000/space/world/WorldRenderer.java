package de.pcfreak9000.space.world;

import java.util.Comparator;

import de.omnikryptec.libapi.exposed.render.FrameBuffer;
import de.omnikryptec.libapi.exposed.render.RenderAPI;
import de.omnikryptec.render.IProjection;
import de.omnikryptec.render.objects.AdvancedSprite;
import de.omnikryptec.render.objects.Sprite;
import de.omnikryptec.render.renderer.AdvancedRenderer2D;
import de.omnikryptec.render.renderer.ViewManager;
import de.omnikryptec.render.renderer.ViewManager.EnvironmentKey;
import de.omnikryptec.util.profiling.Profiler;
import de.omnikryptec.util.settings.Settings;
import de.omnikryptec.util.updater.Time;

public class WorldRenderer extends AdvancedRenderer2D {
    
    public WorldRenderer() {
        super(12 * 6 * Chunk.CHUNK_TILE_SIZE * Chunk.CHUNK_TILE_SIZE);
        setSpriteComparator(COMP);
    }
    
    private static final Comparator<Sprite> COMP = new Comparator<Sprite>() {
        
        @Override
        public int compare(Sprite o1, Sprite o2) {
            if (o1.getLayer() != o2.getLayer()) {
                return (int) Math.signum(o1.getLayer() - o2.getLayer());
            }
            AdvancedSprite s1 = (AdvancedSprite) o1;
            AdvancedSprite s2 = (AdvancedSprite) o2;
            return (int) Math.signum(s2.getTransform().worldspacePos().y() - s1.getTransform().worldspacePos().y());
        }
    };
    
    @Override
    public void render(ViewManager viewManager, RenderAPI api, IProjection projection, FrameBuffer target,
            Settings<EnvironmentKey> envS, Time time) {
        //TODO performance
        Profiler.begin("Render world");
        forceSort();
        super.render(viewManager, api, projection, target, envS, time);
        Profiler.end();
    }
    
}
