package de.pcfreak9000.space.tileworld.ecs;

import org.joml.Vector2f;

import de.omnikryptec.ecs.component.Component;

public class PhysicsComponent implements Component {

    public final Vector2f velocity = new Vector2f();
    public final Vector2f acceleration = new Vector2f();

    public boolean onGround = false;

    public float x, y, w, h;
    
}
