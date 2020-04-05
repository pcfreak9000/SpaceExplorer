package de.pcfreak9000.space.core;

import de.omnikryptec.core.Omnikryptec;
import de.omnikryptec.event.Event;
import de.omnikryptec.event.EventBus;
import de.omnikryptec.event.EventSubscription;
import de.omnikryptec.gui.GuiComponent;
import de.omnikryptec.gui.GuiImage;
import de.omnikryptec.gui.GuiManager;
import de.omnikryptec.gui.GuiProgressBar;
import de.omnikryptec.resource.Font;
import de.omnikryptec.util.Logger;
import de.omnikryptec.util.data.Color;

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
    
    private GuiComponent component;
    
    private GuiImage background;
    
    private GuiProgressBar bar0;
    private GuiProgressBar bar1;
    
    private int max;
    private int bar0count = 0;
    
    private boolean active = false;
    
    public LoadingScreen(int max) {
        LOGGER.debugf("Creating LoadingScreen (Max stages: %d)", max);
        LOADING_STAGE_BUS.register(this);
        this.max = max;
        Font font = Omnikryptec.getFontsS().getFontSDF("candara");
        this.background = new GuiImage();
        this.background.setTexture(Omnikryptec.getTexturesS().get("hyperraum.png"));
        this.background.setMaxAlways(true);
        this.bar0 = new GuiProgressBar();
        this.bar0.setPos(0.1f, 0.5f);
        this.bar0.setSize(0.8f, 0.1f);
        this.bar0.setFont(font);
        this.bar0.colorEmpty().set(1, 0, 0);
        this.bar0.colorFull().set(0, 1, 0);
        this.bar0.setText("Loading...");
        this.bar1 = new GuiProgressBar();
        this.bar1.setPos(0.1f, 0.3f);
        this.bar1.setSize(0.8f, 0.1f);
        this.bar1.setFont(font);
        this.bar1.colorEmpty().set(1, 0, 0);
        this.bar1.colorFull().set(0, 1, 0);
        this.bar1.setEnabled(false);
        component = new GuiComponent();
        component.addComponent(background);
        component.addComponent(bar0);
        component.addComponent(bar1);
    }
    
    public void begin() {
        LOGGER.debug("Starting LoadingScreen");
        this.active = true;
        Omnikryptec.getGameS().getGuiManager().setGui(component);
    }
    
    public void end() {
        LOADING_STAGE_BUS.unregister(this);
        Omnikryptec.getGameS().getGuiManager().setGui(null);
        this.active = false;
        LOGGER.debug("Ended LoadingScreen");
    }
    
    private long lastupdate = 0;
    
    private void checkUpdate() {
        if (!active) {
            return;
        }
        long t = System.currentTimeMillis();
        if (lastupdate <= t - 1000 / 60) {
            Omnikryptec.getGameS().updateAll();
            lastupdate = t;
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
        bar0count++;
        bar0.setValue(bar0count / (float) max);
        bar0.setText(String.format("%d/%d: %s", bar0count, max, ev.name));
        if (ev.enableSecondBar) {
            bar1.setValue(0);//?
            bar1.setText("Loading...");
        }
        bar1.setEnabled(ev.enableSecondBar);
        checkUpdate();
    }
    
    @EventSubscription
    public void onLoadingSubEvent(LoadingSubEvent ev) {
        bar1.setValue(ev.number / (float) ev.max);
        bar1.setText(String.format("%d/%d: %s ", ev.number, ev.max, ev.name));
        checkUpdate();
    }
}
