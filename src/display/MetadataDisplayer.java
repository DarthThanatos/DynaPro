package display;

import javax.swing.*;

public class MetadataDisplayer {

    private JComboBox typeDisplay;
    private JTextField nameDisplay;
    private JSpinner heightDisplay, widthDisplay, depthDisplay, frontPriceDisplay, moduleUnitPriceDisplay;

    public void displayMetadata(String type, String name, int height, int width, int depth, int fronPrice, int moduleUnitPrice){
        typeDisplay.setSelectedItem(type);
        nameDisplay.setText(name);
        heightDisplay.setValue(height);
        widthDisplay.setValue(width);
        depthDisplay.setValue(depth);
        frontPriceDisplay.setValue(fronPrice);
        moduleUnitPriceDisplay.setValue(moduleUnitPrice);
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
}
