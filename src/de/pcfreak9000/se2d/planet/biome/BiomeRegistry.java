package de.pcfreak9000.se2d.planet.biome;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import de.pcfreak9000.se2d.planet.PlanetData;

public class BiomeRegistry {

	public static final int ENVIRONMENT_SENSITIVE = 1;
	public static final int ENVIRONMENT_UNSENSITIVE = 2;

	private static HashMap<BiomeDefinition, Integer> registeredBiomes = new HashMap<>();
	private static HashMap<BiomeDefinition, Integer> allrandomBiomes = new HashMap<>();

	public static BiomeDefinition getBiomeDefinition(PlanetData data, int tilex, int tiley) {
		HashMap<BiomeDefinition, Integer> results = new HashMap<>();
		for (Entry<BiomeDefinition, Integer> def : registeredBiomes.entrySet()) {
			if (def.getKey().likes(data, tilex, tiley)) {
				results.put(def.getKey(), def.getValue());
			}
		}
		if (results.size() == 0) {
			return getWeightedRandom(data.getRandom(), allrandomBiomes);
		}
		return getWeightedRandom(data.getRandom(), results);
	}

	public static void registerBiomeDefinition(int weight, int flag, BiomeDefinition definition) {
		if ((flag & ENVIRONMENT_SENSITIVE) == ENVIRONMENT_SENSITIVE) {
			registeredBiomes.put(definition, weight);
		}
		if ((flag & ENVIRONMENT_UNSENSITIVE) == ENVIRONMENT_UNSENSITIVE) {
			allrandomBiomes.put(definition, weight);
		}
	}

	private static BiomeDefinition getWeightedRandom(Random random, HashMap<BiomeDefinition, Integer> defs) {
		// int sum = 0;
		// for(BiomeDefinition i : defs.keySet()) {
		// sum+=defs.get(i);
		// }
		// if(sum-1<=0) {
		// return null;
		// }
		// int rand = random.nextInt(sum-1)+1;
		// for(BiomeDefinition i : defs.keySet()) {
		// rand -= defs.get(i);
		// if(rand<=0) {
		// return i;
		// }
		// }
		double totalWeight = 0.0d;
		for (BiomeDefinition i : defs.keySet()) {
			totalWeight += defs.get(i);
		}
		// Now choose a random item
		double rand = Math.random() * totalWeight;
		for (BiomeDefinition i : defs.keySet()) {
			rand -= defs.get(i);
			if (rand <= 0.0d) {
				return i;
			}
		}
		return null;
	}
}
