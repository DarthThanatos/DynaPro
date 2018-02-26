package display._3d;

import com.jogamp.opengl.GL2;
import javafx.geometry.Point3D;
import model.RightDoor;

class RightDoorDrawer extends DoorDrawer {

    private int rightDoorTexture;
    private boolean showFronts;

    RightDoorDrawer(RightDoor rightDoor, int rightDoorTexture, boolean backInserted, boolean isLastToTheLeft, boolean showFronts) {
        super(rightDoor, backInserted, isLastToTheLeft, showFronts);
        this.rightDoorTexture = rightDoorTexture;
        this.showFronts = showFronts;
    }

    @Override
    void drawDoor(GL2 gl, Point3D start, Point3D dimens) {
        super.drawDoor(gl, start, dimens);
        if(showFronts) drawTexturedFront(gl, start, dimens, rightDoorTexture);
    }
}
