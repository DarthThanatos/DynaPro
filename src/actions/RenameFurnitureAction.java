package actions;

import display.ProjectTree;
import main.DynProMain;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RenameFurnitureAction extends AbstractAction {

    private DynProMain dynProMain;
    private ProjectTree projectTree;

    public RenameFurnitureAction(DynProMain dynProMain){
        this.dynProMain = dynProMain;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dynProMain.getPresenter().onRenameFurniture(projectTree.getLastSelectedPathComponent().toString());

    }

    public void setProjectTree(ProjectTree projectTree) {
        this.projectTree = projectTree;
    }
}