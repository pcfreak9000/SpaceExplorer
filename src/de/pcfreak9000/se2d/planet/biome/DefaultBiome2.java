package de.pcfreak9000.se2d.planet.biome;

import de.pcfreak9000.noise.components.NoiseWrapper;
import de.pcfreak9000.noise.noises.Noise;
import de.pcfreak9000.noise.noises.OpenSimplexNoise;
import de.pcfreak9000.se2d.planet.PlanetData;
import de.pcfreak9000.se2d.planet.TileDefinition;
import omnikryptec.resource.loader.ResourceLoader;

public class DefaultBiome2 extends BiomeDefinition{

	public DefaultBiome2() {
		super(BiomeRegistry.ENVIRONMENT_SENSITIVE);
	}

	@Override
	public boolean likes(PlanetData data, int tilex, int tiley) {
		return data.getTemperature(tilex, tiley)<45;
	}

	private TileDefinition TMP_T = new TileDefinition(ResourceLoader.currentInstance().getTexture("desert.png"));
	private TileDefinition TMP_T_2 = new TileDefinition(ResourceLoader.currentInstance().getTexture("dirt.png"));

	private static Noise noise = new NoiseWrapper(new OpenSimplexNoise()).setXScale(1.0/20).setYScale(1.0/20);
	
	@Override
	public TileDefinition getTileDefinition(PlanetData data, int tilex, int tiley) {
		return noise.valueAt(tilex, tiley)>0.4?TMP_T_2:TMP_T;
	}

}
