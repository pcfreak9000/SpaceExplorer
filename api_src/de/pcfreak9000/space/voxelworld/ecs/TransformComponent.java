package de.pcfreak9000.space.voxelworld.ecs;

import de.omnikryptec.ecs.component.Component;
import de.omnikryptec.util.math.transform.Transform2Df;

public class TransformComponent implements Component {

    public final Transform2Df transform;

    public TransformComponent() {
        this.transform = new Transform2Df();
    }

}
