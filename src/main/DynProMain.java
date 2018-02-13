package main;

import actions.*;
import config.Config;
import contract.DynProContract;
import display.FrontConfigurationDisplayer;
import display.ImagePanel;
import display.MetadataDisplayer;
import display.ProjectTree;
import model.FrontConfigurationVM;
import presenter.DynProPresenter;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;

public class DynProMain implements DynProContract.View {

    private static DynProMain dynProMain = new DynProMain();
    private static DynProSwingEngine renderer = new DynProSwingEngine(dynProMain);
    private static DynProContract.Presenter presenter = new DynProPresenter(dynProMain);

    static ProjectTree projectTree;
    static JPopupMenu projectPopup, furniturePopup, frontConfigColumnOrientedPopup, frontConfigRowOrientedPopup;

    static ImagePanel furnitureAvatar;
    @SuppressWarnings("WeakerAccess") static JComboBox furnitureTypeDisplay, frontConfigurationOrientation, pedestalCB, furnitureBackOptions, furnitureRoofOptions;
    static JTextField furnitureNameDisplay;
    static JSpinner furnitureHeightDisplay, furnitureWidthDisplay, furnitureDepthDisplay, furnitureFrontPriceDisplay, furnitureModuleUnitPriceDisplay;
    static MetadataDisplayer metadataDisplayer = new MetadataDisplayer();
    @SuppressWarnings("WeakerAccess")  static FrontConfigurationDisplayer frontConfigurationDisplayer;

    @SuppressWarnings("unused") public static NewProjectAction newProjectAction = new NewProjectAction(dynProMain);
    @SuppressWarnings({"unused", "WeakerAccess"}) public static RemoveFurnitureAction removeFurnitureAction = new RemoveFurnitureAction(dynProMain);
    @SuppressWarnings({"unused", "WeakerAccess"}) public static RenameProjectTreeFurnitureAction renameFurnitureAction = new RenameProjectTreeFurnitureAction(dynProMain);
    @SuppressWarnings("unused") public static RenameProjectAction renameProjectAction = new RenameProjectAction(dynProMain);
    @SuppressWarnings("unused")  public static RenameMetadataFurnitureAction renameMetadataFurnitureAction = new RenameMetadataFurnitureAction(dynProMain);
    @SuppressWarnings("unused") public static NewFurnitureAction newFurnitureAction = new NewFurnitureAction(dynProMain);
    @SuppressWarnings({"unused", "WeakerAccess"}) public static RemoveFrontConfigElementAction removeFrontConfigElementAction = new RemoveFrontConfigElementAction(dynProMain);


    public static void main(String args[]) throws Exception {
        renderer.render("xml/dyn_pro_main.xml").setVisible(true);
        renderer.inject();
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
    public void setupProjectTreeModel(TreeModel treeModel) {
        projectTree.setModel(treeModel);
    }

    @Override
    public String promptForUserInput(String message) {
        return JOptionPane.showInputDialog(message);
    }

    @Override
    public String promptForUserInput(String message, String initialValue) {
        return JOptionPane.showInputDialog(message, initialValue);
    }

    @Override
    public void displayMetadata(String type, String name, int height, int width, int depth, int fronPrice, int moduleUnitPrice, String pathToImage) {
        metadataDisplayer.displayMetadata(type, name, height, width, depth, fronPrice, moduleUnitPrice, pathToImage);

    }

    @Override
    public void displaySpecifics(String pedestalOptionText, String backOptionText, String roofOptionText) {
        pedestalCB.setSelectedItem(pedestalOptionText);
        furnitureBackOptions.setSelectedItem(backOptionText);
        furnitureRoofOptions.setSelectedItem(roofOptionText);
    }


    @Override
    public void displayFrontConfiguration(FrontConfigurationVM frontConfigurationVM, String orientationText) {
        frontConfigurationDisplayer.display(frontConfigurationVM, true);
        frontConfigurationOrientation.setSelectedItem(orientationText);
    }

    @Override
    public void displayFrontConfigurationRowOrientedPopup(String elementId) {
        frontConfigurationDisplayer.displayRowOrientedPopup(elementId);
    }

    @Override
    public void displayFrontConfigurationColumnOrientedPopup(String elementId) {
        frontConfigurationDisplayer.displayColumnOrientedPopup(elementId);
    }

}
