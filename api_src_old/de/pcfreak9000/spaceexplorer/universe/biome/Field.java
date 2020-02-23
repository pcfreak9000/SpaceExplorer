package de.pcfreak9000.spaceexplorer.universe.biome;

import de.pcfreak9000.noise.noises.Noise;

public class Field {
    
    private final Noise noise;
    private final float min, max;
    private final float offset;
    
    public Field(final float constant) {
        this(null, constant, constant);
    }
    
    public Field(final float constant, final float offset) {
        this(null, constant, constant, offset);
    }
    
    public Field(final Noise noise, final float min, final float max) {
        this(noise, min, max, 0);
    }
    
    public Field(final Noise noise, final float min, final float max, final float offset) {
        this.noise = noise;
        this.min = min;
        this.max = max;
        this.offset = offset;
    }
    
    public float get(final int x, final int y) {
        if (isConstant()) {
            return this.min + this.offset;
        }
        return (float) (this.min + (this.max - this.min) * (0.5 * this.noise.valueAt(x, y) + 0.5) + this.offset);
    }
    
    public float getMinimum() {
        return this.min;
    }
    
    public float getMaximum() {
        return this.max;
    }
    
    public float getOffset() {
        return this.offset;
    }
    
    public boolean isConstant() {
        return this.noise == null || this.min == this.max;
    }
    
}
