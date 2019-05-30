package de.pcfreak9000.space.world.ecs;

import de.omnikryptec.ecs.component.Component;
import de.omnikryptec.util.math.transform.Transform3Df;

public class PlayerInputComponent implements Component{
    public float maxXv,maxYv;
    public Transform3Df cam;
}
