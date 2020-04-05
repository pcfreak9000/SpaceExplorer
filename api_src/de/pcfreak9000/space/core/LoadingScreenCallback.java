package de.pcfreak9000.space.core;

import de.codemakers.io.file.AdvancedFile;
import de.omnikryptec.resource.loadervpc.LoadingProgressCallback;

public class LoadingScreenCallback implements LoadingProgressCallback {
    
    private int grm;
    
    @Override
    public void onLoadingStart(int globalResMax, int globalMaxStages) {
        this.grm = globalResMax;
        LoadingScreen.LOADING_STAGE_BUS.post(new LoadingScreen.LoadingEvent("Loading resources", true));
    }
    
    @Override
    public void onStageChange(AdvancedFile superfile, int stageResMax, int stageNumber) {
    }
    
    @Override
    public void onProgressChange(AdvancedFile file, int stageResProcessedCount) {
        LoadingScreen.LOADING_STAGE_BUS
                .post(new LoadingScreen.LoadingSubEvent(file.getName(), stageResProcessedCount, grm));
    }
    
    @Override
    public void onLoadingDone() {

    }
    
}
