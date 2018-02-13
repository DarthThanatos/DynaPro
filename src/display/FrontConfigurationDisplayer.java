package display;

import contract.DynProContract;
import model.ConfigurationColumnVM;
import model.ConfigurationElementVM;
import model.FrontConfigurationVM;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrontConfigurationDisplayer extends JPanel implements MouseListener, ActionListener {

    private DynProContract.Presenter presenter;
    private JPopupMenu frontConfigColumnOrientedPopup, frontConfigRowOrientedPopup;
    private Map<String, JComponent> idToComponentBinding = new HashMap<>();
    private Map<JComponent, String> componentToIdBinding = new HashMap<>();
    private FrontConfigurationVM frontConfigurationVM;

    private JComponent recentlyClickedComponent;

    public FrontConfigurationDisplayer(){
        addMouseListener(this);
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
        componentToIdBinding = new HashMap<>();
    }

    private ImageButton newFrontConfigElement(ConfigurationElementVM configurationElementVM){
        ImageButton configElement = new ImageButton(configurationElementVM.getImagePath(), this);
        configElement.addActionListener(this);
        idToComponentBinding.put(configurationElementVM.getModelElementKey().toString(), configElement);
        componentToIdBinding.put(configElement, configurationElementVM.getModelElementKey().toString());
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


    public void setPresenter(DynProContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public void displayColumnOrientedPopup(String elementId){
        displayPopup(frontConfigColumnOrientedPopup, elementId);
    }


    public void displayRowOrientedPopup(String elementId){
        displayPopup(frontConfigRowOrientedPopup, elementId);
    }

    private void displayPopup(JPopupMenu popup, String elementId){
        JComponent component = idToComponentBinding.get(elementId);
        Point center = getCenterOf(component);
        popup.show(
                component, center.x,center.y
        );
    }

    private Point getCenterOf(JComponent component){
        return new Point(component.getWidth()/2, component.getHeight()/2);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.isPopupTrigger()){
            recentlyClickedComponent = (JComponent) e.getSource();
            presenter.onChooseFurnitureConfigurationPopup(
                    frontConfigurationVM.getFurnitureName(),
                    componentToIdBinding.get( e.getSource())
            );
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void setFrontConfigColumnOrientedPopup(JPopupMenu frontConfigColumnOrientedPopup) {
        this.frontConfigColumnOrientedPopup = frontConfigColumnOrientedPopup;
    }

    public void setFrontConfigRowOrientedPopup(JPopupMenu frontConfigRowOrientedPopup) {
        this.frontConfigRowOrientedPopup = frontConfigRowOrientedPopup;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Action performed");
    }

    public String getRecentlyClickedFurnitureElementId(){
        return componentToIdBinding.get(recentlyClickedComponent);
    }

    public String getCurrentlyDisplayedFurnitureName(){
        return frontConfigurationVM.getFurnitureName();
    }
}
