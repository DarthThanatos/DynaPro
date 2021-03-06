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
    private JSpinner shelvesNumberDisplayer;
    private JCheckBox heightBlocker, widthBlocker, growthRingOrientationDisplayer;

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
            mountPanelOfElements(gridx, gridy, configurationAggregateVM, maxNumberInAggregate);
            if(columnOriented) gridx++; else gridy++;
        }
        revalidate();
    }

    private JPanel newAggregatePanel(){
        JPanel aggregate = new JPanel();
        aggregate.setLayout(new GridBagLayout());
        return aggregate;
    }

    private GridBagConstraints newAggregateConstraints(ConfigurationElementVM configurationElementVM,int gridx, int gridy, int maxNumberInAggregate){
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = gridx;
        gridBagConstraints.gridy = gridy;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        if(frontConfigurationVM.getColumnOriented()) {
            gridBagConstraints.gridheight = maxNumberInAggregate;
            gridBagConstraints.weightx = configurationElementVM.getWidthX();
            gridBagConstraints.weighty = 1;
        }
        else {
            gridBagConstraints.gridwidth = maxNumberInAggregate;
            gridBagConstraints.weightx = 1;
            gridBagConstraints.weighty = configurationElementVM.getWidthY();

        }
        return gridBagConstraints;
    }

    private GridBagConstraints newPanelElementConstrainst(ConfigurationElementVM configurationElementVM){
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        if(frontConfigurationVM.getColumnOriented()){
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = GridBagConstraints.RELATIVE;

        }
        else{
            gridBagConstraints.gridx = GridBagConstraints.RELATIVE;
            gridBagConstraints.gridy = 0;
        }
        gridBagConstraints.weightx = configurationElementVM.getWidthX();
        gridBagConstraints.weighty = configurationElementVM.getWidthY();
        return gridBagConstraints;
    }

    private void mountPanelOfElements(int gridx, int gridy, ConfigurationAggregateVM configurationAggregateVM, int maxNumberInAggregate){
        JPanel aggregate = newAggregatePanel();
        for(ConfigurationElementVM configurationElementVM: configurationAggregateVM){
            aggregate.add(
                    newFrontConfigElement(configurationElementVM),
                    newPanelElementConstrainst(configurationElementVM)
            );
        }
        add(aggregate, newAggregateConstraints(configurationAggregateVM.get(0), gridx, gridy, maxNumberInAggregate));
    }

    private void initConfigurationDisplay(FrontConfigurationVM frontConfigurationVM){
        this.frontConfigurationVM = frontConfigurationVM;
        removeAll();
        getParent().repaint();

        setPreferredSize(new Dimension(
                (int)(frontConfigurationVM.getFurnitureWidth() / frontConfigurationVM.getMaxWidth() * getParent().getWidth()),
                (int)(frontConfigurationVM.getFurnitureHeight() / frontConfigurationVM.getMaxHeight() * getParent().getHeight())
        ));

        idToComponentBinding = new HashMap<>();
    }

    private FrontConfigViewElem newFrontConfigElement(ConfigurationElementVM configurationElementVM){
        FrontConfigViewElem configElement = new FrontConfigViewElem(
                configurationElementVM.getImagePath(),
                configurationElementVM.getModelElementKey().toString(),
                frontConfigurationVM.getFurnitureName()
        );
        configElement.setCanBlockWidth(configurationElementVM.getCanBlockWidth());
        configElement.setCanBlockHeight(configurationElementVM.getCanBlockHeight());
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
    public void displayFrontConfigElemDialog(String furnitureName, String elementId, String initialType, int initialWidth, int initialHeight, int maxWidth, int maxHeight, String initialElemName, boolean widthBlocked, boolean heightBlocked, boolean growthRingVertically, int shelvesNumber){
        frontConfigElemType.setSelectedItem(initialType);
        frontConfigElemWidth.setModel(new SpinnerNumberModel(initialWidth, 0, maxWidth, 1));
        frontConfigElemHeight.setModel(new SpinnerNumberModel(initialHeight,0,maxHeight,1));
        frontConfigElemName.setText(initialElemName);
        widthBlocker.setSelected(widthBlocked);
        widthBlocker.setEnabled(idToComponentBinding.get(elementId).getCanBlockWidth());
        heightBlocker.setSelected(heightBlocked);
        heightBlocker.setEnabled(idToComponentBinding.get(elementId).getCanBlockHeight());
        growthRingOrientationDisplayer.setSelected(growthRingVertically);
        shelvesNumberDisplayer.setValue(shelvesNumber);
        frontConfigElementDialogPanel.setVisible(true);
        int optionClicked = showFrontConfigElemModal();
        frontConfigElementDialogPanel.setVisible(false);
        if(optionClicked == JOptionPane.OK_OPTION){
            setupTipFor(elementId);
            modifyFrontConfigElement(furnitureName, elementId);
        }
    }

    private int showFrontConfigElemModal(){
        return JOptionPane.showConfirmDialog(
                null,
                frontConfigElementDialogPanel,
                Config.FRONT_CONFIG_ELEM_DIALOG_TITLE_PL,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
    }

    private void setupTipFor(String elementId){
        //noinspection ConstantConditions
        idToComponentBinding.get(elementId).setToolTipText(
                String.format(
                        Config.FRONT_CONFIG_ELEMENT_TIP_FORMAT,
                        frontConfigElemName.getText(),
                        frontConfigElemType.getSelectedItem().toString(),
                        Integer.parseInt(frontConfigElemWidth.getValue().toString()),
                        Integer.parseInt(frontConfigElemHeight.getValue().toString()),
                        widthBlocker.isSelected() ? Config.YES_PL : Config.NO_PL,
                        heightBlocker.isSelected() ? Config.YES_PL : Config.NO_PL,
                        growthRingOrientationDisplayer.isSelected() ? Config.YES_PL : Config.NO_PL,
                        Integer.parseInt(shelvesNumberDisplayer.getValue().toString())
                )
        );

    }

    private void modifyFrontConfigElement(String furnitureName, String elementId){
        //noinspection ConstantConditions
        presenter.onModifyFrontConfigElement(
                furnitureName,
                elementId,
                frontConfigElemType.getSelectedItem().toString(),
                Integer.parseInt(frontConfigElemWidth.getValue().toString()),
                Integer.parseInt(frontConfigElemHeight.getValue().toString()),
                frontConfigElemName.getText(),
                widthBlocker.isSelected(),
                heightBlocker.isSelected(),
                growthRingOrientationDisplayer.isSelected(),
                Integer.parseInt(shelvesNumberDisplayer.getValue().toString())
        );

    }

    public void setFrontConfigElementDialogPanel(JPanel frontConfigElementDialogPanel) {
        this.frontConfigElementDialogPanel = frontConfigElementDialogPanel;
    }

    public void setFrontConfigElemWidth(JSpinner frontConfigElemWidth) {
        this.frontConfigElemWidth = frontConfigElemWidth;
        this.frontConfigElemWidth.setModel(new SpinnerNumberModel(0,0,0,1));
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

    public void setShelvesNumberDisplayer(JSpinner shelvesNumberDisplayer) {
        this.shelvesNumberDisplayer = shelvesNumberDisplayer;
        this.shelvesNumberDisplayer.setModel(new SpinnerNumberModel(0,0,5,1));
    }

    public void setGrowthRingOrientationDisplayer(JCheckBox growthRingOrientationDisplayer) {
        this.growthRingOrientationDisplayer = growthRingOrientationDisplayer;
    }

    public void setWidthBlocker(JCheckBox widthBlocker) {
        this.widthBlocker = widthBlocker;
    }

    public void setHeightBlocker(JCheckBox heightBlocker) {
        this.heightBlocker = heightBlocker;
    }
}