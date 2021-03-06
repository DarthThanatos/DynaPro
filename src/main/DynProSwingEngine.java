package main;

import actions.*;
import contract.DynProContract;
import display.*;
import display._3d.FurniturePerspective;
import model.*;
import org.swixml.SwingEngine;
import presenter.*;

import static main.DynProMain.*;

class DynProSwingEngine extends SwingEngine {


    DynProSwingEngine(Object client){
        super(client);
        getTaglib().registerTag("imagebutton", ImageButton.class);
        getTaglib().registerTag("imagepanel", ImagePanel.class);
        getTaglib().registerTag("projecttree", ProjectTree.class);
        getTaglib().registerTag("frontconfigurationdisplayer", FrontConfigurationDisplayer.class);
        getTaglib().registerTag("furnitureperspective", FurniturePerspective.class);
        getTaglib().registerTag("furnituredisembowelmentdisplay", FurnitureDisembowelmentDisplay.class);
        getTaglib().registerTag("drevitdisembowelmentdisplay", DrevitDisembowelment.class);
        DEBUG_MODE = true;
    }


    void onAfterRender(){
        setup();
        inject();
        initPresenter();
        injectPresenter();
    }


    private void inject(){
        injectProjectTree();
        injectFrontConfigDisplayer();
        injectViewSwitcher();
        injectFurniturePerspective();
        injectDisembowelmentDisplay();
        injectFrame();
    }

    private void injectViewSwitcher() {
        moveToFurniturePerspectiveAction.setViewSwitcherPanel(viewSwitcher);
        moveToMainMenuAction.setViewSwitcherPanel(viewSwitcher);
        moveToFurnitureDisembowelmentAction.setViewSwitcherPanel(viewSwitcher);
        moveToProjectDisembowelmentAction.setViewSwitcherPanel(viewSwitcher);
    }

    private void setup(){
        setupMetadataDisplayer();
        setupFrontConfigDisplayer();
        setupProjectTree();
        setupFurnitureDisembowelmentDisplay();
        setupFurniturePerspective();
    }

    private void setupFurniturePerspective(){
        furniturePerspective.setShowFrontsCheckerBinder(new JCheckBoxBinder(showFrontsChecker));
        furniturePerspective.setShowSkeletonCheckerBinder(new JCheckBoxBinder(showSkeletonChecker));
    }

    private void setupFurnitureDisembowelmentDisplay() {
        furnitureDisembowelmentDisplay.setAssessmentDisplay(assessmentDisplay);
        furnitureDisembowelmentDisplay.setCutLengthDisplay(cutLengthDisplay);
        furnitureDisembowelmentDisplay.setScaleBoardLengthDisplay(scaleBoardLengthDisplay);

        drevitDisembowelmentDisplay.setAssessmentDisplay(assessmentDisplay);
        drevitDisembowelmentDisplay.setCutLengthDisplay(cutLengthDisplay);
        drevitDisembowelmentDisplay.setScaleBoardLengthDisplay(scaleBoardLengthDisplay);
    }

    private void initPresenter(){
       Project project = new DynProject();
       ProjectKeeper projectKeeper = new DynaProjectKeeper(project);
       DynProContract.Model model = new DynProModel(project, projectKeeper);
       FurnitureSpecificsPresenter furnitureSpecificsPresenter = new DynaProFurnitureSpecificsPresenter(model, dynProMain);
       MetadataPresenter metadataPresenter = new DynProMetadataPresenter(model, dynProMain);
       ProjectTreePresenter projectTreePresenter = new DynProTreePresenter(model, dynProMain);
       presenter = new DynProPresenter(dynProMain, furnitureSpecificsPresenter, projectTreePresenter, metadataPresenter, model);
       project.setPresenter(presenter);
       projectKeeper.setPresenter(presenter);
       projectTreePresenter.setParentPresenter(presenter);
    }


    void initActions(){
        initProjectActions();
        initFurnitureActions();
        initFrontConfigActions();
        initMoveToActions();
    }

    private void initMoveToActions() {
        moveToMainMenuAction = new MoveToMainMenuAction();
        moveToFurniturePerspectiveAction = new MoveToFurniturePerspectiveAction(dynProMain);
        moveToFurnitureDisembowelmentAction = new MoveToFurnitureDisembowelmentAction(dynProMain);
        moveToProjectDisembowelmentAction = new MoveToProjectDisembowelmentAction(dynProMain);
        switchDisembowelmentAction = new SwitchDisembowelmentAction(dynProMain);
    }

    private void injectDisembowelmentDisplay(){
        moveToFurnitureDisembowelmentAction.setFurnitureDisembowelmentDisplay(furnitureDisembowelmentDisplay);
        moveToFurnitureDisembowelmentAction.setDisembowelmentSwitcher(disembowelmentSwitcher);
        moveToFurnitureDisembowelmentAction.setDisembowelmentContainer(disembowelmentContainer);

        moveToProjectDisembowelmentAction.setFurnitureDisembowelmentDisplay(furnitureDisembowelmentDisplay);
        moveToProjectDisembowelmentAction.setDisembowelmentContainer(disembowelmentContainer);
        moveToProjectDisembowelmentAction.setDisembowelmentSwitcher(disembowelmentSwitcher);

        printDisembowelmentAction.setFurnitureDisembowelmentDisplay(furnitureDisembowelmentDisplay);
        printDisembowelmentAction.setDrevitDisembowelment(drevitDisembowelmentDisplay);

        switchDisembowelmentAction.setDisembowelmentContainer(disembowelmentContainer);
        switchDisembowelmentAction.setDrevitDisembowelment(drevitDisembowelmentDisplay);
        switchDisembowelmentAction.setFurnitureDisembowelmentDisplay(furnitureDisembowelmentDisplay);
    }

    private void initFurnitureActions(){
        removeFurnitureAction = new RemoveFurnitureAction(dynProMain);
        renameFurnitureAction = new RenameProjectTreeFurnitureAction(dynProMain);
        renameMetadataFurnitureAction = new RenameMetadataFurnitureAction(dynProMain);
        newFurnitureAction = new NewFurnitureAction(dynProMain);
        printDisembowelmentAction = new PrintDisembowelmentAction();
    }

    private void initProjectActions(){
        newProjectAction = new NewProjectAction(dynProMain);
        renameProjectAction = new RenameProjectAction(dynProMain);
        saveAction = new SaveAction(dynProMain);
        openAction = new OpenAction(dynProMain);

    }

    private void injectFurniturePerspective(){
        moveToFurniturePerspectiveAction.setFurniturePerspective(furniturePerspective);
    }

    private void injectFrame(){
        saveAction.setParent(frame);
        openAction.setParent(frame);
    }

    private void initFrontConfigActions(){
        removeFrontConfigElementAction = new RemoveFrontConfigElementAction(dynProMain);
        addElementBeforeAction = new AddElementBeforeAction(dynProMain);
        addElementNextToAction = new AddElementNextToAction(dynProMain);
        addMultiElementAggragateNextToAction = new AddMultiElementAggragateNextToAction(dynProMain);
        addMultiElementAggregateBeforeAction = new AddMultiElementAggregateBeforeAction(dynProMain);
        addOneElementAggregateBeforeAction = new AddOneElementAggregateBeforeAction(dynProMain);
        addOneElementAggregateNextToAction = new AddOneElementAggregateNextToAction(dynProMain);
        modifyConfigElemAction = new ModifyConfigElemAction(dynProMain);
    }

    private void injectProjectTree() {
        renameFurnitureAction.setProjectTree(projectTree);
        removeFurnitureAction.setProjectTree(projectTree);
    }

    private void injectFrontConfigDisplayer(){
        removeFrontConfigElementAction.setFrontConfigurationDisplayer(frontConfigurationDisplayer);
        addElementBeforeAction.setFrontConfigurationDisplayer(frontConfigurationDisplayer);
        addElementNextToAction.setFrontConfigurationDisplayer(frontConfigurationDisplayer);
        addMultiElementAggragateNextToAction.setFrontConfigurationDisplayer(frontConfigurationDisplayer);
        addMultiElementAggregateBeforeAction.setFrontConfigurationDisplayer(frontConfigurationDisplayer);
        addOneElementAggregateBeforeAction.setFrontConfigurationDisplayer(frontConfigurationDisplayer);
        addOneElementAggregateNextToAction.setFrontConfigurationDisplayer(frontConfigurationDisplayer);
    }

    private void setupMetadataDisplayer(){
        metadataDisplayer = new MetadataDisplayer();
        metadataDisplayer.setDepthDisplay(furnitureDepthDisplay);
        metadataDisplayer.setFrontPriceDisplay(furnitureFrontPriceDisplay);
        metadataDisplayer.setHeightDisplay(furnitureHeightDisplay);
        metadataDisplayer.setModuleUnitPriceDisplay(furnitureModuleUnitPriceDisplay);
        metadataDisplayer.setWidthDisplay(furnitureWidthDisplay);
        metadataDisplayer.setNameDisplay(furnitureNameDisplay);
        metadataDisplayer.setTypeDisplay(furnitureTypeDisplay);
        metadataDisplayer.setFurnitureAvatar(furnitureAvatar);
    }

    private void setupProjectTree(){
        projectTree.setProjectPopup(projectPopup);
        projectTree.setFurniturePopup(furniturePopup);
    }

    private void setupFrontConfigDisplayer(){
        frontConfigurationDisplayer.setFrontConfigColumnOrientedPopup(frontConfigColumnOrientedPopup);
        frontConfigurationDisplayer.setFrontConfigRowOrientedPopup(frontConfigRowOrientedPopup);
        frontConfigurationDisplayer.setFrontConfigElementDialogPanel(frontConfigElementDialogPanel);
        frontConfigurationDisplayer.setFrontConfigElemName(frontConfigElemName);
        frontConfigurationDisplayer.setFrontConfigElemHeight(frontConfigElemHeight);
        frontConfigurationDisplayer.setFrontConfigElemWidth(frontConfigElemWidth);
        frontConfigurationDisplayer.setFrontConfigElemType(frontConfigElemType);
        frontConfigurationDisplayer.setShelvesNumberDisplayer(shelvesNumberDisplayer);
        frontConfigurationDisplayer.setWidthBlocker(widthBlocker);
        frontConfigurationDisplayer.setHeightBlocker(heightBlocker);
        frontConfigurationDisplayer.setGrowthRingOrientationDisplayer(growthRingOrientationDisplayer);
    }

    private void injectPresenter() {
        frontConfigurationDisplayer.setPresenter(presenter);
    }
}
