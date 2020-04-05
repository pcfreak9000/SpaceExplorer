package de.pcfreak9000.space.core;

import de.omnikryptec.core.Omnikryptec;
import de.omnikryptec.event.Event;
import de.omnikryptec.event.EventBus;
import de.omnikryptec.event.EventSubscription;
import de.omnikryptec.gui.GuiComponent;
import de.omnikryptec.gui.GuiImage;
import de.omnikryptec.gui.GuiProgressBar;
import de.omnikryptec.resource.Font;
import de.omnikryptec.util.Logger;

public class LoadingScreen {
    
    private static final boolean DEBUG = true;
    
    public static class LoadingEvent extends Event {
        private final String name;
        private final boolean enableSecondBar;
        
        public LoadingEvent(String name) {
            this.name = name;
            this.enableSecondBar = false;
        }
        
        public LoadingEvent(String name, boolean b) {
            this.name = name;
            this.enableSecondBar = b;
        }
    }
    
    public static class LoadingSubEvent extends Event {
        private final String name;
        private final int number;
        private final int max;
        
        public LoadingSubEvent(String name, int number, int max) {
            this.name = name;
            this.number = number;
            this.max = max;
        }
    }
    
    private static final Logger LOGGER = Logger.getLogger(LoadingScreen.class);
    
    public static final EventBus LOADING_STAGE_BUS = new EventBus();
    
    private final GuiComponent component;
    
    private final GuiImage background;
    
    private final GuiProgressBar bar0;
    private final GuiProgressBar bar1;
    
    private final int max;
    private int bar0count = 0;
    
    private boolean active = false;
    
    public LoadingScreen(int max) {
        LOGGER.debugf("Creating LoadingScreen (Max stages: %d)", max);
        LOADING_STAGE_BUS.register(this);
        this.max = max;
        Font font = Omnikryptec.getFontsS().getFontSDF("candara");
        this.background = new GuiImage();
        this.background.setTexture(Omnikryptec.getTexturesS().get("hyperraum.png"));
        this.bar0 = new GuiProgressBar();
        this.bar0.setDimensions(0.1f, 0.5f, 0.8f, 0.1f);
        this.bar0.setFont(font);
        this.bar0.colorEmpty().set(1, 0, 0);
        this.bar0.colorFull().set(0, 1, 0);
        this.bar0.setText("Loading...");
        this.bar1 = new GuiProgressBar();
        this.bar1.setDimensions(0.1f, 0.3f, 0.8f, 0.1f);
        this.bar1.setFont(font);
        this.bar1.colorEmpty().set(1, 0, 0);
        this.bar1.colorFull().set(0, 1, 0);
        this.bar1.setVisibility(false);
        this.component = new GuiComponent();
        this.component.addComponent(this.background);
        this.component.addComponent(this.bar0);
        this.component.addComponent(this.bar1);
    }
    
    public void begin() {
        LOGGER.debug("Starting LoadingScreen");
        this.active = true;
        Omnikryptec.getGameS().getGuiManager().setGui(this.component);
    }
    
    public void end() {
        LOADING_STAGE_BUS.unregister(this);
        Omnikryptec.getGameS().getGuiManager().setGui(null);
        this.active = false;
        LOGGER.debug("Ended LoadingScreen");
    }
    
    private long lastupdate = 0;
    
    private void checkUpdate() {
        if (!this.active) {
            return;
        }
        long t = System.currentTimeMillis();
        if (this.lastupdate <= t - 1000 / 60) {
            Omnikryptec.getGameS().updateAll();
            this.lastupdate = t;
        }
        if (DEBUG) {
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    @EventSubscription
    public void onLoadingEvent(LoadingEvent ev) {
        this.bar0count++;
        this.bar0.setValue(this.bar0count / (float) this.max);
        this.bar0.setText(String.format("%d/%d: %s", this.bar0count, this.max, ev.name));
        if (ev.enableSecondBar) {
            this.bar1.setValue(0);//?
            this.bar1.setText("Loading...");
        }
        this.bar1.setVisibility(ev.enableSecondBar);
        checkUpdate();
    }
    
    @EventSubscription
    public void onLoadingSubEvent(LoadingSubEvent ev) {
        this.bar1.setValue(ev.number / (float) ev.max);
        this.bar1.setText(String.format("%d/%d: %s ", ev.number, ev.max, ev.name));
        checkUpdate();
    }
}
