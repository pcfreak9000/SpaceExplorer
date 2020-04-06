package de.pcfreak9000.space.core;

import de.omnikryptec.event.Event;
import de.omnikryptec.resource.helper.SoundHelper;
import de.omnikryptec.resource.helper.TextureHelper;

public class CoreEvents {
    
    public static class PlayEvent extends Event {
        
    }
    
    public static class AssignResourcesEvent extends Event {
        public final TextureHelper textures;
        public final SoundHelper sounds;
        
        public AssignResourcesEvent(TextureHelper textures, SoundHelper sounds) {
            this.textures = textures;
            this.sounds = sounds;
        }
    }
}
