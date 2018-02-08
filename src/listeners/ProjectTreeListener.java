package listeners;

import display.ProjectTree;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ProjectTreeListener implements MouseListener{

    private ProjectTree projectTree;

    public void attachProjectTree(ProjectTree projectTree){
        this.projectTree = projectTree;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object lastSelectedPathComponent  = projectTree.getLastSelectedPathComponent();
        if(lastSelectedPathComponent == null) return;
        projectTree.getDynProMain().getPresenter().onMetadataSetSelected(lastSelectedPathComponent.toString());
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if(e.isPopupTrigger()){
            Object lastSelectedPathComponent =
                    projectTree
                        .getLastSelectedPathComponent();

            if(lastSelectedPathComponent != null) {
                projectTree
                        .getDynProMain()
                        .getPresenter()
                        .onProjectTreePopupSelection(
                                lastSelectedPathComponent.toString()
                        );
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
