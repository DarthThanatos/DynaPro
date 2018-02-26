package display._3d;

import com.jogamp.opengl.GL2;
import javafx.geometry.Point3D;
import model.DoubleDoor;
import model.ShelfSlabFurnitureTree;

class DoubleDoorDrawer extends ShelfDrawer{

    private final int leftDoorTexture;
    private final int rightDoorTextue;
    private DoubleDoor element;
    private boolean isLastToTheRight;

    DoubleDoorDrawer(DoubleDoor element, boolean backInserted, boolean isLastToTheLeft, boolean isLastToTheRight, int leftDoorTexture, int rightDoorTexture) {
        super(element, backInserted, isLastToTheLeft);
        this.element = element;
        this.isLastToTheRight = isLastToTheRight;
        this.leftDoorTexture = leftDoorTexture;
        this.rightDoorTextue = rightDoorTexture;
    }

    void drawDoubleDrawer(GL2 gl, Point3D currentPointWithOffsets, Point3D dimens) {


        for(int i = 0; i < element.getShelvesNumber(); i++){
            drawShelf(gl, currentPointWithOffsets, dimens, i);
        }
    }

    private void drawLeftDoor(){

    }

    private void drawRightDoor(){

    }
}
