package de.pcfreak9000.se2d.universe.planet.biome;

import de.pcfreak9000.noise.components.NoiseWrapper;
import de.pcfreak9000.noise.noises.Noise;
import de.pcfreak9000.noise.noises.OpenSimplexNoise;
import de.pcfreak9000.se2d.universe.planet.PlanetData;
import de.pcfreak9000.se2d.universe.planet.TileDefinition;
import omnikryptec.resource.loader.ResourceLoader;

public class DefaultBiome extends BiomeDefinition{

	public DefaultBiome() {
		super(0);
	}

	private BiomeValue value = new BiomeValue().setTemperature(new SingleValue(BiomeValueMode.THIS_IS_BEST, 45, 2)).setHumidity(new SingleValue(BiomeValueMode.THIS_IS_BEST, 0.6f, 1));
	
	@Override
	public float likes(PlanetData data, int tilex, int tiley) {
		return value.getFor(data, tilex, tiley);
	}

	private TileDefinition TMP_T = new TileDefinition(ResourceLoader.currentInstance().getTexture("grassy.png"));
	private TileDefinition TMP_T_2 = new TileDefinition(ResourceLoader.currentInstance().getTexture("water.png"));

	private static Noise noise = new NoiseWrapper(new OpenSimplexNoise()).setXScale(1.0/20).setYScale(1.0/20);
	
	@Override
	public TileDefinition getTileDefinition(PlanetData data, int tilex, int tiley) {
		return noise.valueAt(tilex, tiley)>0.4&&data.getHumidity(tilex, tiley)>0.5f?TMP_T_2:TMP_T;
	}


	
}
