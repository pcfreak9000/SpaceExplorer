package de.pcfreak9000.space.voxelworld.ecs;

import org.joml.Vector2f;

import de.omnikryptec.ecs.component.Component;

public class PhysicsComponent implements Component {
    
    public final Vector2f velocity = new Vector2f();

    public float x, y, w, h; 
    
}
