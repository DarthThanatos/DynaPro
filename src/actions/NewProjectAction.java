package actions;

import main.DynProMain;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class NewProjectAction extends AbstractAction {

    private final DynProMain dynProMain;

    public NewProjectAction(DynProMain dynProMain){
        this.dynProMain = dynProMain;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dynProMain.getPresenter().onNewProject();
    }
}
