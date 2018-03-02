package de.pcfreak9000.se2d.universe.planet;

import java.util.ArrayList;
import java.util.Random;

import de.pcfreak9000.noise.components.NoiseWrapper;
import de.pcfreak9000.noise.noises.Noise;
import de.pcfreak9000.noise.noises.OpenSimplexNoise;
import de.pcfreak9000.se2d.game.SpaceExplorer2D;
import de.pcfreak9000.se2d.universe.star.StarData;
import omnikryptec.util.Maths;
import omnikryptec.util.Util;

public class PlanetData {

	// In tiles
	private static final long MAX_RADIUS = 5000;

	private static final int MINDAYSEC = 60 * 5;
	private static final int MAXDAYSEC = 60 * 60;

	private static final double MINDISSTAR = 10_000;
	private static final double MAXDISSTAR = 10_000_000_000.0;

	private static final double MINTEMPDIF = 10;
	private static final double MAXTEMPDIF = 500;

	private static final double TEMPOFFSET = 50;

	private static final char[] CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
	private static final int NAME_R_LENGTH = 5;
	private static final ArrayList<String> names = new ArrayList<>();

	private static double calculateAvgPlanetTemp(double lumin, double albedo, double distanceStar,
			double daytimepercent) {
		double avgTempKelvin = Math.pow(
				(lumin * (1 - albedo)) / (16 * Math.PI * Maths.STEFANBOLTZMANN * distanceStar * distanceStar), 0.25);
		avgTempKelvin *= 2 * daytimepercent;
		return avgTempKelvin;
	}

	private String name;

	private long seed = 0;

	private StarData star;

	private Atmosphere atmo;
	
	private long radiusMax, radiusFade;
	// meter
	private double distanceToStar = 100;

	private double albedo = 0.5f;

	private double avgTempKelvin = 0;

	private double maxTempDifKelvin = 0;
	// RLSeconds
	private int avgLengthOfDay = 0;
	// RLSeconds
	private int timeOffset = 0;

	private float daytimePercentage = 0.5f;

	private double humidity = 0.5;

	private double maxDifHumidity = 0.5;

	private Noise tempNoise, humidityNoise, heightsNoise;

	
	private Random plRandom;

	private Random chRandom;

	public PlanetData(long seed, StarData data) {
		this.seed = seed;
		this.star = data;
		plRandom = new Random(seed);
		chRandom = new Random();
		name = generateName(plRandom);
		albedo = plRandom.nextDouble();
		distanceToStar = /*
							 * (Maths.normalStandardDistribution((random.nextDouble() + 0) * 6)) *
							 * (MAXDISSTAR - MINDISSTAR) + MINDISSTAR;
							 */ MINDISSTAR + plRandom.nextDouble() * (MAXDISSTAR - MINDISSTAR);
		radiusMax = (long) (Maths.normalStandardDistribution(plRandom.nextDouble() * 2) * MAX_RADIUS);
		radiusFade = radiusMax - (long) Math.min(Math.max(0, plRandom.nextInt(40) + 10), radiusMax * 0.1);
		daytimePercentage = (float) (plRandom.nextDouble() * 0.6 + 0.2);
		avgLengthOfDay = MINDAYSEC + plRandom.nextInt(MAXDAYSEC - MINDAYSEC);
		timeOffset = plRandom.nextInt(avgLengthOfDay);
		avgTempKelvin = calculateAvgPlanetTemp(star.getLuminosity(), albedo, distanceToStar, daytimePercentage)
				+ (plRandom.nextDouble() * 0.2 + 0.9) * TEMPOFFSET;
		maxTempDifKelvin = MINTEMPDIF
				+ Maths.normalStandardDistribution((1 - plRandom.nextDouble()) * 7) * (MAXTEMPDIF - MINTEMPDIF);
		humidity = plRandom.nextDouble();
		maxDifHumidity = plRandom.nextDouble();

		tempNoise = new NoiseWrapper(new OpenSimplexNoise(seed ^ (long) avgTempKelvin)).setXScale(1 / 300.0)
				.setYScale(1 / 300.0);
		heightsNoise = new NoiseWrapper(new OpenSimplexNoise(seed ^ 0)).setXScale(1 / 100.0).setYScale(1 / 100.0);
		humidityNoise = new NoiseWrapper(new OpenSimplexNoise(seed ^ (long) humidity)).setXScale(1 / 200.0)
				.setYScale(1 / 200.0);

	}

	public float getTemperature(float x, float y) {
		return (float) (avgTempKelvin + maxTempDifKelvin * tempNoise.valueAt(x, y));
	}

	public float getHumidity(float x, float y) {
		return (float) (humidity + maxDifHumidity * humidityNoise.valueAt(x, y));
	}

	/**
	 * [-1;1]
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public float getHeight(float x, float y) {
		return (float) heightsNoise.valueAt(x, y);
	}

	public double getPlanetTimeSec() {
		return SpaceExplorer2D.getSpaceExplorer2D().getUniverse().getUniverseTimeSec() + timeOffset;
	}

	private String generateName(Random r) {
		String s;
		do {
			s = SpaceExplorer2D.getSpaceExplorer2D().getUniverse().getGalaxyName(0, 0);
			for (int i = 0; i < NAME_R_LENGTH; i++) {
				if (i == 2) {
					s += "-";
				}
				s += CHARSET[r.nextInt(CHARSET.length)];
			}
		} while (names.contains(s));
		names.add(s);
		return s;
	}

	public Random getRandom() {
		return plRandom;
	}

	public Random getChunkRandom(int x, int y) {
		chRandom.setSeed(x ^ y ^ seed);
		return chRandom;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("===Planet===").append(Util.LINE_TERM);
		builder.append("Name: ").append(name).append(Util.LINE_TERM);
		builder.append("Seed: ").append(seed).append(Util.LINE_TERM);
		builder.append("RadiusMax: ").append(radiusMax).append(" RadiusFade: ").append(radiusFade).append(" (In tiles)")
				.append(Util.LINE_TERM);
		builder.append("Distance to star: ").append(distanceToStar).append("m").append(Util.LINE_TERM);
		builder.append("Albedo: ").append(albedo).append(Util.LINE_TERM);
		builder.append("Avg temperature: ").append(avgTempKelvin).append("K Max dif temp: ").append(maxTempDifKelvin)
				.append("K").append(Util.LINE_TERM);
		builder.append("Avg length of day: ").append(avgLengthOfDay).append("s UT offset: ").append(timeOffset)
				.append("s").append(Util.LINE_TERM);
		builder.append("Daytime percentage: ").append(daytimePercentage).append(Util.LINE_TERM);
		builder.append("Humidity: ").append(humidity * 100).append("% Max dif humidity: ").append(maxDifHumidity * 100)
				.append("%").append(Util.LINE_TERM);
		return builder.toString();
	}

	// public double getsmth() {
	// return avgTempKelvin;
	// }

	public long getSeed() {
		return seed;
	}

	public long getMaxRadius() {
		return radiusMax;
	}

	public long getFadeRadius() {
		return radiusFade;
	}

	public String getName() {
		return name;
	}

	public double getDistanceToStar() {
		return distanceToStar;
	}

	public double getAlbedo() {
		return albedo;
	}

	public double getAvgTempKelvin() {
		return avgTempKelvin;
	}

	public double getMaxTempDifKelvin() {
		return maxTempDifKelvin;
	}

	public int getAvgLengthOfDay() {
		return avgLengthOfDay;
	}

	public int getTimeOffset() {
		return timeOffset;
	}

	public double getHumidity() {
		return humidity;
	}

	public double getMaxDifHumidity() {
		return maxDifHumidity;
	}

	public float getDaytimePercentage() {
		return daytimePercentage;
	}

	public double getPlanetTime() {
		return SpaceExplorer2D.getSpaceExplorer2D().getUniverse().getUniverseTimeSec() + timeOffset;
	}

}
