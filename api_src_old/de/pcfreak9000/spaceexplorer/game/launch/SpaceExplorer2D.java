package de.pcfreak9000.spaceexplorer.game.launch;

import java.util.Timer;
import java.util.TimerTask;

import de.codemakers.io.file.AdvancedFile;
import de.omnikryptec.event.EventBus;
import de.omnikryptec.event.EventSubscription;
import de.omnikryptec.libapi.exposed.input.InputManager;
import de.omnikryptec.old.event.eventV2.engineevents.FrameEvent;
import de.omnikryptec.old.event.eventV2.engineevents.FrameEvent.FrameType;
import de.omnikryptec.old.gui.ProgressBar;
import de.omnikryptec.old.main.OmniKryptecEngine;
import de.pcfreak9000.space.mod.Instance;
import de.pcfreak9000.space.mod.ModManager;
import de.pcfreak9000.spaceexplorer.universe.Universe;

public class SpaceExplorer2D {

    private static final AdvancedFile RESOURCELOCATION = new AdvancedFile(true, "", "de", "pcfreak9000", "se2d", "res");
    private static final float[] PLANETPROJ = { -1920 / 2, -1080 / 2, 1920, 1080 };
    // private static final float[] PLANETPROJ = { -19200 / 2, -10800 / 2, 19200,
    // 10800 };

    public static final String EVENTBUSNAME = "SpaceExplorer2D_EVENT_BUS-2718";

    private static SpaceExplorer2D instance;

    public static SpaceExplorer2D getSpaceExplorer2D() {
        return instance;
    }

    private final AdvancedFile resourcepacks, modsfolder;

    private final ModManager manager;
    private final EventBus se2d_events;
    private Universe currentWorld = null;

    public SpaceExplorer2D(final AdvancedFile resourcepacksp, final AdvancedFile modsfolderp) {
        if (instance != null) {
            throw new IllegalStateException("SpaceExplorer2D is already created!");
        }
        this.se2d_events = new EventBus(EVENTBUSNAME, 2, 2);
        instance = this;
        this.resourcepacks = resourcepacksp;
        if (!this.resourcepacks.toFile().existsInCompilation()) {
            this.resourcepacks.setShouldBeFile(false);
            this.resourcepacks.toFile().mkdirs();
        }
        this.modsfolder = modsfolderp;
        if (!this.modsfolder.toFile().existsInCompilation()) {
            this.modsfolder.setShouldBeFile(false);
            this.modsfolder.toFile().mkdirs();
        }
        ResourceLoader.createInstanceDefault(true, false);
        this.manager = new ModManager();
        this.manager.load(this.modsfolder);
        loadRes();
        this.currentWorld = new Universe();
        this.currentWorld.loadWorld(1);
        Instance.engineBus().registerEventHandler(this);
        // TEST
        final ProgressBar b = new ProgressBar(null, null, 0.25f, 0.25f);
        b.getColor().set(0, 0.5f, 0.5f);
        b.getBarColor().set(0, 0, 1);
        b.setW(0.5f).setH(0.05f);

        final ProgressBar b2 = new ProgressBar(null, null, 0.25f, 0.5f);
        b2.getColor().set(0, 0.5f, 0.5f);
        b2.getBarColor().set(1, 0, 0);
        b2.setW(0.5f).setH(0.025f);

        b.add(b2);
        final Timer ttt = new Timer();
        ttt.schedule(new TimerTask() {

            @Override
            public void run() {
                if (b.getValue() < 1) {
                    b.setValue(b.getValue() + 0.02f);
                } else {
                    b.setValue(0);
                    if (b2.getValue() == 1) {
                        b2.setValue(0);
                    }
                    b2.setValue(b2.getValue() + 0.1f);

                }
            }
        }, 0, 10);
        OmniKryptecEngine.instance().setGui(b);
        // OmniKryptecEngine.instance().setGui(new
        // TexturedGuiObject(ResourceLoader.MISSING_TEXTURE, 0.75f,0.75f));
        // TEST
        OmniKryptecEngine.instance().startLoop();
    }

    public EventBus getEventBus() {
        return this.se2d_events;
    }

    private void loadRes() {
        ResourceLoader.currentInstance().stageAdvancedFiles(0, ResourceLoader.LOAD_XML_INFO, this.resourcepacks);
        ResourceLoader.currentInstance().loadStagedAdvancedFiles(true);
        ResourceLoader.currentInstance().clearStagedAdvancedFiles();
        ResourceLoader.currentInstance().stageAdvancedFiles(1, ResourceLoader.LOAD_XML_INFO, RESOURCELOCATION);
        ResourceLoader.currentInstance().loadStagedAdvancedFiles(false);
        ResourceLoader.currentInstance().clearStagedAdvancedFiles();
        this.manager.stageRessourceLoading();
        ResourceLoader.currentInstance().loadStagedAdvancedFiles(false);
        ResourceLoader.currentInstance().clearStagedAdvancedFiles();
    }

    public float[] getProjectionData() {
        return PLANETPROJ;
    }

    public Universe getUniverse() {
        return this.currentWorld;
    }

    @EventSubscription
    public void someFrameUpdateEventHandlerMethod(final FrameEvent ev) {
        if (ev.getType() == FrameType.PRE && this.currentWorld != null) {
            System.out.println(InputManager.getMouseHandler().getPosition());
            this.currentWorld.update();
        }
    }

}
