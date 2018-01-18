package de.pcfreak9000.se2d.universe.planet.biome;

import de.pcfreak9000.se2d.universe.planet.PlanetData;

public class BiomeValue {


	
	private SingleValue temperature,height,humidity;
	private float startranking=0;
	
	public float getFor(PlanetData data, int tx, int ty) {
		float finall=startranking;
		if(humidity!=null) {
			finall += humidity.get(data.getHumidity(tx, ty));
		}
		if(temperature!=null) {
			finall += temperature.get(data.getTemperature(tx, ty));
		}
		if(height!=null) {
			finall += height.get(data.getHeight(tx, ty));
		}
		return finall;
	}

	public BiomeValue setTemperature(SingleValue temperature) {
		this.temperature = temperature;
		return this;
	}

	public BiomeValue setHeight(SingleValue height) {
		this.height = height;
		return this;
	}

	public BiomeValue setHumidity(SingleValue humidity) {
		this.humidity = humidity;
		return this;
	}

	public BiomeValue setStartranking(float startranking) {
		this.startranking = startranking;
		return this;
	}
	
	
	

}
