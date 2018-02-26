package display._3d;

import com.jogamp.opengl.GL2;
import config.Config;
import javafx.geometry.Point3D;
import model.SingleDoor;

import static util.SlabSidePositionUtil.getLeftSideX;
import static util.SlabSidePositionUtil.getRightSideX;

abstract class DoorDrawer extends ShelfDrawer {

    private final SingleDoor door;

    DoorDrawer(SingleDoor door, boolean backInserted, boolean isLastToTheLeft){
        super(door, backInserted, isLastToTheLeft);
        this.door = door;
    }

    void drawDoor(GL2 gl, Point3D start, Point3D dimens){

        float frontColor = 0 / 255f;
        float frontStartX, frontStartY, frontStartZ;
        float frontWidth, frontHeight, frontDepth;

        frontStartX = (float) start.getX();
        frontStartY = (float) start.getY();
        frontStartZ = (float) start.getZ();

        frontWidth = door.getWidth();
        frontHeight = door.getHeight();
        frontDepth = Config.SLAB_THICKNESS;

        drawCuboid(gl,
                new Point3D(frontStartX,frontStartY,frontStartZ),
                new Point3D(frontWidth,frontHeight,frontDepth),
                frontColor,
                true
        );

        for(int i = 0; i < door.getShelvesNumber(); i++){
            drawShelf(gl, start, dimens, i);
        }


    }

}
