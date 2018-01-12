package de.pcfreak9000.se2d.planet.biome;

import java.util.ArrayList;
import java.util.Random;

import de.pcfreak9000.se2d.planet.PlanetData;

public class BiomeRegistry {

	public static final int ENVIRONMENT_SENSITIVE = 1;
	public static final int ENVIRONMENT_UNSENSITIVE = 2;
	
	private static ArrayList<BiomeDefinition> registeredBiomes = new ArrayList<>();
	private static ArrayList<BiomeDefinition> allrandomBiomes = new ArrayList<>();
	
	public static BiomeDefinition getBiomeDefinition(PlanetData data, float worldx, float worldy) {
		ArrayList<BiomeDefinition> results = new ArrayList<>();
		for(BiomeDefinition def : registeredBiomes) {
			if(def.likes(data, worldx, worldy)) {
				results.add(def);
			}
		}
		if(results.size()==0) {
			return getWeightedRandom(data.getRandom(), allrandomBiomes);
		}
		return getWeightedRandom(data.getRandom(), results);
	}
	
	public static void registerBiomeDefinition(BiomeDefinition definition, int flag) {
		if((flag & ENVIRONMENT_SENSITIVE) == ENVIRONMENT_SENSITIVE) {
			registeredBiomes.add(definition);
		}
		if((flag & ENVIRONMENT_UNSENSITIVE) == ENVIRONMENT_UNSENSITIVE){
			allrandomBiomes.add(definition);
		}
	}
	
    private static BiomeDefinition getWeightedRandom(Random random, ArrayList<BiomeDefinition> defs){
    	int sum = 0;
    	for(BiomeDefinition i : defs) {
    		sum+=i.getWeight();
    	}
    	int rand = random.nextInt(sum-1)+1;
    	for(BiomeDefinition i : defs) {
    		rand -= i.getWeight();
    		if(rand<=0) {
    			return i;
    		}
    	}
    	return defs.get(0);
    }
}
