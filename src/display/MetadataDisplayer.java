package display;

import javax.swing.*;

public class MetadataDisplayer {

    private ImagePanel furnitureAvatar;
    private JComboBox typeDisplay;
    private JTextField nameDisplay;
    private JSpinner heightDisplay, widthDisplay, depthDisplay, frontPriceDisplay, moduleUnitPriceDisplay;

    public void displayMetadata(String type, String name, int height, int width, int depth, int fronPrice, int moduleUnitPrice, String pathToImage){
        typeDisplay.setSelectedItem(type);
        nameDisplay.setText(name);
        heightDisplay.setModel(new SpinnerNumberModel(height, 0, 2500, 1));
        widthDisplay.setModel(new SpinnerNumberModel(width, 0, 2500, 1));
        depthDisplay.setModel(new SpinnerNumberModel(depth, 0, 2500, 1));
        frontPriceDisplay.setValue(fronPrice);
        moduleUnitPriceDisplay.setValue(moduleUnitPrice);
        furnitureAvatar.setImage(pathToImage);
    }

    public void setHeightDisplay(JSpinner heightDisplay) {
        this.heightDisplay = heightDisplay;
    }

    public void setModuleUnitPriceDisplay(JSpinner moduleUnitPriceDisplay) {
        this.moduleUnitPriceDisplay = moduleUnitPriceDisplay;
    }

    public void setFrontPriceDisplay(JSpinner frontPriceDisplay) {
        this.frontPriceDisplay = frontPriceDisplay;
    }

    public void setDepthDisplay(JSpinner depthDisplay) {
        this.depthDisplay = depthDisplay;
    }

    public void setWidthDisplay(JSpinner widthDisplay) {
        this.widthDisplay = widthDisplay;
    }

    public void setNameDisplay(JTextField nameDisplay) {
        this.nameDisplay = nameDisplay;
    }

    public void setTypeDisplay(JComboBox typeDisplay) {
        this.typeDisplay = typeDisplay;
    }

    public void setFurnitureAvatar(ImagePanel furnitureAvatar) {
        this.furnitureAvatar = furnitureAvatar;
    }
}
