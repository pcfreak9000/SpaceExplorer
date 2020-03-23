package de.pcfreak9000.space.core;

import de.omnikryptec.core.Omnikryptec;
import de.omnikryptec.util.settings.KeySettings;
import de.omnikryptec.util.settings.keys.KeysAndButtons;

public enum Keys {
    
    FORWARD, BACKWARD, LEFT, RIGHT, UP, DOWN, DESTROY, BUILD;
    
    public final String id = toString();
    
    public boolean isPressed() {
        return Omnikryptec.getInput().isPressed(id);
    }
    
    public static void applyDefaultKeyConfig(KeySettings ks) {
        ks.addKey(FORWARD.id, KeysAndButtons.OKE_KEY_W);
        ks.addKey(BACKWARD.id, KeysAndButtons.OKE_KEY_S);
        ks.addKey(LEFT.id, KeysAndButtons.OKE_KEY_A);
        ks.addKey(RIGHT.id, KeysAndButtons.OKE_KEY_D);
        ks.addKey(UP.id, KeysAndButtons.OKE_KEY_SPACE);
        ks.addKey(DOWN.id, KeysAndButtons.OKE_KEY_LEFT_SHIFT);
        ks.addMouseKey(DESTROY.id, KeysAndButtons.OKE_MOUSE_BUTTON_LEFT);
        ks.addMouseKey(BUILD.id, KeysAndButtons.OKE_MOUSE_BUTTON_RIGHT);
    }
}
