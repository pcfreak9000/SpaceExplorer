package dmod;

import de.omnikryptec.event.EventSubscription;
import de.omnikryptec.util.math.Mathf;
import de.pcfreak9000.space.core.GameRegistry;
import de.pcfreak9000.space.mod.Instance;
import de.pcfreak9000.space.mod.Mod;
import de.pcfreak9000.space.mod.ModLoaderEvents;
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
    public void preInit(final ModLoaderEvents.ModPreInitEvent pre) {
        System.out.println(pre.getClass());
    }
    
    @EventSubscription
    public void init(final ModLoaderEvents.ModInitEvent init) {
        TileType testTile = new TileType();
        testTile.setTexture("stone.png");
        GameRegistry.TILE_REGISTRY.register("stone", testTile);
        TileType grasstile = new TileType();
        grasstile.setTexture("grass.png");
        GameRegistry.TILE_REGISTRY.register("grass", grasstile);
        TileType dirttile = new TileType();
        dirttile.setTexture("dirt.png");
        dirttile.setBouncyness(1);
        GameRegistry.TILE_REGISTRY.register("dirt", dirttile);
        GameRegistry.GENERATOR_REGISTRY.register("STS", new TileWorldGenerator() {
            
            @Override
            protected void initCaps() {
                CAPS.add(GeneratorCapabilitiesBase.LVL_ENTRY);
            }
            
            @Override
            public TileWorld generateWorld(long seed) {
                return new TileWorld(200, 200, new RegionGenerator() {
                    @Override
                    public void generateChunk(Region chunk, TileWorld tileWorld) {
                        for (int i = 0; i < Region.REGION_TILE_SIZE; i++) {
                            for (int j = 0; j < Region.REGION_TILE_SIZE; j++) {
                                if (!tileWorld.inBounds(i + chunk.getGlobalTileX(), j + chunk.getGlobalTileY())) {
                                    continue;
                                }
                                int value = Mathf.round(20 * Mathf.abs(Mathf.sin(0.1f * (i + chunk.getGlobalTileX()))));
                                if (j + chunk.getGlobalTileY() > value) {
                                    continue;
                                }
                                TileType t;
                                if (j + chunk.getGlobalTileY() == value) {
                                    t = GameRegistry.TILE_REGISTRY.get("grass");
                                } else if (j + chunk.getGlobalTileY() >= value - 3) {
                                    t = GameRegistry.TILE_REGISTRY.get("dirt");
                                } else {
                                    t = GameRegistry.TILE_REGISTRY.get("stone");
                                }
                                chunk.setTile(new Tile(t, i + chunk.getGlobalTileX(), j + chunk.getGlobalTileY()));
                            }
                        }
                        chunk.recache();
                    }
                    
                });
            }
        });
    }
    
    @EventSubscription
    public void postInit(final ModLoaderEvents.ModPostInitEvent post) {
        
    }
}
