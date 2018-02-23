package display._3d;

import com.jogamp.opengl.GL2;
import config.Config;
import javafx.geometry.Point3D;
import model.Door;

abstract class DoorDrawer extends CuboidDrawer {

    private final boolean backInserted;
    private final Door door;
    private boolean isLastToTheLeft, isLastToTheRight;

    DoorDrawer(Door door, boolean backInserted, boolean isLastToTheLeft, boolean isLastToTheRight){
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

        frontWidth = (float) dimens.getX();
        frontHeight = (float) dimens.getY();
        frontDepth = Config.SLAB_THICKNESS;

        drawCuboid(gl,
                new Point3D(frontStartX,frontStartY,frontStartZ),
                new Point3D(frontWidth,frontHeight,frontDepth),
                frontColor
        );

        for(int i = 0; i < door.getShelvesNumber(); i++){
            drawShelf(gl, start, dimens, i);
        }

//        if(!isLastToBottom){
//            drawShelfAtBottom(gl, start, dimens);
//        }

    }

    private void drawShelf(GL2 gl, Point3D start, Point3D dimens, int index){
        float shelfColor = 120 / 255f;
        float shelfStartX, shelfStartY, shelfStartZ;
        float shelfWidth, shelfHeight, shelfDepth;


        shelfStartX = getLeftSideX(start, isLastToTheLeft) + Config.SLAB_THICKNESS;
        shelfStartY = (float) (start.getY() - (dimens.getY() * ((float)(index) / (door.getShelvesNumber() + 1)) + (1.0f / (door.getShelvesNumber() + 1) * dimens.getY()))  );
        shelfStartZ = (float) (start.getZ() - dimens.getZ() + (backInserted ? Config.SLAB_THICKNESS : 0));

        shelfWidth = getRightSideX(start, dimens, isLastToTheRight) - getLeftSideX(start, isLastToTheLeft) - 2*Config.SLAB_THICKNESS;
        shelfHeight = Config.SLAB_THICKNESS;
        shelfDepth = (float) (dimens.getZ() - (backInserted ? Config.SLAB_THICKNESS : 0));

        drawCuboid(gl,
                new Point3D(shelfStartX,shelfStartY,shelfStartZ),
                new Point3D(shelfWidth,shelfHeight,shelfDepth),
                shelfColor
        );

    }

//    private void drawShelfAtBottom(GL2 gl, Point3D start, Point3D dimens){
//        float shelfColor = 120 / 255f;
//        float shelfStartX, shelfStartY, shelfStartZ;
//        float shelfWidth, shelfHeight, shelfDepth;
//
//
//        shelfStartX = getLeftSideX(start, isLastToTheLeft) + Config.SLAB_THICKNESS;
//        shelfStartY = (float) (start.getY() - dimens.getY() + 8);
//        shelfStartZ = (float) (start.getZ() - dimens.getZ() + (backInserted ? Config.SLAB_THICKNESS : 0));
//
//        shelfWidth = getRightSideX(start, dimens, isLastToTheRight) - getLeftSideX(start, isLastToTheLeft) - 2*Config.SLAB_THICKNESS;
//        shelfHeight = Config.SLAB_THICKNESS;
//        shelfDepth = (float) (dimens.getZ() - (backInserted ? Config.SLAB_THICKNESS : 0));
//
//        drawCuboid(gl,
//                new Point3D(shelfStartX,shelfStartY,shelfStartZ),
//                new Point3D(shelfWidth,shelfHeight,shelfDepth),
//                shelfColor
//        );
//
//    }


}
