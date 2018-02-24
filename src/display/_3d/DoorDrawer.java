package display._3d;

import com.jogamp.opengl.GL2;
import config.Config;
import javafx.geometry.Point3D;
import model.SingleDoor;

import static util.SlabSidePositionUtil.getLeftSideX;
import static util.SlabSidePositionUtil.getRightSideX;

abstract class DoorDrawer extends CuboidDrawer {

    private final boolean backInserted;
    private final SingleDoor door;
    private boolean isLastToTheLeft, isLastToTheRight;

    DoorDrawer(SingleDoor door, boolean backInserted, boolean isLastToTheLeft, boolean isLastToTheRight){
        this.isLastToTheLeft = isLastToTheLeft;
        this.isLastToTheRight = isLastToTheRight;
        this.backInserted = backInserted;
        this.door = door;
    }

    void drawDoor(GL2 gl, Point3D start, Point3D dimens){

        float frontColor = 0 / 255f;
        float frontStartX, frontStartY, frontStartZ;
        float frontWidth, frontHeight, frontDepth;

        frontStartX = (float) start.getX();
        frontStartY = (float) start.getY();
        frontStartZ = (float) start.getZ();

        frontWidth = door.getDoorSlabSecondDimension();
        frontHeight = door.getDoorSlabFirstDimension();
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

    private void drawShelf(GL2 gl, Point3D start, Point3D dimens, int index){
        float shelfColor = 0 / 255f;
        float shelfStartX, shelfStartY, shelfStartZ;
        float shelfWidth, shelfHeight, shelfDepth;


        shelfStartX = getLeftSideX(start, isLastToTheLeft) + Config.SLAB_THICKNESS;
        shelfStartY = (float) (start.getY() - (dimens.getY() * ((float)(index) / (door.getShelvesNumber() + 1)) + (1.0f / (door.getShelvesNumber() + 1) * dimens.getY()))  );
        shelfStartZ = (float) (start.getZ() - dimens.getZ() + (backInserted ? Config.SLAB_THICKNESS : 0));

        shelfWidth = door.getShelfSlabFirstDimension();
        shelfHeight = Config.SLAB_THICKNESS;
        shelfDepth = door.getShelfSlabSecondDimension();

        drawCuboid(gl,
                new Point3D(shelfStartX,shelfStartY,shelfStartZ),
                new Point3D(shelfWidth,shelfHeight,shelfDepth),
                shelfColor,
                false
        );

    }

}
