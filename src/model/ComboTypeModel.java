package model;

import config.Config;

import javax.swing.*;

public class ComboTypeModel extends DefaultComboBoxModel {
    public ComboTypeModel(){
        super(new Object[]{Config.UPPER_MODULE, Config.BOTTOM_MODULE});
    }
}
