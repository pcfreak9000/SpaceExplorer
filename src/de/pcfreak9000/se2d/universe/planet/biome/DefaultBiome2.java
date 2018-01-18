package de.pcfreak9000.se2d.universe.planet.biome;

import com.google.common.collect.MapDifference.ValueDifference;

import de.pcfreak9000.noise.components.NoiseWrapper;
import de.pcfreak9000.noise.noises.Noise;
import de.pcfreak9000.noise.noises.OpenSimplexNoise;
import de.pcfreak9000.se2d.universe.planet.PlanetData;
import de.pcfreak9000.se2d.universe.planet.TileDefinition;
import omnikryptec.resource.loader.ResourceLoader;

public class DefaultBiome2 extends BiomeDefinition{

	public DefaultBiome2() {
		super(0);
	}

	private BiomeValue value = new BiomeValue().setTemperature(new SingleValue(BiomeValueMode.MORE_IS_BETTER, 50, 1.5f)).setHumidity(new SingleValue(BiomeValueMode.LESS_IS_BETTER, 0.4f, 1));

	
	@Override
	public float likes(PlanetData data, int tilex, int tiley) {
		return value.getFor(data, tilex, tiley);
	}

	private TileDefinition TMP_T = new TileDefinition(ResourceLoader.currentInstance().getTexture("desert.png"));
	private TileDefinition TMP_T_2 = new TileDefinition(ResourceLoader.currentInstance().getTexture("dirt.png"));

	private static Noise noise = new NoiseWrapper(new OpenSimplexNoise()).setXScale(1.0/20).setYScale(1.0/20);
	
	@Override
	public TileDefinition getTileDefinition(PlanetData data, int tilex, int tiley) {
		return noise.valueAt(tilex, tiley)>0.4?TMP_T_2:TMP_T;
	}

}
