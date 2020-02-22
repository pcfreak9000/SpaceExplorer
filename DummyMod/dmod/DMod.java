package dmod;

import de.omnikryptec.event.EventSubscription;
import de.pcfreak9000.space.core.GameRegistry;
import de.pcfreak9000.space.mod.Instance;
import de.pcfreak9000.space.mod.Mod;
import de.pcfreak9000.space.mod.Se2DModInitEvent;
import de.pcfreak9000.space.mod.Se2DModPostInitEvent;
import de.pcfreak9000.space.mod.Se2DModPreInitEvent;
import de.pcfreak9000.space.voxelworld.Region;
import de.pcfreak9000.space.voxelworld.RegionGenerator;
import de.pcfreak9000.space.voxelworld.TileWorld;
import de.pcfreak9000.space.voxelworld.TileWorldGenerator;
import de.pcfreak9000.space.voxelworld.tile.Tile;
import de.pcfreak9000.space.voxelworld.tile.TileType;

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
        GameRegistry.GENERATOR_REGISTRY.register("STS", new TileWorldGenerator() {
            
            @Override
            protected void initCaps() {
                CAPS.add(GeneratorCapabilitiesBase.LVL_ENTRY);
            }
            
            @Override
            public TileWorld generateWorld(long seed) {
                return new TileWorld(10, 10, new RegionGenerator() {
                    @Override
                    public void generateChunk(Region chunk) {
                        for (int i = 0; i < Region.REGION_TILE_SIZE; i++) {
                            for (int j = 0; j < Region.REGION_TILE_SIZE; j++) {
                                chunk.setTile(new Tile(GameRegistry.TILE_REGISTRY.get("Kek vom Dienst"),
                                        i + chunk.getGlobalTileX(), j + chunk.getGlobalTileY()));
                            }
                        }
                        chunk.recache();
                    }
                    
                });
            }
        });
    }
    
    @EventSubscription
    public void postInit(final Se2DModPostInitEvent post) {
        
    }
}
