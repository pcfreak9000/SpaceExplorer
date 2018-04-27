package de.pcfreak9000.se2d.universe.celestial;

import de.pcfreak9000.se2d.universe.Orbit;

public abstract class CelestialBody {

	private Orbit orbit;
	private World world;

	private String name;

	public CelestialBody(Orbit orbit, World world, String name) {
		this.orbit = orbit;
		this.world = world;
		this.name = name;
	}

	public Orbit getOrbit() {
		return orbit;
	}

	public World getWorld() {
		return world;
	}

	public String getName() {
		return name;
	}

	public abstract void generateChunk(Chunk c);

}
