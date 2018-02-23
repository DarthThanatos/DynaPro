package display._3d;

import com.jogamp.opengl.GL2;
import javafx.geometry.Point3D;
import model.LeftDoor;

class LeftDoorDrawer extends DoorDrawer {

    private int leftDoorTexture;

    LeftDoorDrawer(LeftDoor leftDoor, int leftDoorTexture, boolean backInserted, boolean isLastToTheLeft, boolean isLastToTheRight) {
        super(leftDoor, backInserted, isLastToTheLeft, isLastToTheRight);
        this.leftDoorTexture = leftDoorTexture;

    }

    @Override
    void drawDoor(GL2 gl, Point3D start, Point3D dimens) {
        super.drawDoor(gl, start, dimens);
        drawTexturedFront(gl, start, dimens, leftDoorTexture);
    }

}
