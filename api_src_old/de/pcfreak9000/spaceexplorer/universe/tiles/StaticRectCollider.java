package de.pcfreak9000.spaceexplorer.universe.tiles;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.MassType;
import org.joml.Vector2f;

import de.omnikryptec.old.physics.AdvancedRectangle;
import de.omnikryptec.old.physics.Dyn4JPhysicsWorld;
import de.omnikryptec.old.util.ConverterUtil;
import de.pcfreak9000.space.mod.Instance;

/**
 * An easy to use static solid Body (e.g. can be used as wall)
 *
 * @author pcfreak9000
 *
 */
public class StaticRectCollider {

    private final Body body;

    /**
     *
     * @param w   width
     * @param h   height
     * @param pos global position
     */
    public StaticRectCollider(final float w, final float h, final Vector2f pos) {
        this.body = new Body();
        this.body.addFixture(new AdvancedRectangle(TileDefinition.TILE_SIZE, TileDefinition.TILE_SIZE));
        this.body.getFixture(0).setRestitution(0.1);
        this.body.getTransform().setTranslation(ConverterUtil.convertToPhysics2D(pos));
        // body.getTransform().translate(ConverterUtil.convertToPhysics2D(0),
        // ConverterUtil.convertToPhysics2D(0));
        this.body.setMass(MassType.INFINITE);
    }

    /**
     * adds this Body to the physics world
     *
     * @return
     */
    public StaticRectCollider add() {
        ((Dyn4JPhysicsWorld) Instance.getCurrent2DScene().getPhysicsWorld()).getWorld().addBody(this.body);
        return this;
    }

    public Body getBody() {
        return this.body;
    }

}
