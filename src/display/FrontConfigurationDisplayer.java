package display;

import contract.DynProContract;
import model.ConfigurationColumnVM;
import model.ConfigurationElementVM;
import model.FrontConfigurationVM;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class FrontConfigurationDisplayer extends JPanel implements MouseListener {

    private DynProContract.Presenter presenter;

    public FrontConfigurationDisplayer(){
        addMouseListener(this);
    }

    public void display(FrontConfigurationVM frontConfigurationVM, boolean columnOriented) {
        removeAll();
        int gridx = 0, gridy = 0;
        for(ConfigurationColumnVM configurationColumnVM: frontConfigurationVM.getColumns()){
            if(columnOriented)gridy = 0; else gridx=0;
            for(ConfigurationElementVM configurationElementVM: configurationColumnVM){
                add(
                        new ImageButton(configurationElementVM.getImagePath(), this),
                        newConstraints(gridx, gridy, frontConfigurationVM.getColumns(), columnOriented, configurationColumnVM.size() == 1)
                );
                if(columnOriented) gridy++; else gridx++;
            }
            if(columnOriented) gridx++; else gridy++;
        }
        revalidate();
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

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if(e.isPopupTrigger()){
            System.out.println("Here should be a popup");
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
