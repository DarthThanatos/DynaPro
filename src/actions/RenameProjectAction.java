package actions;

import main.DynProMain;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RenameProjectAction extends AbstractAction {
    private DynProMain dynProMain;

    public RenameProjectAction(DynProMain dynProMain){
        this.dynProMain = dynProMain;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dynProMain.getPresenter().onRenameProject();
    }
}