package display._3d;

import model.Door;
import model.RightDoor;

class RightDoorDrawer extends DoorDrawer {

    RightDoorDrawer(RightDoor rightDoor, boolean backInserted, boolean isLastToTheLeft, boolean isLastToTheRight) {
        super(rightDoor, backInserted, isLastToTheLeft, isLastToTheRight);
    }
}
