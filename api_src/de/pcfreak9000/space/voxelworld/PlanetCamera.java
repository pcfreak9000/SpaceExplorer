package de.pcfreak9000.space.voxelworld;

import org.joml.Matrix4f;

import de.omnikryptec.render.AdaptiveCamera;
import de.omnikryptec.util.math.MathUtil;

public class PlanetCamera {

    private final AdaptiveCamera cam;
    private int width;
    private int height;

    public PlanetCamera() {
        this.cam = new AdaptiveCamera((w, h) -> deal(w, h));
    }

    private Matrix4f deal(int w, int h) {
        int[] vp = MathUtil.calculateViewport(w / (double) h, 1920, 1920);
        this.width = vp[2];
        this.height = vp[3];
        return new Matrix4f().ortho2D(0, vp[2], 0, vp[3]);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public AdaptiveCamera getCameraActual() {
        return this.cam;
    }

}
