package de.pcfreak9000.se2d.universe.planet.biome;

public class SingleValue {

	private float effect = 0;
	private float target = 0;
	private BiomeValueMode mode;

	public SingleValue(BiomeValueMode mode, float target, float effectperc) {
		this.effect = effectperc;
		this.target = target;
		this.mode = mode;
	}
	
	
	public float get(float in) {
		float fff = Float.NEGATIVE_INFINITY;
		switch (mode) {
		case LESS_IS_BETTER:
			fff = target - in;
			break;
		case MORE_IS_BETTER:
			fff = in - target;
			break;
		case THIS_IS_BEST:
			fff = valued(in, target);
			break;
		}
		return fff * effect;
	}

	/**
	 * the nearer in to optimal is the nearer the returned value will get to 0.
	 * 
	 * @param in
	 * @param optimal
	 * @return
	 */
	private static float valued(float in, float optimal) {
		return -Math.abs(in - optimal);
	}

}
