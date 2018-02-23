package display._3d;

import model.Door;
import model.LeftDoor;

class LeftDoorDrawer extends DoorDrawer {

    LeftDoorDrawer(LeftDoor leftDoor, boolean backInserted, boolean isLastToTheLeft, boolean isLastToTheRight) {
        super(leftDoor, backInserted, isLastToTheLeft, isLastToTheRight);
    }
}
