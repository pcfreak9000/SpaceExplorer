package de.pcfreak9000.spaceexplorer.universe;

public class SpaceCoordinates {

	private long spacex;
	private long spacey;
	// private Sector sector;

	public SpaceCoordinates() {
		this(0, 0);
	}

	public SpaceCoordinates(long spacex, long spacey) {
		this.spacex = spacex;
		this.spacey = spacey;
	}

	public long getSpaceX() {
		return spacex;
	}

	public long getSpaceY() {
		return spacey;
	}

	public long distanceSquared(SpaceCoordinates others) {
		return (others.spacex - spacex) * (others.spacex - spacex)
				+ (others.spacey - spacey) * (others.spacey - spacey);
	}

	public long adjustSeedToPos(long seed) {
		return seed ^ spacex ^ spacey;
	}

}
