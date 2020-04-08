package de.pcfreak9000.space.core;

import de.omnikryptec.core.Omnikryptec;
import de.omnikryptec.gui.GuiComponent;

public abstract class AbstractGui {
    
    protected final GuiComponent component;
    
    public AbstractGui() {
        this.component = new GuiComponent();
    }
    
    public void makeCurrentGui() {
        Omnikryptec.getGameS().getGuiManager().setGui(component);
    }
    
    public void removeCurrentGui() {
        Omnikryptec.getGameS().getGuiManager().setGui(null);
    }
}
