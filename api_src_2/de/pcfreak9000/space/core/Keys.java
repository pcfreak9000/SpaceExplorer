package de.pcfreak9000.space.core;

import de.omnikryptec.core.Omnikryptec;
import de.omnikryptec.util.settings.KeySettings;
import de.omnikryptec.util.settings.keys.KeysAndButtons;

public enum Keys {
    
    WALK_UP, WALK_DOWN, WALK_LEFT, WALK_RIGHT, DESTROY;
    
    public final String id = toString();
    
    public boolean isPressed() {
        return Omnikryptec.getInput().isPressed(id);
    }
    
    public static void applyDefaultKeyConfig(KeySettings ks) {
        ks.addKey(WALK_UP.id, KeysAndButtons.OKE_KEY_W);
        ks.addKey(WALK_DOWN.id, KeysAndButtons.OKE_KEY_S);
        ks.addKey(WALK_LEFT.id, KeysAndButtons.OKE_KEY_A);
        ks.addKey(WALK_RIGHT.id, KeysAndButtons.OKE_KEY_D);
        ks.addKey(DESTROY.id, KeysAndButtons.OKE_MOUSE_BUTTON_1);
    }
}
