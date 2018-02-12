package main;

import actions.*;
import contract.DynProContract;
import display.FrontConfigurationDisplayer;
import display.ImagePanel;
import display.MetadataDisplayer;
import display.ProjectTree;
import model.FrontConfigurationVM;
import presenter.DynProPresenter;

import javax.swing.*;
import javax.swing.tree.*;

public class DynProMain implements DynProContract.View {

    private static DynProMain dynProMain = new DynProMain();
    private static DynProSwingEngine renderer = new DynProSwingEngine(dynProMain);
    private static DynProContract.Presenter presenter = new DynProPresenter(dynProMain);

    static ProjectTree projectTree;
    static JPopupMenu projectPopup, furniturePopup;

    static ImagePanel furnitureAvatar;
    static JComboBox furnitureTypeDisplay, frontConfigurationOrientation;
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
    public void displayFrontConfiguration(FrontConfigurationVM frontConfigurationVM, String orientationText) {
        frontConfigurationDisplayer.display(frontConfigurationVM, true);
        frontConfigurationOrientation.setSelectedItem(orientationText);
    }

}
