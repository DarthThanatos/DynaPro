package display._3d;

import com.jogamp.opengl.GL2;
import javafx.geometry.Point3D;
import model.Door;
import model.RightDoor;

class RightDoorDrawer extends DoorDrawer {

    private int rightDoorTexture;

    RightDoorDrawer(RightDoor rightDoor, int rightDoorTexture, boolean backInserted, boolean isLastToTheLeft, boolean isLastToTheRight) {
        super(rightDoor, backInserted, isLastToTheLeft, isLastToTheRight);
        this.rightDoorTexture = rightDoorTexture;
    }

    @Override
    void drawDoor(GL2 gl, Point3D start, Point3D dimens) {
        super.drawDoor(gl, start, dimens);
        drawTexturedFront(gl, start, dimens, rightDoorTexture);
    }
}
