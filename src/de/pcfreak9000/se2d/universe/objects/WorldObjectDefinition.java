package de.pcfreak9000.se2d.universe.objects;

public class WorldObjectDefinition {
	
	private String texture;
	
	public WorldObjectDefinition(String texture) {
		this.texture = texture;
	}
	
	public String getTexture() {
		return texture;
	}
	
	public WorldObject get(float x, float y) {
		WorldObject r = new WorldObject(this);
		r.getTransform().setPosition(x, y);
		return r;
	}
}
