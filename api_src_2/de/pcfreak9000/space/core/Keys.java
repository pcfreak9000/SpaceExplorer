package de.pcfreak9000.space.core;

import de.omnikryptec.util.settings.KeySettings;
import de.omnikryptec.util.settings.keys.KeysAndButtons;

public enum Keys {
    
    WALK_FORWARD, WALK_BACKWARD, WALK_LEFT, WALK_RIGHT;
    
    public final String id = toString();
    
    public boolean isPressed() {
        return Space.getSpace().getGameKeySettings().isPressed(id);
    }
    
    public static KeySettings createDefaultKeySettings() {
        KeySettings ks = new KeySettings();
        ks.addKey(WALK_FORWARD.id, KeysAndButtons.OKE_KEY_W);
        ks.addKey(WALK_BACKWARD.id, KeysAndButtons.OKE_KEY_S);
        ks.addKey(WALK_LEFT.id, KeysAndButtons.OKE_KEY_A);
        ks.addKey(WALK_RIGHT.id, KeysAndButtons.OKE_KEY_D);
        return ks;
    }
}
