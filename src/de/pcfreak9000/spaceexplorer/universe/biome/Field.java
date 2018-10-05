package de.pcfreak9000.spaceexplorer.universe.biome;

import de.pcfreak9000.noise.noises.Noise;

public class Field {

	private Noise noise;
	private float min, max;
	private float offset;

	public Field(float constant) {
		this(null, constant, constant);
	}

	public Field(float constant, float offset) {
		this(null, constant, constant, offset);
	}

	public Field(Noise noise, float min, float max) {
		this(noise, min, max, 0);
	}

	public Field(Noise noise, float min, float max, float offset) {
		this.noise = noise;
		this.min = min;
		this.max = max;
		this.offset = offset;
	}

	public float get(int x, int y) {
		if (isConstant()) {
			return min + offset;
		}
		return (float) (min + (max - min) * (0.5 * noise.valueAt(x, y) + 0.5) + offset);
	}

	public float getMinimum() {
		return min;
	}

	public float getMaximum() {
		return max;
	}

	public float getOffset() {
		return offset;
	}

	public boolean isConstant() {
		return noise == null || min == max;
	}

}
