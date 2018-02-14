package display;

import model.ConfigurationColumnVM;
import model.ConfigurationElementVM;
import model.FrontConfigurationVM;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrontConfigurationDisplayer extends JPanel {

    private JPopupMenu frontConfigColumnOrientedPopup, frontConfigRowOrientedPopup;
    private Map<String, FrontConfigViewElem> idToComponentBinding = new HashMap<>();
    private FrontConfigurationVM frontConfigurationVM;

    private FrontConfigViewElem recentlyClickedComponent;

    void onChildElemChanged(FrontConfigViewElem frontConfigViewElem){
        recentlyClickedComponent = frontConfigViewElem;
    }

    public void display(FrontConfigurationVM frontConfigurationVM, boolean columnOriented) {
        initConfigurationDisplay(frontConfigurationVM);
        int gridx = 0, gridy = 0;
        for(ConfigurationColumnVM configurationColumnVM: frontConfigurationVM.getColumns()){
            if(columnOriented)gridy = 0; else gridx=0;
            for(ConfigurationElementVM configurationElementVM: configurationColumnVM){
                add(
                        newFrontConfigElement(configurationElementVM),
                        newConstraints(gridx, gridy, frontConfigurationVM.getColumns(), columnOriented, configurationColumnVM.size() == 1)
                );
                if(columnOriented) gridy++; else gridx++;
            }
            if(columnOriented) gridx++; else gridy++;
        }
        revalidate();
    }

    private void initConfigurationDisplay(FrontConfigurationVM frontConfigurationVM){
        this.frontConfigurationVM = frontConfigurationVM;
        removeAll();
        idToComponentBinding = new HashMap<>();
    }

    private FrontConfigViewElem newFrontConfigElement(ConfigurationElementVM configurationElementVM){
        FrontConfigViewElem configElement = new FrontConfigViewElem(
                configurationElementVM.getImagePath(),
                configurationElementVM.getModelElementKey().toString(),
                frontConfigurationVM.getFurnitureName()
        );
        idToComponentBinding.put(configurationElementVM.getModelElementKey().toString(), configElement);
        configElement.setFrontConfigurationDisplayer(this);
        return configElement;
    }

    private GridBagConstraints newConstraints(int gridx, int gridy, List<ConfigurationColumnVM> columns, boolean columnOriented, boolean singleElement){
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = gridx;
        gridBagConstraints.gridy = gridy;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.ipady = 75;
        if(singleElement){
            if(columnOriented){

            }
            else{

            }
        }
        return gridBagConstraints;
    }

    public void displayColumnOrientedPopup(String elementId){
        displayPopup(frontConfigColumnOrientedPopup, elementId);
    }


    public void displayRowOrientedPopup(String elementId){
        displayPopup(frontConfigRowOrientedPopup, elementId);
    }

    private void displayPopup(JPopupMenu popup, String elementId){
        FrontConfigViewElem component = idToComponentBinding.get(elementId);
        Point center = getCenterOf(component);
        popup.show(
                component, center.x,center.y
        );
    }

    private Point getCenterOf(FrontConfigViewElem component){
        return new Point(component.getWidth()/2, component.getHeight()/2);
    }


    @Override
    public void addMouseListener(MouseListener mouseListener){
        for(FrontConfigViewElem component : idToComponentBinding.values()) {
            component.addMouseListener(mouseListener);
        }
    }

    public void setFrontConfigColumnOrientedPopup(JPopupMenu frontConfigColumnOrientedPopup) {
        this.frontConfigColumnOrientedPopup = frontConfigColumnOrientedPopup;
    }

    public void setFrontConfigRowOrientedPopup(JPopupMenu frontConfigRowOrientedPopup) {
        this.frontConfigRowOrientedPopup = frontConfigRowOrientedPopup;
    }


    public String getRecentlyClickedFurnitureElementId(){
        return recentlyClickedComponent.getModelKey();
    }

    public String getCurrentlyDisplayedFurnitureName(){
        return frontConfigurationVM.getFurnitureName();
    }
}
