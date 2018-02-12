package main;

import display.FrontConfigurationDisplayer;
import display.ImageButton;
import display.ImagePanel;
import display.ProjectTree;
import org.swixml.SwingEngine;

class DynProSwingEngine extends SwingEngine {

    private DynProMain dynProMain;

    DynProSwingEngine(Object client){
        super(client);
        dynProMain = (DynProMain) client;
        getTaglib().registerTag("imagebutton", ImageButton.class);
        getTaglib().registerTag("imagepanel", ImagePanel.class);
        getTaglib().registerTag("projecttree", ProjectTree.class);
        getTaglib().registerTag("frontconfigurationdisplayer", FrontConfigurationDisplayer.class);
        DEBUG_MODE = true;
    }

    void inject(){
        setupProjectTree();
        injectProjectTree();
        setupMetadataDisplayer();
    }

    private void injectProjectTree() {
        DynProMain.renameFurnitureAction.setProjectTree(DynProMain.projectTree);
        DynProMain.removeFurnitureAction.setProjectTree(DynProMain.projectTree);
    }

    private void setupMetadataDisplayer(){
        DynProMain.metadataDisplayer.setDepthDisplay(DynProMain.furnitureDepthDisplay);
        DynProMain.metadataDisplayer.setFrontPriceDisplay(DynProMain.furnitureFrontPriceDisplay);
        DynProMain.metadataDisplayer.setHeightDisplay(DynProMain.furnitureHeightDisplay);
        DynProMain.metadataDisplayer.setModuleUnitPriceDisplay(DynProMain.furnitureModuleUnitPriceDisplay);
        DynProMain.metadataDisplayer.setWidthDisplay(DynProMain.furnitureWidthDisplay);
        DynProMain.metadataDisplayer.setNameDisplay(DynProMain.furnitureNameDisplay);
        DynProMain.metadataDisplayer.setTypeDisplay(DynProMain.furnitureTypeDisplay);
        DynProMain.metadataDisplayer.setFurnitureAvatar(DynProMain.furnitureAvatar);
    }

    private void setupProjectTree(){
        DynProMain.projectTree.attachDynProMain(dynProMain);
        DynProMain.projectTree.setProjectPopup(DynProMain.projectPopup);
        DynProMain.projectTree.setFurniturePopup(DynProMain.furniturePopup);
    }

}
