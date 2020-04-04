package dmod;

import de.omnikryptec.event.EventSubscription;
import de.omnikryptec.util.data.Color;
import de.omnikryptec.util.math.Mathf;
import de.pcfreak9000.space.core.GameRegistry;
import de.pcfreak9000.space.mod.Instance;
import de.pcfreak9000.space.mod.Mod;
import de.pcfreak9000.space.mod.ModLoaderEvents;
import de.pcfreak9000.space.voxelworld.Background;
import de.pcfreak9000.space.voxelworld.Region;
import de.pcfreak9000.space.voxelworld.RegionGenerator;
import de.pcfreak9000.space.voxelworld.TileWorld;
import de.pcfreak9000.space.voxelworld.TileWorldGenerator;
import de.pcfreak9000.space.voxelworld.WorldInformationBundle;
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
        TileType tstoneTile = new TileType();
        tstoneTile.setTexture("stone.png");
        GameRegistry.TILE_REGISTRY.register("stone", tstoneTile);
        
        TileType ironTile = new TileType();
        ironTile.setTexture("ore_iron.png");
        ironTile.setLightColor(new Color(TileType.MAX_LIGHT_VALUE, TileType.MAX_LIGHT_VALUE, TileType.MAX_LIGHT_VALUE));
        GameRegistry.TILE_REGISTRY.register("ore_iron", ironTile);
        
        TileType bottom = new TileType();
        bottom.setCanBreak(false);
        bottom.setTexture("stone_dark.png");
        GameRegistry.TILE_REGISTRY.register("bottom", bottom);
        
        TileType grasstile = new TileType();
        grasstile.setTexture("grass.png");
        //grasstile.setFilterColor(new Color(0.3f, 0.3f, 0.3f));
        GameRegistry.TILE_REGISTRY.register("grass", grasstile);
        
        TileType dirttile = new TileType();
        dirttile.setTexture("dirt.png");
        dirttile.setBouncyness(1);
        //dirttile.setFilterColor(new Color(1, 0, 0, 1));
        GameRegistry.TILE_REGISTRY.register("dirt", dirttile);
        
        Background back = new Background("Space.png", 16 / 9f, 3, 1000, 1000);
        GameRegistry.BACKGROUND_REGISTRY.register("stars", back);
        
        GameRegistry.GENERATOR_REGISTRY.register("STS", new TileWorldGenerator() {
            
            @Override
            protected void initCaps() {
                CAPS.add(GeneratorCapabilitiesBase.LVL_ENTRY);
            }
            
            @Override
            public WorldInformationBundle generateWorld(long seed) {
                return new WorldInformationBundle(new TileWorld(400, 400, new RegionGenerator() {
                    @Override
                    public void generateChunk(Region chunk, TileWorld tileWorld) {
                        for (int i = 0; i < Region.REGION_TILE_SIZE; i++) {
                            for (int j = 0; j < Region.REGION_TILE_SIZE; j++) {
                                if (!tileWorld.inBounds(i + chunk.getGlobalTileX(), j + chunk.getGlobalTileY())) {
                                    continue;
                                }
                                int value = 75
                                        + Mathf.round(6 * Mathf.abs(Mathf.sin(0.2f * (i + chunk.getGlobalTileX())))
                                                + 20 * Mathf.abs(Mathf.sin(0.05f * (i + chunk.getGlobalTileX()))));
                                
                                TileType t;
                                if (j + chunk.getGlobalTileY() > value) {
                                    t = TileType.EMPTY;
                                } else {
                                    if (j + chunk.getGlobalTileY() == 0) {
                                        t = GameRegistry.TILE_REGISTRY.get("bottom");
                                    } else {
                                        if (j + chunk.getGlobalTileY() == value) {
                                            t = GameRegistry.TILE_REGISTRY.get("grass");
                                        } else if (j + chunk.getGlobalTileY() >= value - 3) {
                                            t = GameRegistry.TILE_REGISTRY.get("dirt");
                                        } else {
                                            t = GameRegistry.TILE_REGISTRY.get("stone");
                                        }
                                    }
                                }
                                Tile tile = new Tile(t, i + chunk.getGlobalTileX(), j + chunk.getGlobalTileY());
                                if (tile.getType() == tstoneTile) {
                                    if (Math.random() < 0.001) {
                                        t = ironTile;
                                    }
                                }
                                chunk.setTile(new Tile(t, i + chunk.getGlobalTileX(), j + chunk.getGlobalTileY()));
                                chunk.setTileBackground(tile);
                            }
                        }
                    }
                    
                }), GameRegistry.BACKGROUND_REGISTRY.get("stars"));
            }
        });
    }
    
    @EventSubscription
    public void postInit(final ModLoaderEvents.ModPostInitEvent post) {
        
    }
}
