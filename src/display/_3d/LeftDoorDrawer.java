package display._3d;

import com.jogamp.opengl.GL2;
import javafx.geometry.Point3D;
import model.LeftDoor;

class LeftDoorDrawer extends DoorDrawer {

    private final boolean showFronts;
    private int leftDoorTexture;

    LeftDoorDrawer(LeftDoor leftDoor, int leftDoorTexture, boolean backInserted, boolean isLastToTheLeft, boolean showFronts) {
        super(leftDoor, backInserted, isLastToTheLeft, showFronts);
        this.leftDoorTexture = leftDoorTexture;
        this.showFronts = showFronts;
    }

    @Override
    void drawDoor(GL2 gl, Point3D start, Point3D dimens) {
        super.drawDoor(gl, start, dimens);
        if(showFronts) drawTexturedFront(gl, start, dimens, leftDoorTexture);
    }

}
