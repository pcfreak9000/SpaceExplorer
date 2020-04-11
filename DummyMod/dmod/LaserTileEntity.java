package dmod;

import de.omnikryptec.util.math.Mathf;
import de.omnikryptec.util.updater.Time;
import de.pcfreak9000.space.tileworld.Region;
import de.pcfreak9000.space.tileworld.TileWorld;
import de.pcfreak9000.space.tileworld.tile.Tickable;
import de.pcfreak9000.space.tileworld.tile.Tile;
import de.pcfreak9000.space.tileworld.tile.TileEntity;
import de.pcfreak9000.space.tileworld.tile.TileState;

public class LaserTileEntity extends TileEntity implements Tickable {
    
    private float progress = 0;
    
    private TileState myState;
    private TileWorld world;
    
    public LaserTileEntity(TileWorld w, TileState state) {
        this.world = w;
        this.myState = state;
    }
    
    @Override
    public void tick(Time time) {
        progress += time.deltaf;
        if (myState.getGlobalTileY() - progress >= 0 && progress >= 1) {
            Region tw = world.getRegion(Region.toGlobalRegion(myState.getGlobalTileX()),
                    Region.toGlobalRegion(myState.getGlobalTileY() - Mathf.floori(progress)));
            if (tw != null) {
                tw.setTile(Tile.EMPTY, myState.getGlobalTileX(), myState.getGlobalTileY() - Mathf.floori(progress));
            }
        } else if (myState.getGlobalTileY() - progress < 0) {
            progress = 0;
        }
        
    }
}
