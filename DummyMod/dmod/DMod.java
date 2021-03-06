package dmod;

import de.omnikryptec.event.EventSubscription;
import de.omnikryptec.util.data.Color;
import de.omnikryptec.util.math.Mathf;
import de.pcfreak9000.space.core.registry.GameRegistry;
import de.pcfreak9000.space.mod.Instance;
import de.pcfreak9000.space.mod.Mod;
import de.pcfreak9000.space.mod.ModLoaderEvents;
import de.pcfreak9000.space.tileworld.Background;
import de.pcfreak9000.space.tileworld.Region;
import de.pcfreak9000.space.tileworld.TileWorld;
import de.pcfreak9000.space.tileworld.World;
import de.pcfreak9000.space.tileworld.WorldGenerator;
import de.pcfreak9000.space.tileworld.tile.Tile;
import de.pcfreak9000.space.tileworld.tile.TileEntity;
import de.pcfreak9000.space.tileworld.tile.TileState;

@Mod(id = "SpaceExplorer2D-Dummy-Mod", name = "Kek", version = { 0, 0, 1 })
public class DMod {
    
    @Instance
    private static DMod instance;
    
    @EventSubscription
    public void preInit(final ModLoaderEvents.ModPreInitEvent pre) {
        System.out.println(pre.getClass());
    }
    
    @EventSubscription
    public void init(final ModLoaderEvents.ModInitEvent init) {
        Tile tstoneTile = new Tile();
        tstoneTile.setTexture("stone.png");
        GameRegistry.TILE_REGISTRY.register("stone", tstoneTile);
        
        Tile ironTile = new Tile();
        ironTile.setTexture("ore_iron.png");
        ironTile.setLightColor(new Color(Tile.MAX_LIGHT_VALUE, Tile.MAX_LIGHT_VALUE, Tile.MAX_LIGHT_VALUE));
        GameRegistry.TILE_REGISTRY.register("ore_iron", ironTile);
        
        Tile bottom = new Tile();
        bottom.setCanBreak(false);
        bottom.setTexture("stone_dark.png");
        GameRegistry.TILE_REGISTRY.register("bottom", bottom);
        
        Tile grasstile = new Tile();
        grasstile.setTexture("grass.png");
        //grasstile.setFilterColor(new Color(0.3f, 0.3f, 0.3f));
        GameRegistry.TILE_REGISTRY.register("grass", grasstile);
        
        Tile dirttile = new Tile();
        dirttile.setTexture("dirt.png");
        dirttile.setBouncyness(1);
        //dirttile.setFilterColor(new Color(1, 0, 0, 1));
        GameRegistry.TILE_REGISTRY.register("dirt", dirttile);
        
        Tile torch = new Tile();
        torch.setLightColor(new Color(Tile.MAX_LIGHT_VALUE, Tile.MAX_LIGHT_VALUE, Tile.MAX_LIGHT_VALUE));
        
        Tile laser = new Tile() {
            @Override
            public boolean hasTileEntity() {
                return true;
            }
            
            @Override
            public TileEntity createTileEntity(TileWorld world, TileState myState) {
                return new LaserTileEntity(world, myState);
            }
        };
        laser.setTexture("dirt.png");
        laser.color().set(1, 0, 0);
        laser.setLightColor(new Color(5, 0, 0));
        GameRegistry.TILE_REGISTRY.register("laser", laser);
        
        Background back = new Background("Space.png", 16 / 9f, 3, 1000, 1000);
        GameRegistry.BACKGROUND_REGISTRY.register("stars", back);
        
        GameRegistry.GENERATOR_REGISTRY.register("STS", new WorldGenerator() {
            
            @Override
            protected void initCaps() {
                this.CAPS.add(GeneratorCapabilitiesBase.LVL_ENTRY);
            }
            
            @Override
            public World generateWorld(long seed) {
                return new World(new TileWorld(400, 400, (chunk, tileWorld) -> {
                    for (int i = 0; i < Region.REGION_TILE_SIZE; i++) {
                        for (int j = 0; j < Region.REGION_TILE_SIZE; j++) {
                            if (!tileWorld.inBounds(i + chunk.getGlobalTileX(), j + chunk.getGlobalTileY())) {
                                continue;
                            }
                            int value = 75 + Mathf.round(6 * Mathf.abs(Mathf.sin(0.2f * (i + chunk.getGlobalTileX())))
                                    + 20 * Mathf.abs(Mathf.sin(0.05f * (i + chunk.getGlobalTileX()))));
                            if (j + chunk.getGlobalTileY() > value) {
                                continue;
                            }
                            Tile t;
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
                            
                            if (t == tstoneTile) {
                                if (Math.random() < 0.001) {
                                    t = laser;
                                }
                                if(Math.random() < 0.002) {
                                    t = torch;
                                }
                            }
                            chunk.setTile(t, i + chunk.getGlobalTileX(), j + chunk.getGlobalTileY());
                            chunk.setTileBackground(t, i + chunk.getGlobalTileX(), j + chunk.getGlobalTileY());
                        }
                    }
                    //chunk.requestSunlightComputation();
                }), GameRegistry.BACKGROUND_REGISTRY.get("stars"));
            }
        });
    }
    
    @EventSubscription
    public void postInit(final ModLoaderEvents.ModPostInitEvent post) {
        
    }
}
