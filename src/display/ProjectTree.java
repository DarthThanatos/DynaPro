package display;


import javax.swing.*;
import java.awt.*;

public class ProjectTree extends JTree {

    private JPopupMenu projectPopup, furniturePopup;


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
        assert nodeBounds != null;
        return new Point(nodeBounds.x + nodeBounds.width ,nodeBounds.y+nodeBounds.height);
    }

}
