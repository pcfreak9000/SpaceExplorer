package dmod;

import de.omnikryptec.event.EventSubscription;
import de.pcfreak9000.space.core.GameRegistry;
import de.pcfreak9000.space.mod.Instance;
import de.pcfreak9000.space.mod.Mod;
import de.pcfreak9000.space.mod.Se2DModInitEvent;
import de.pcfreak9000.space.mod.Se2DModPostInitEvent;
import de.pcfreak9000.space.mod.Se2DModPreInitEvent;
import de.pcfreak9000.space.world.Chunk;
import de.pcfreak9000.space.world.GeneratorTemplate;
import de.pcfreak9000.space.world.IGenerator;
import de.pcfreak9000.space.world.tile.Tile;
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
        testTile.getReflectiveness().set(0.7f, 0.7f, 0.7f);
        GameRegistry.TILE_REGISTRY.register("Kek vom Dienst", testTile);
        GameRegistry.GENERATOR_REGISTRY.register("STS", new GeneratorTemplate() {
            
            @Override
            public boolean satisfiesPlace() {
                return true;
            }
            
            @Override
            public IGenerator createGenerator(long seed) {
                return new IGenerator() {
                    
                    @Override
                    public void generateChunk(Chunk chunk) {
                        for (int i = 0; i < Chunk.CHUNK_TILE_SIZE; i++) {
                            for (int j = 0; j < Chunk.CHUNK_TILE_SIZE; j++) {
                                chunk.setTile(new Tile(GameRegistry.TILE_REGISTRY.get("Kek vom Dienst"),
                                        i * chunk.getChunkX() * Chunk.CHUNK_TILE_SIZE,
                                        j * chunk.getChunkY() * Chunk.CHUNK_TILE_SIZE), i, j);
                            }
                        }
                    }
                };
            }
            @Override
            public boolean canStart() {
                return true;
            }
        });
    }
    
    @EventSubscription
    public void postInit(final Se2DModPostInitEvent post) {
        
    }
}
