package de.pcfreak9000.space.tileworld.ecs;

import org.joml.Vector2f;
import org.joml.Vector2fc;

import de.omnikryptec.util.math.Mathf;

@Deprecated
public class Physics {
    public static class Manifold {
        float awidth, aheight, bwidth, bheight;
        Vector2f apos, bpos;
        Vector2f avel, bvel;
        float ainvMass, binvMass;
        //Results
        float penetration;
        Vector2fc normal;

    }

    public static void ResolveCollision(Manifold m) {
        // Calculate relative velocity
        Vector2f rv = m.bvel.sub(m.avel, new Vector2f());

        // Calculate relative velocity in terms of the normal direction
        float velAlongNormal = rv.dot(m.normal);

        // Do not resolve if velocities are separating
        if (velAlongNormal > 0) {
            return;
        }
        // Calculate restitution
        float e = 0;
        //float e = min( A.restitution, B.restitution)

        // Calculate impulse scalar
        float j = -(1 + e) * velAlongNormal;
        j /= m.ainvMass + m.binvMass;

        // Apply impulse
        Vector2f impulse = m.normal.mul(j, new Vector2f());
        m.avel.sub(impulse.mul(m.ainvMass, new Vector2f()), m.avel);
        m.bvel.add(impulse.mul(m.binvMass, new Vector2f()), m.bvel);
    }

    public static void PositionalCorrection(Manifold m) {
        final float percent = 0.001f; // usually 20% to 80%
        final float slop = 0.01f; // usually 0.01 to 0.1
        Vector2f correction = m.normal
                .mul((Mathf.max(m.penetration - slop, 0.0f) / (m.ainvMass + m.binvMass)) * percent, new Vector2f());
        m.apos.sub(correction.mul(m.ainvMass, new Vector2f()), m.apos);
        m.bpos.add(correction.mul(m.binvMass, new Vector2f()), m.bpos);
    }

    public static boolean AABBvsAABB(Manifold m) {

        // Vector from A to B
        Vector2f n = m.bpos.sub(m.apos, new Vector2f());

        // Calculate half extents along x axis for each object
        float a_extent = (m.awidth) / 2;
        float b_extent = (m.bwidth) / 2;

        // Calculate overlap on x axis
        float x_overlap = a_extent + b_extent - Mathf.abs(n.x);

        // SAT test on x axis
        if (x_overlap > 0) {
            // Calculate half extents along x axis for each object
            a_extent = (m.aheight) / 2;
            b_extent = (m.bheight) / 2;

            // Calculate overlap on y axis
            float y_overlap = a_extent + b_extent - Mathf.abs(n.y);

            // SAT test on y axis
            if (y_overlap > 0) {
                // Find out which axis is axis of least penetration
                if (x_overlap > y_overlap) {
                    // Point towards B knowing that n points from A to B
                    if (n.x < 0) {
                        m.normal = new Vector2f(-1, 0);
                    } else {
                        m.normal = new Vector2f(1, 0);
                        m.penetration = x_overlap;
                        return true;
                    }
                } else {
                    // Point toward B knowing that n points from A to B
                    if (n.y < 0) {
                        m.normal = new Vector2f(0, -1);
                    } else {
                        m.normal = new Vector2f(0, 1);
                        m.penetration = y_overlap;
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
