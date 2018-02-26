package display._3d;

import com.jogamp.opengl.GL2;
import config.Config;
import javafx.geometry.Point3D;
import model.DoubleDoor;

class DoubleDoorDrawer extends ShelfDrawer{

    private final int leftDoorTexture;
    private final int rightDoorTexture;
    private DoubleDoor element;
    private boolean isLastToTheRight;
    private final boolean showFronts;

    DoubleDoorDrawer(DoubleDoor element, boolean backInserted, boolean isLastToTheLeft, boolean isLastToTheRight, int leftDoorTexture, int rightDoorTexture, boolean showFronts) {
        super(element, backInserted, isLastToTheLeft);
        this.element = element;
        this.isLastToTheRight = isLastToTheRight;
        this.leftDoorTexture = leftDoorTexture;
        this.rightDoorTexture = rightDoorTexture;
        this.showFronts = showFronts;
    }

    void drawDoubleDrawer(GL2 gl, Point3D currentPointWithOffsets, Point3D dimens) {
        if(showFronts) {
            drawSingleDoor(gl,
                    currentPointWithOffsets,
                    new Point3D(element.getSingleDoorWidth(), dimens.getY(), dimens.getZ()),
                    leftDoorTexture
            );
            drawSingleDoor(gl,
                    new Point3D(currentPointWithOffsets.getX() + element.getSingleDoorWidth() + Config.BETWEEN_ELEMENTS_VERTICAL_GAP / 2, currentPointWithOffsets.getY(), currentPointWithOffsets.getZ()),
                    new Point3D(element.getSingleDoorWidth(), dimens.getY(), dimens.getZ()),
                    rightDoorTexture
            );
        }
        for(int i = 0; i < element.getShelvesNumber(); i++){
            drawShelf(gl, currentPointWithOffsets, dimens, i);
        }
    }

    private void drawSingleDoor(GL2 gl, Point3D start, Point3D dimens, int texture){

        float frontColor = 0 / 255f;
        float frontStartX, frontStartY, frontStartZ;
        float frontWidth, frontHeight, frontDepth;

        frontStartX = (float) start.getX();
        frontStartY = (float) start.getY();
        frontStartZ = (float) start.getZ();

        frontWidth = element.getSingleDoorWidth();
        frontHeight = element.getHeight();
        frontDepth = Config.SLAB_THICKNESS;

        drawCuboid(gl,
                new Point3D(frontStartX,frontStartY,frontStartZ),
                new Point3D(frontWidth,frontHeight,frontDepth),
                frontColor,
                true
        );

        drawTexturedFront(gl, start, dimens, texture);
    }

}
