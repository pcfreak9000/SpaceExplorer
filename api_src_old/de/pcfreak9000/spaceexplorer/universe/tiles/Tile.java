package de.pcfreak9000.spaceexplorer.universe.tiles;

import de.omnikryptec.old.util.EnumCollection.FixedSizeMode;
import de.omnikryptec.render.objects.Sprite;
import de.pcfreak9000.space.core.GameRegistry;
import de.pcfreak9000.spaceexplorer.universe.celestial.CelestialBody;
import de.pcfreak9000.spaceexplorer.universe.worlds.World;

/**
 * the class representing a Tile in the {@link World}
 *
 * @author pcfreak9000
 *
 */
public class Tile extends Sprite {

    private final TileDefinition myDefinition;

    private boolean validPosition = true;
    private final int gtx, gty;

    /**
     * Constructs a new Tile.That is usually done in
     * {@link TileDefinition#newTile(int, int)}
     *
     * @param def the Tile's {@link TileDefinition}
     * @param gtx global tile x
     * @param gty global tile y
     */
    public Tile(final TileDefinition def, final int gtx, final int gty) {
        super(ResourceLoader.MISSING_TEXTURE);
        this.gtx = gtx;
        this.gty = gty;
        GameRegistry.getTileRegistry().checkRegistered(def);
        this.myDefinition = def;
        setUpdateType(def.getUpdateType());
        setFixedSizeMode(FixedSizeMode.ON);
        setFixedSize(TileDefinition.TILE_SIZE, TileDefinition.TILE_SIZE);
        setLayer(TileDefinition.TILE_LAYER);
    }

    public TileDefinition getDefinition() {
        return this.myDefinition;
    }

    /**
     * Only during the generation of a {@link World}. Usually, an invalid Tile does
     * not support decoration (e.g. this can be used for the edge of a
     * {@link CelestialBody})
     */
    public void invalidate() {
        this.validPosition = false;
    }

    public boolean isValid() {
        return this.validPosition;
    }

    public int getTileX() {
        return this.gtx;
    }

    public int getTileY() {
        return this.gty;
    }

    @Override
    public String toString() {
        return super.toString() + ": " + this.myDefinition.toString();
    }

}
