package display;

import config.Config;
import contract.DynProContract;
import main.DynProMain;
import model.ConfigurationAggregateVM;
import model.ConfigurationElementVM;
import model.FrontConfigurationVM;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

public class FrontConfigurationDisplayer extends JPanel {

    private DynProContract.Presenter presenter;

    private JPopupMenu frontConfigColumnOrientedPopup, frontConfigRowOrientedPopup;
    private Map<String, FrontConfigViewElem> idToComponentBinding = new HashMap<>();
    private FrontConfigurationVM frontConfigurationVM;

    private FrontConfigViewElem recentlyClickedComponent;

    private JPanel frontConfigElementDialogPanel;
    private JSpinner frontConfigElemWidth, frontConfigElemHeight;
    private JComboBox frontConfigElemType;
    private JTextField frontConfigElemName;

    void onChildElemChanged(FrontConfigViewElem frontConfigViewElem){
        recentlyClickedComponent = frontConfigViewElem;
    }

    public FrontConfigurationDisplayer(){
        ToolTipManager.sharedInstance().setInitialDelay(0);
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
        configElement.setAction(DynProMain.modifyConfigElemAction);
        configElement.setToolTipText(configurationElementVM.getTooltip());
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


    @SuppressWarnings("ConstantConditions")
    public void displayFrontConfigElemDialog(String furnitureName, String elementId, String initialType, int initialWidth, int initialHeight, String initialElemName){

        frontConfigElemType.setSelectedItem(initialType);
        frontConfigElemWidth.setValue(initialWidth);
        frontConfigElemHeight.setValue(initialHeight);
        frontConfigElemName.setText(initialElemName);

        frontConfigElementDialogPanel.setVisible(true);

        int optionClicked = JOptionPane.showConfirmDialog(
                null,
                frontConfigElementDialogPanel,
                Config.FRONT_CONFIG_ELEM_DIALOG_TITLE_PL,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        frontConfigElementDialogPanel.setVisible(false);

        if(optionClicked == JOptionPane.OK_OPTION){
            idToComponentBinding.get(elementId).setToolTipText(
                    String.format(
                            Config.FRONT_CONFIG_ELEMENT_TIP_FORMAT,
                            frontConfigElemName.getText(),
                            frontConfigElemType.getSelectedItem().toString(),
                            Integer.parseInt(frontConfigElemWidth.getValue().toString()),
                            Integer.parseInt(frontConfigElemHeight.getValue().toString())
                    )
            );
            presenter.onModifyFrontConfigElement(
                    furnitureName,
                    elementId,
                    frontConfigElemType.getSelectedItem().toString(),
                    Integer.parseInt(frontConfigElemWidth.getValue().toString()),
                    Integer.parseInt(frontConfigElemHeight.getValue().toString()),
                    frontConfigElemName.getText()
            );
        }
    }

    public void setFrontConfigElementDialogPanel(JPanel frontConfigElementDialogPanel) {
        this.frontConfigElementDialogPanel = frontConfigElementDialogPanel;
    }

    public void setFrontConfigElemWidth(JSpinner frontConfigElemWidth) {
        this.frontConfigElemWidth = frontConfigElemWidth;
    }

    public void setFrontConfigElemHeight(JSpinner frontConfigElemHeight) {
        this.frontConfigElemHeight = frontConfigElemHeight;
    }

    public void setFrontConfigElemType(JComboBox frontConfigElemType) {
        this.frontConfigElemType = frontConfigElemType;
    }

    public void setFrontConfigElemName(JTextField frontConfigElemeName) {
        this.frontConfigElemName = frontConfigElemeName;
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

    public void setPresenter(DynProContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
