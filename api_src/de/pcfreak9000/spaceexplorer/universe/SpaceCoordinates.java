package de.pcfreak9000.spaceexplorer.universe;

public class SpaceCoordinates {
    
    private final long spacex;
    private final long spacey;
    // private Sector sector;
    
    public SpaceCoordinates() {
        this(0, 0);
    }
    
    public SpaceCoordinates(final long spacex, final long spacey) {
        this.spacex = spacex;
        this.spacey = spacey;
    }
    
    public long getSpaceX() {
        return this.spacex;
    }
    
    public long getSpaceY() {
        return this.spacey;
    }
    
    public long distanceSquared(final SpaceCoordinates others) {
        return (others.spacex - this.spacex) * (others.spacex - this.spacex)
                + (others.spacey - this.spacey) * (others.spacey - this.spacey);
    }
    
    public long adjustSeedToPos(final long seed) {
        return seed ^ this.spacex ^ this.spacey;
    }
    
}
