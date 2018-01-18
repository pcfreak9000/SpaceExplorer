package de.pcfreak9000.se2d.universe.planet.biome;

import java.util.ArrayList;
import java.util.Comparator;
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

	private static HashMap<BiomeDefinition, Integer> registeredBiomes = new HashMap<>();

	static {
		registerBiomeDefinition(1, new BiomeDefinition(0) {

			@Override
			public float likes(PlanetData data, int tilex, int tiley) {
				return -1000;//valued(data.getHumidity(tilex, tiley), 0.9f);
			}

			private TileDefinition def = new TileDefinition(ResourceLoader.MISSING_TEXTURE);

			@Override
			public TileDefinition getTileDefinition(PlanetData data, int tilex, int tiley) {
				return def;
			}
		});
	}

	public static BiomeDefinition getBiomeDefinition(PlanetData data, int tilex, int tiley) {
		ArrayList<BiomeDefinition> arraylist = new ArrayList<>(registeredBiomes.keySet());
		float likes = Float.NEGATIVE_INFINITY;
		for (BiomeDefinition d : arraylist) {
			likes = Math.max(d.likes(data, tilex, tiley), likes);
		}
		final float fnlLikes = likes;
		arraylist.removeIf((e) -> e.likes(data, tilex, tiley) != fnlLikes);
		return getWeightedRandom(data.getRandom(), arraylist, tilex, tiley);
	}

	public static void registerBiomeDefinition(int weight, BiomeDefinition definition) {
		registeredBiomes.put(definition, weight);
	}

	private static Noise noise = new NoiseWrapper(new OpenSimplexNoise()).setXScale(1.0 / 75).setYScale(1.0 / 75);

	private static BiomeDefinition getWeightedRandom(Random random, ArrayList<BiomeDefinition> defs, int x, int y) {
		double totalWeight = 0.0d;
		for (BiomeDefinition i : defs) {
			totalWeight += registeredBiomes.get(i);
		}
		// Now choose a random item
		double rand = (noise.valueAt(x, y) * 0.5 + 0.5) * totalWeight;
		for (BiomeDefinition i : defs) {
			rand -= registeredBiomes.get(i);
			if (rand <= 0) {
				return i;
			}
		}
		throw new IllegalStateException("Could not find any biome");
	}
}
