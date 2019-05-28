package dmod;

import de.omnikryptec.event.EventSubscription;
import de.pcfreak9000.space.core.GameRegistry;
import de.pcfreak9000.space.mod.Instance;
import de.pcfreak9000.space.mod.Mod;
import de.pcfreak9000.space.mod.Se2DModInitEvent;
import de.pcfreak9000.space.mod.Se2DModPostInitEvent;
import de.pcfreak9000.space.mod.Se2DModPreInitEvent;
import de.pcfreak9000.space.world.tile.TileType;

@Mod(id = "SpaceExplorer2D-Dummy-Mod", name = "Kek", resourceLocation = "", version = { 0, 0, 1 })
public class DMod {
    
    @Instance
    private static DMod instance;
    
    @EventSubscription
    public void preInit(final Se2DModPreInitEvent pre) {
        System.out.println(pre.getClass());
    }
    
    @EventSubscription
    public void init(final Se2DModInitEvent init) {
        TileType testTile = new TileType();
        testTile.setTexture("is nix da dies");
        GameRegistry.TILE_REGISTRY.register("Kek vom Dienst", testTile);
    }
    
    @EventSubscription
    public void postInit(final Se2DModPostInitEvent post) {
        
    }
}
