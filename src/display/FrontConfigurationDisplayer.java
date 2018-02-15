package display;

import model.ConfigurationAggregateVM;
import model.ConfigurationElementVM;
import model.FrontConfigurationVM;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

public class FrontConfigurationDisplayer extends JPanel {

    private JPopupMenu frontConfigColumnOrientedPopup, frontConfigRowOrientedPopup;
    private Map<String, FrontConfigViewElem> idToComponentBinding = new HashMap<>();
    private FrontConfigurationVM frontConfigurationVM;

    private FrontConfigViewElem recentlyClickedComponent;

    void onChildElemChanged(FrontConfigViewElem frontConfigViewElem){
        recentlyClickedComponent = frontConfigViewElem;
    }

    public void display(FrontConfigurationVM frontConfigurationVM) {
        initConfigurationDisplay(frontConfigurationVM);
        boolean columnOriented = frontConfigurationVM.getColumnOriented();
        int gridx = 0, gridy = 0;
        int maxNumberInAggregate = frontConfigurationVM.getMaxElementsAmount();
        for(ConfigurationAggregateVM configurationAggregateVM: frontConfigurationVM.getColumns()){
            if(columnOriented)gridy = 0; else gridx=0;
            if(configurationAggregateVM.getSize() == maxNumberInAggregate) mountStandaloneElementsGroup(gridx, gridy, configurationAggregateVM);
            else mountPanelOfElements(gridx, gridy, configurationAggregateVM, maxNumberInAggregate);
            if(columnOriented) gridx++; else gridy++;
        }
        revalidate();
    }

    private GridBagConstraints newAggregateConstraints(int gridx, int gridy, int maxNumberInAggregate){
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = gridx;
        gridBagConstraints.gridy = gridy;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        if(frontConfigurationVM.getColumnOriented()) {
            gridBagConstraints.gridheight = maxNumberInAggregate;
        }
        else {
            gridBagConstraints.gridwidth = maxNumberInAggregate;
        }
        return gridBagConstraints;
    }

    private JPanel newAggregatePanel(){
        JPanel aggregate = new JPanel();
        aggregate.setLayout(new GridBagLayout());
        return aggregate;
    }

    private GridBagConstraints newPanelElementConstrainst(){
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        if(frontConfigurationVM.getColumnOriented()){
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = GridBagConstraints.RELATIVE;
            gridBagConstraints.weighty = 1;
            gridBagConstraints.ipadx = 50;

        }
        else{
            gridBagConstraints.gridx = GridBagConstraints.RELATIVE;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.weightx = 1;
            gridBagConstraints.ipady = 75;
        }
        return gridBagConstraints;
    }

    private void mountPanelOfElements(int gridx, int gridy, ConfigurationAggregateVM configurationAggregateVM, int maxNumberInAggregate){
        JPanel aggregate = newAggregatePanel();
        for(ConfigurationElementVM configurationElementVM: configurationAggregateVM){
            aggregate.add(
                    newFrontConfigElement(configurationElementVM),
                    newPanelElementConstrainst()
            );
        }
        add(aggregate, newAggregateConstraints(gridx, gridy, maxNumberInAggregate));
    }

    private GridBagConstraints newConfigElemConstraints(int gridx, int gridy){
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = gridx;
        gridBagConstraints.gridy = gridy;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.ipady = 75;
        return gridBagConstraints;
    }

    private void mountStandaloneElementsGroup(int gridx, int gridy, ConfigurationAggregateVM configurationAggregateVM){
        for(ConfigurationElementVM configurationElementVM: configurationAggregateVM){
            add(
                    newFrontConfigElement(configurationElementVM),
                    newConfigElemConstraints(gridx, gridy)
            );
            if(frontConfigurationVM.getColumnOriented()) gridy++; else gridx++;
        }
    }

    private void initConfigurationDisplay(FrontConfigurationVM frontConfigurationVM){
        this.frontConfigurationVM = frontConfigurationVM;
        removeAll();
        getParent().repaint();
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
