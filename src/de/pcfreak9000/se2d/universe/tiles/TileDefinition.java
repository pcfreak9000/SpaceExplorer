package de.pcfreak9000.se2d.universe.tiles;

import de.omnikryptec.util.EnumCollection.UpdateType;
import de.pcfreak9000.se2d.util.RegisterSensitive;

/**
 * Defines the general properties of a {@link Tile}.
 * 
 * @author pcfreak9000
 *
 */
@RegisterSensitive
public class TileDefinition {

	public static final float TILE_SIZE = 24;
	public static final float TILE_LAYER = 0;

	private String tex;
	private boolean prerenderable = true;
	private UpdateType type = UpdateType.STATIC;

	/**
	 * 
	 * @param t the Textures ID name
	 */
	public TileDefinition(String t) {
		this(t, true);
	}

	/**
	 * 
	 * @param t             the Textures ID name
	 * @param prerenderable are Tiles defined by this definition prerenderable?
	 *                      (e.g. if they have a special unique Animation, the
	 *                      aren't)
	 */
	public TileDefinition(String t, boolean prerenderable) {
		this.tex = t;
	}

	public String getTexture() {
		return tex;
	}

	public boolean isPrerenderable() {
		return prerenderable;
	}

	public UpdateType getUpdateType() {
		return type;
	}

	public TileDefinition setUpdateType(UpdateType t) {
		this.type = t;
		return this;
	}

	/**
	 * The recommended way to construct a new {@link Tile}
	 * 
	 * @param gtx global tile x
	 * @param gty global tile y
	 * @return a Tile
	 */
	public Tile newTile(int gtx, int gty) {
		return new Tile(this, gtx, gty);
	}

}
