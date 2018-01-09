package de.pcfreak9000.se2d.planet;

import java.util.Random;

import de.pcfreak9000.se2d.game.SpaceExplorer2D;
import omnikryptec.util.Instance;
import omnikryptec.util.Maths;
import omnikryptec.util.Util;

public class PlanetData {

	private static final long MAX_RADIUS = 500_000;
	private static final int MINDAYSEC = 60 * 5;
	private static final int MAXDAYSEC = 60 * 60;

	private static final double MINDISSTAR=10_000;
	private static final double MAXDISSTAR=100_000_000_000.0;
	
	private String name;

	private int seed = 0;
	
	private long radiusMax, radiusFade;
	// meter
	private double distanceToStar = 100;
	
	private double albedo = 0.5f;
	
	private double avgTempKelvin = 0;
	// RLSeconds
	private int avgLengthOfDay = 0;
	// RLSeconds
	private int timeOffset = 0;

	private float daytimepercentage = 0.5f;

	public PlanetData(int seed) {
		this.seed = seed;
		Random random = new Random(seed);
		name = generateName(random);
		albedo = random.nextDouble();
		distanceToStar = (Maths.normalStandardDistribution((random.nextDouble()+0) * 500)) * (MAXDISSTAR-MINDISSTAR)+MINDISSTAR;//MINDISSTAR + random.nextDouble() * (MAXDISSTAR-MINDISSTAR);
		radiusMax = (long) (Maths.normalStandardDistribution(random.nextDouble() * 3) * MAX_RADIUS);
		radiusFade = radiusMax - (long) Math.min(Math.max(0, random.nextInt(40) + 10), radiusMax * 0.1);
		daytimepercentage = (float) (random.nextDouble() * 0.6 + 0.2);
		avgLengthOfDay = MINDAYSEC + random.nextInt(MAXDAYSEC - MINDAYSEC);
		timeOffset = random.nextInt(avgLengthOfDay);
		avgTempKelvin = calculateAvgPlanetTemp(10000000000000.0, albedo, distanceToStar, daytimepercentage);
	}

	public float getTemperature(float x, float y) {
		return (float) avgTempKelvin;
	}

	public float getPlanetTimeSec() {
		return (float) SpaceExplorer2D.getSpaceExplorer2D().getGameTime() + timeOffset;
	}
	
	private static double calculateAvgPlanetTemp(double lumin, double albedo, double distanceStar,
			double daytimepercent) {
		double avgTempKelvin = Math.pow(
				//would be 16 but then the numbers are VERY high
				(lumin * (1 - albedo)) / (16 * Math.PI * Maths.STEFANBOLTZMANN * distanceStar * distanceStar), 0.25);
		avgTempKelvin *= 2 * daytimepercent;
		return avgTempKelvin;
	}

	private static String generateName(Random r) {
		return "haha";
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("===Planet===").append(Util.LINE_TERM);
		builder.append("Name: ").append(name).append(Util.LINE_TERM);
		builder.append("Seed: ").append(seed).append(Util.LINE_TERM);;
		builder.append("RadiusMax: ").append(radiusMax).append(" RadiusFade: ").append(radiusFade).append(Util.LINE_TERM);
		builder.append("Distance to star: ").append(distanceToStar).append("m").append(Util.LINE_TERM);
		builder.append("Albedo: ").append(albedo).append(Util.LINE_TERM);
		builder.append("Avg Temperature: ").append(avgTempKelvin).append("K").append(Util.LINE_TERM);;
		builder.append("Avg length of day: ").append(avgLengthOfDay).append("s UT offset: ").append(timeOffset).append("s").append(Util.LINE_TERM);
		builder.append("Daytime percentage: ").append(daytimepercentage).append(Util.LINE_TERM);
		return builder.toString();
	}

}
