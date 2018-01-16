package de.pcfreak9000.se2d.universe.star;

public class StarData {
	
	private static final double LUM_CONSTANT = 100000000000000.0;
	
	private double luminosity=1;
	
	public StarData() {
		setLuminosity(10);
	}
	
	private StarData setLuminosity(double d) {
		this.luminosity = LUM_CONSTANT * d;
		return this;
	}
	
	public double getLuminosity() {
		return luminosity;
	}
	
}
