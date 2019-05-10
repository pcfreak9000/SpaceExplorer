package de.pcfreak9000.space.world;

import java.util.Iterator;
import java.util.Set;

public class WorldUpdater {
    
    private WorldUpdateFence fence;
    
    private Set<Chunk> localLoadedChunks;
    private World world;
    
    public void setWorldUpdateFence(WorldUpdateFence fence) {
        unloadAll();
        this.fence = fence;
        loadAll();
    }
    
    public void setWorld(World world) {
        unloadAll();
        this.world = world;
        loadAll();
    }
    
    //TODO unload all non-needed in update and load new needed
    
    private void loadAll() {
        int xR = fence.getChunkRadiusRangeX();
        int yR = fence.getChunkRadiusRangeY();
        int xM = fence.getChunkMidpointX();
        int yM = fence.getChunkMidpointY();
        for (int i = 0; i <= 2 * xR; i++) {
            for (int j = 0; j <= 2 * yR; j++) {
                int cx = i - xR + xM;
                int cy = j - yR + yM;
                if (world.inBounds(cx, cy)) {
                    localLoadedChunks.add(world.requireGet(cx, cy));
                    //TODO add chunk to scene
                }
            }
        }
    }
    
    private void unloadAll() {
        Iterator<Chunk> it = localLoadedChunks.iterator();
        while (it.hasNext()) {
            Chunk c = it.next();
            //TODO remove chunk from scene
            world.unRequire(c);
            it.remove();
        }
    }
    
}
