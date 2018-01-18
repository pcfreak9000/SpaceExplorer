package de.pcfreak9000.se2d.universe.planet.biome;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import de.pcfreak9000.noise.components.NoiseWrapper;
import de.pcfreak9000.noise.noises.Noise;
import de.pcfreak9000.noise.noises.OpenSimplexNoise;
import de.pcfreak9000.se2d.universe.planet.PlanetData;
import de.pcfreak9000.se2d.universe.planet.TileDefinition;
import omnikryptec.resource.loader.ResourceLoader;

public class BiomeRegistry {

	public static final int ENVIRONMENT_SENSITIVE = 1;
	public static final int ENVIRONMENT_UNSENSITIVE = 2;

	private static HashMap<BiomeDefinition, Integer> registeredBiomes = new HashMap<>();
	private static HashMap<BiomeDefinition, Integer> allrandomBiomes = new HashMap<>();

	static {
		registerBiomeDefinition(1, new BiomeDefinition(ENVIRONMENT_UNSENSITIVE) {

			@Override
			public boolean likes(PlanetData data, int tilex, int tiley) {
				// TODO Auto-generated method stub
				return true;
			}

			private TileDefinition def = new TileDefinition(ResourceLoader.MISSING_TEXTURE);

			@Override
			public TileDefinition getTileDefinition(PlanetData data, int tilex, int tiley) {
				// TODO Auto-generated method stub
				return def;
			}
		});
	}

	public static BiomeDefinition getBiomeDefinition(PlanetData data, int tilex, int tiley) {
		HashMap<BiomeDefinition, Integer> results = new HashMap<>();
		for (Entry<BiomeDefinition, Integer> def : registeredBiomes.entrySet()) {
			if (def.getKey().likes(data, tilex, tiley)) {
				results.put(def.getKey(), def.getValue());
			}
		}
		if (results.size() == 0) {
			return getWeightedRandom(data.getRandom(), allrandomBiomes, tilex, tiley);
		}
		return getWeightedRandom(data.getRandom(), results, tilex, tiley);
	}

	public static void registerBiomeDefinition(int weight, BiomeDefinition definition) {
		if (definition.isFlagSet(ENVIRONMENT_SENSITIVE)) {
			registeredBiomes.put(definition, weight);
		}
		if (definition.isFlagSet(ENVIRONMENT_UNSENSITIVE)) {
			allrandomBiomes.put(definition, weight);
		}
	}

	private static Noise noise = new NoiseWrapper(new OpenSimplexNoise()).setXScale(1.0 / 75).setYScale(1.0 / 75);

	private static BiomeDefinition getWeightedRandom(Random random, HashMap<BiomeDefinition, Integer> defs, int x,
			int y) {
		double totalWeight = 0.0d;
		for (BiomeDefinition i : defs.keySet()) {
			totalWeight += defs.get(i);
		}
		// Now choose a random item
		double rand = (noise.valueAt(x, y) * 0.5 + 0.5) * totalWeight;
		for (BiomeDefinition i : defs.keySet()) {
			rand -= defs.get(i);
			if (rand <= 0) {
				return i;
			}
		}
		throw new IllegalStateException("Could not find any biome");
	}
}
