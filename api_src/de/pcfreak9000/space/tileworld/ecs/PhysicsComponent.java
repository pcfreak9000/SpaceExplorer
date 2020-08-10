package de.pcfreak9000.space.tileworld.ecs;

import java.util.BitSet;

import org.joml.Vector2f;

import de.omnikryptec.ecs.component.Component;

public class PhysicsComponent implements Component {
    
    public final Vector2f velocity = new Vector2f();
    public final Vector2f acceleration = new Vector2f();
    
    public boolean onGround = false;
    
    public float x, y, w, h;
    public float restitution = 0;
    
    private BitSet flags = new BitSet();
    
    public void setFlags(int index, boolean collision, boolean resolution) {
        flags.set(index * 2, collision);
        flags.set(index * 2 + 1, resolution);
    }
    
    public boolean collide(int index) {
        return flags.get(index * 2);
    }
    
    public boolean resolve(int index) {
        return flags.get(index * 2 + 1);
    }
    
}
