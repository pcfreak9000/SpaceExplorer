package de.pcfreak9000.se2d.universe.objects;

import omnikryptec.physics.AdvancedBody;

public class EntityDefinition {
	
	private String texture;
	
	public EntityDefinition(String texture) {
		this.texture = texture;
	}
	
	public String getTexture() {
		return texture;
	}
	
	public Entity get(float x, float y) {
		Entity r = new Entity(this);
		r.getTransform().setPosition(x, y);
		return r;
	}
	
	public void configureBody(AdvancedBody body) {
		
	}
}
