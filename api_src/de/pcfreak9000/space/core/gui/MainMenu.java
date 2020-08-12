package de.pcfreak9000.space.core.gui;

import de.omnikryptec.core.Omnikryptec;
import de.omnikryptec.event.EventSubscription;
import de.omnikryptec.gui.GuiButton;
import de.omnikryptec.gui.GuiButton.State;
import de.omnikryptec.gui.GuiImage;
import de.omnikryptec.gui.GuiLabel;
import de.omnikryptec.resource.helper.TextureHelper;
import de.pcfreak9000.space.core.CoreEvents;
import de.pcfreak9000.space.core.Space;

public class MainMenu extends AbstractGui {
    
    private GuiImage background;
    private GuiButton buttonPlay;
    private GuiButton buttonOptions;
    private GuiButton buttonExit;
    
    public MainMenu() {
        this.background = new GuiImage();
        this.buttonPlay = new GuiButton();
        this.buttonPlay.setDimensions(0.3f, 0.4f, 0.4f, 0.1f);
        GuiLabel playLabel = new GuiLabel();
        playLabel.setFont(Omnikryptec.getFontsS().getFontSDF("candara"));
        playLabel.setText("Play");
        playLabel.setSize(0.1f);
        playLabel.setThickness(0.47f);
        playLabel.setCentered(true);
        playLabel.setDimensions(0.3f, 0.4f, 0.4f, 0.1f);
        this.buttonPlay.addComponent(playLabel);
        this.buttonPlay.addActionListener((b) -> Space.BUS.post(new CoreEvents.PlayEvent()));
        
        this.buttonOptions = new GuiButton();
        this.buttonOptions.setDimensions(0.3f, 0.25f, 0.4f, 0.1f);
        this.buttonOptions.setEnabled(false);
        GuiLabel optionsLabel = new GuiLabel();
        optionsLabel.setFont(Omnikryptec.getFontsS().getFontSDF("candara"));
        optionsLabel.setText("Options");
        optionsLabel.setSize(0.1f);
        optionsLabel.setThickness(0.47f);
        optionsLabel.setCentered(true);
        optionsLabel.setDimensions(0.3f, 0.25f, 0.4f, 0.1f);
        this.buttonOptions.addComponent(optionsLabel);
        
        this.buttonExit = new GuiButton();
        this.buttonExit.setDimensions(0.3f, 0.1f, 0.4f, 0.1f);
        this.buttonExit.addActionListener((b) -> Omnikryptec.instance().requestShutdown());
        GuiLabel exitLabel = new GuiLabel();
        exitLabel.setFont(Omnikryptec.getFontsS().getFontSDF("candara"));
        exitLabel.setText("Exit");
        exitLabel.setSize(0.1f);
        exitLabel.setThickness(0.47f);
        exitLabel.setCentered(true);
        exitLabel.setDimensions(0.3f, 0.1f, 0.4f, 0.1f);
        this.buttonExit.addComponent(exitLabel);
        
        this.component.addComponent(background);
        this.component.addComponent(buttonPlay);
        this.component.addComponent(buttonOptions);
        this.component.addComponent(buttonExit);
        
        GuiLabel title = new GuiLabel();
        title.setFont(Omnikryptec.getFontsS().getFontSDF("candara"));
        title.setText(Space.NAME);
        title.setSize(0.2f);
        title.setThickness(0.51f);
        title.setCentered(true);
        title.setDimensions(0.2f, 0.5f, 0.6f, 0.5f);
        this.component.addComponent(title);
        
        Space.BUS.register(this);
    }
    
    @EventSubscription
    public void resEvent(CoreEvents.AssignResourcesEvent ev) {
        this.background.setTexture(ev.textures.get("Space.png"));
        textureButton(buttonPlay, ev.textures);
        textureButton(buttonOptions, ev.textures);
        textureButton(buttonExit, ev.textures);
    }
    
    private void textureButton(GuiButton b, TextureHelper h) {
        b.setTexture(State.Idle, h.get("gui:button_1.png"));
        b.setTexture(State.Hovering, h.get("gui:button_2.png"));
        b.setTexture(State.Clicked, h.get("gui:button_2.png"));
        b.setTexture(State.Disabled, h.get("gui:button_3.png"));
    }
    
}
