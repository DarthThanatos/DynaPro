package display._3d;

import com.jogamp.opengl.GL2;
import config.Config;
import javafx.geometry.Point3D;

abstract class DoorDrawer extends CuboidDrawer {

    private final boolean backInserted;
    private int shelvesAmount;
    private boolean isLastToBottom;
    private boolean isLastToTheLeft, isLastToTheRight;
    private float slabMeshThickness = Config.SLAB_WIDTH / Config.MESH_UNIT;

    DoorDrawer(boolean backInserted, boolean isLastToTheLeft, boolean isLastToTheRight, boolean isLastToBottom, int shelvesAmount){
        this.shelvesAmount = shelvesAmount;
        this.isLastToBottom = isLastToBottom;
        this.isLastToTheLeft = isLastToTheLeft;
        this.isLastToTheRight = isLastToTheRight;
        this.backInserted = backInserted;
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
        frontDepth = slabMeshThickness;

        drawCuboid(gl,
                new Point3D(frontStartX,frontStartY,frontStartZ),
                new Point3D(frontWidth,frontHeight,frontDepth),
                frontColor
        );

        for(int i = 0; i < shelvesAmount; i++){
            drawShelf(gl, start, dimens, i);
        }

        if(!isLastToBottom){
            drawShelfAtBottom(gl, start, dimens);
        }

    }

    private void drawShelf(GL2 gl, Point3D start, Point3D dimens, int index){
        float shelfColor = 120 / 255f;
        float shelfStartX, shelfStartY, shelfStartZ;
        float shelfWidth, shelfHeight, shelfDepth;


        shelfStartX = getLeftSideX(start, isLastToTheLeft) + slabMeshThickness;
        shelfStartY = (float) (start.getY() - (dimens.getY() * ((float)(index) / (shelvesAmount + 1)) + (1.0f / (shelvesAmount + 1) * dimens.getY()))  );
        shelfStartZ = (float) (start.getZ() - dimens.getZ() + (backInserted ? slabMeshThickness : 0));

        shelfWidth = getRightSideX(start, dimens, isLastToTheRight) - getLeftSideX(start, isLastToTheLeft) - 2*slabMeshThickness;
        shelfHeight = slabMeshThickness;
        shelfDepth = (float) (dimens.getZ() - (backInserted ? slabMeshThickness : 0));

        drawCuboid(gl,
                new Point3D(shelfStartX,shelfStartY,shelfStartZ),
                new Point3D(shelfWidth,shelfHeight,shelfDepth),
                shelfColor
        );

    }

    private void drawShelfAtBottom(GL2 gl, Point3D start, Point3D dimens){
        float shelfColor = 120 / 255f;
        float shelfStartX, shelfStartY, shelfStartZ;
        float shelfWidth, shelfHeight, shelfDepth;


        shelfStartX = getLeftSideX(start, isLastToTheLeft) + slabMeshThickness;
        shelfStartY = (float) (start.getY() - dimens.getY() + 8/Config.MESH_UNIT);
        shelfStartZ = (float) (start.getZ() - dimens.getZ() + (backInserted ? slabMeshThickness : 0));

        shelfWidth = getRightSideX(start, dimens, isLastToTheRight) - getLeftSideX(start, isLastToTheLeft) - 2*slabMeshThickness;
        shelfHeight = slabMeshThickness;
        shelfDepth = (float) (dimens.getZ() - (backInserted ? slabMeshThickness : 0));

        drawCuboid(gl,
                new Point3D(shelfStartX,shelfStartY,shelfStartZ),
                new Point3D(shelfWidth,shelfHeight,shelfDepth),
                shelfColor
        );

    }


}
