package main;

import actions.*;
import contract.DynProContract;
import display.FrontConfigurationDisplayer;
import display.ImagePanel;
import display.MetadataDisplayer;
import display.ProjectTree;
import model.FrontConfigurationVM;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.*;

public class DynProMain implements DynProContract.View {

    static DynProMain dynProMain = new DynProMain();
    private static DynProSwingEngine renderer = new DynProSwingEngine(dynProMain);

    static DynProContract.Presenter presenter;

    static ProjectTree projectTree;
    static JPopupMenu projectPopup, furniturePopup, frontConfigColumnOrientedPopup, frontConfigRowOrientedPopup;

    static ImagePanel furnitureAvatar;
    @SuppressWarnings("WeakerAccess") static JComboBox furnitureTypeDisplay, frontConfigurationOrientation, pedestalCB, furnitureBackOptions, furnitureRoofOptions;
    static JSpinner pedestalHeightDisplayer;
    static JTextField furnitureNameDisplay;
    static JSpinner furnitureHeightDisplay, furnitureWidthDisplay, furnitureDepthDisplay, furnitureFrontPriceDisplay, furnitureModuleUnitPriceDisplay;
    static MetadataDisplayer metadataDisplayer;
    @SuppressWarnings("WeakerAccess")  static FrontConfigurationDisplayer frontConfigurationDisplayer;

    @SuppressWarnings("unused") static JPanel frontConfigElementDialogPanel;
    @SuppressWarnings("unused") static JSpinner frontConfigElemWidth, frontConfigElemHeight;
    @SuppressWarnings("unused") static JComboBox frontConfigElemType;
    @SuppressWarnings("unused") static JTextField frontConfigElemName;


    @SuppressWarnings({"unused", "WeakerAccess"}) public static NewProjectAction newProjectAction;
    @SuppressWarnings({"unused", "WeakerAccess"}) public static RemoveFurnitureAction removeFurnitureAction;
    @SuppressWarnings({"unused", "WeakerAccess"}) public static RenameProjectTreeFurnitureAction renameFurnitureAction;
    @SuppressWarnings({"unused", "WeakerAccess"}) public static RenameProjectAction renameProjectAction;
    @SuppressWarnings({"unused", "WeakerAccess"}) public static RenameMetadataFurnitureAction renameMetadataFurnitureAction;
    @SuppressWarnings({"unused", "WeakerAccess"}) public static NewFurnitureAction newFurnitureAction;
    @SuppressWarnings({"unused", "WeakerAccess"}) public static FrontConfigAction removeFrontConfigElementAction, addElementNextToAction,
            addElementBeforeAction, addOneElementAggregateNextToAction, addMultiElementAggragateNextToAction,
            addOneElementAggregateBeforeAction, addMultiElementAggregateBeforeAction;
    @SuppressWarnings({"unused", "WeakerAccess"}) public static ModifyConfigElemAction modifyConfigElemAction;

    static JSpinner shelvesNumberDisplayer;
    static JCheckBox heightBlocker, widthBlocker, growthRingOrientationDisplayer;

    public static void main(String args[]) throws Exception {
        renderer.initActions();
        renderer.render("xml/dyn_pro_main.xml").setVisible(true);
        renderer.onAfterRender();
        presenter.attachView();
    }

    public DynProContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void displayProjectPopup() {
        projectTree.displayProjectPopup();
    }

    @Override
    public void displayFurniturePopup() {
        projectTree.displayFurniturePopup();
    }

    @Override
    public void setupProjectTreeModel(@NotNull TreeModel treeModel) {
        projectTree.setModel(treeModel);
    }

    @NotNull
    @Override
    public String promptForUserInput(@NotNull String message) {
        return JOptionPane.showInputDialog(message);
    }

    @NotNull
    @Override
    public String promptForUserInput(String message, String initialValue) {
        return JOptionPane.showInputDialog(message, initialValue);
    }

    @Override
    public void displayMetadata(@NotNull String type, @NotNull String name, int height, int width, int depth, int fronPrice, int moduleUnitPrice, @NotNull String pathToImage) {
        metadataDisplayer.displayMetadata(type, name, height, width, depth, fronPrice, moduleUnitPrice, pathToImage);

    }

    @Override
    public void displaySpecifics(@NotNull String pedestalOptionText, @NotNull String backOptionText, @NotNull String roofOptionText, int pedestalHeight) {
        pedestalCB.setSelectedItem(pedestalOptionText);
        furnitureBackOptions.setSelectedItem(backOptionText);
        furnitureRoofOptions.setSelectedItem(roofOptionText);
        pedestalHeightDisplayer.setValue(pedestalHeight);
    }


    @Override
    public void displayFrontConfiguration(@NotNull FrontConfigurationVM frontConfigurationVM, @NotNull String orientationText) {
        frontConfigurationDisplayer.display(frontConfigurationVM);
        frontConfigurationOrientation.setSelectedItem(orientationText);
    }

    @Override
    public void displayFrontConfigurationRowOrientedPopup(@NotNull String elementId) {
        frontConfigurationDisplayer.displayRowOrientedPopup(elementId);
    }

    @Override
    public void displayFrontConfigurationColumnOrientedPopup(@NotNull String elementId) {
        frontConfigurationDisplayer.displayColumnOrientedPopup(elementId);
    }


    @Override
    public void displayFrontConfigElemDialog(@NotNull String furnitureName, @NotNull String elementId, @NotNull String initialType, int initialWidth, int initialHeight, int maxWidth, int maxHeight, @NotNull String initialName, boolean widthBlocked, boolean heightBlocked, boolean growthRingVertically, int shelvesNumber) {
        frontConfigurationDisplayer.displayFrontConfigElemDialog(furnitureName, elementId, initialType, initialWidth, initialHeight, maxWidth, maxHeight,  initialName, widthBlocked, heightBlocked, growthRingVertically, shelvesNumber);
    }
}
