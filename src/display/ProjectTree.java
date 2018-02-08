package display;

import listeners.ProjectTreeListener;
import main.DynProMain;

import javax.swing.*;
import java.awt.*;

public class ProjectTree extends JTree {

    private DynProMain dynProMain;
    private JPopupMenu projectPopup, furniturePopup;

    public ProjectTree(ProjectTreeListener projectTreeListener){
        addMouseListener(projectTreeListener);
        projectTreeListener.attachProjectTree(this);
    }

    public void attachDynProMain(DynProMain dynProMain){
        this.dynProMain = dynProMain;
    }

    public void setFurniturePopup(JPopupMenu furniturePopup) {
        this.furniturePopup = furniturePopup;
    }

    public void setProjectPopup(JPopupMenu projectPopup) {
        this.projectPopup = projectPopup;
    }

    public void displayProjectPopup(){
        displayPopup(projectPopup);
    }


    public void displayFurniturePopup() {
        displayPopup(furniturePopup);
    }

    private void displayPopup(JPopupMenu popupMenu){
        Point position = getPopupPosition();
        popupMenu.show(this, position.x, position.y);

    }

    private Point getPopupPosition(){
        Rectangle nodeBounds = getPathBounds(getSelectionPath());
        System.out.println(nodeBounds);
        assert nodeBounds != null;
        return new Point(nodeBounds.x + nodeBounds.width ,nodeBounds.y+nodeBounds.height);
    }

    public DynProMain getDynProMain() {
        return dynProMain;
    }

}
