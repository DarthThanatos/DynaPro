package actions;

import main.DynProMain;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class NewFurnitureAction extends AbstractAction {
    private DynProMain dynProMain;

    public NewFurnitureAction(DynProMain dynProMain){
        this.dynProMain = dynProMain;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dynProMain.getPresenter().onNewFurnitureAdd();

    }
}