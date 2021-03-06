package de.pcfreak9000.spaceexplorer.game.launch;

import org.lwjgl.glfw.GLFW;

import de.pcfreak9000.space.mod.Instance;

public class KeyManager {

    public static final String KEY_PLAYER_MOVE_FORWARD = "KEY_PLAYER_MOVE_FORWARD";
    public static final String KEY_PLAYER_MOVE_BACKWARD = "KEY_PLAYER_MOVE_BACKWARD";
    public static final String KEY_PLAYER_MOVE_RIGHT = "KEY_PLAYER_MOVE_RIGHT";
    public static final String KEY_PLAYER_MOVE_LEFT = "KEY_PLAYER_MOVE_LEFT";
    public static final String KEY_FASTER = "KEY_FASTER";

    static {
        Instance.getKeySettings().setKey(KEY_PLAYER_MOVE_FORWARD, GLFW.GLFW_KEY_W, true);
        Instance.getKeySettings().setKey(KEY_PLAYER_MOVE_BACKWARD, GLFW.GLFW_KEY_S, true);
        Instance.getKeySettings().setKey(KEY_PLAYER_MOVE_RIGHT, GLFW.GLFW_KEY_D, true);
        Instance.getKeySettings().setKey(KEY_PLAYER_MOVE_LEFT, GLFW.GLFW_KEY_A, true);
        Instance.getKeySettings().setKey(KEY_FASTER, GLFW.GLFW_KEY_LEFT_SHIFT, true);
    }

    static void init() {
        System.out.println("Initing keys...");
    }

}
