package display._3d;

import com.jogamp.opengl.GL2;
import config.Config;
import javafx.geometry.Point3D;

class DoorDrawer extends CuboidDrawer {

    private final boolean backInserted;
    private final boolean isLastToTheTop;
    private int shelvesAmount;
    private boolean isLastToBottom;
    private boolean isLastToTheLeft, isLastToTheRight;
    private float slabMeshThickness = Config.SLAB_WIDTH / Config.MESH_UNIT;

    DoorDrawer(boolean backInserted, boolean isLastToTheLeft, boolean isLastToTheRight, boolean isLastToTheTop, boolean isLastToBottom,int shelvesAmount){
        this.shelvesAmount = shelvesAmount;
        this.isLastToBottom = isLastToBottom;
        this.isLastToTheLeft = isLastToTheLeft;
        this.isLastToTheRight = isLastToTheRight;
        this.backInserted = backInserted;
        this.isLastToTheTop = isLastToTheTop;
    }

    private float getLeftSideX(Point3D start){
        float distToLeftSide = (isLastToTheLeft ? Config.BETWEEN_ELEMENTS_VERTICAL_GAP / Config.MESH_UNIT : 10/ Config.MESH_UNIT);
        return  (float) (start.getX() - distToLeftSide);
    }

    private float getRightSideX(Point3D start, Point3D dimens){
        float distToRightSide = (float) (dimens.getX() + (isLastToTheRight ? Config.BETWEEN_ELEMENTS_VERTICAL_GAP / Config.MESH_UNIT : 10/ Config.MESH_UNIT));
        return (float) (start.getX() + distToRightSide);

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

        if(!isLastToTheRight){
            drawSeparator(gl, start, dimens);
        }
    }

    private void drawShelf(GL2 gl, Point3D start, Point3D dimens, int index){
        float shelfColor = 120 / 255f;
        float shelfStartX, shelfStartY, shelfStartZ;
        float shelfWidth, shelfHeight, shelfDepth;


        shelfStartX = getLeftSideX(start) + slabMeshThickness;
        shelfStartY = (float) (start.getY() - (dimens.getY() * ((float)(index) / (shelvesAmount + 1)) + (1.0f / (shelvesAmount + 1) * dimens.getY()))  );
        shelfStartZ = (float) (start.getZ() - dimens.getZ() + (backInserted ? slabMeshThickness : 0));

        shelfWidth = getRightSideX(start, dimens) - getLeftSideX(start) - 2*slabMeshThickness;
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


        shelfStartX = getLeftSideX(start) + slabMeshThickness;
        shelfStartY = (float) (start.getY() - dimens.getY() + 8/Config.MESH_UNIT);
        shelfStartZ = (float) (start.getZ() - dimens.getZ() + (backInserted ? slabMeshThickness : 0));

        shelfWidth = getRightSideX(start, dimens) - getLeftSideX(start) - 2*slabMeshThickness;
        shelfHeight = slabMeshThickness;
        shelfDepth = (float) (dimens.getZ() - (backInserted ? slabMeshThickness : 0));

        drawCuboid(gl,
                new Point3D(shelfStartX,shelfStartY,shelfStartZ),
                new Point3D(shelfWidth,shelfHeight,shelfDepth),
                shelfColor
        );

    }

    private float getTopSlabY(Point3D start){
        float distToTop =  isLastToTheTop ? 2 / Config.MESH_UNIT : 8/Config.MESH_UNIT;
        return (float) (start.getY() + distToTop);
    }

    private void drawSeparator(GL2 gl, Point3D start, Point3D dimens){
        //start x,y,z - coords of the front plane
        //dimens - y - height of separator, z - depth of furniture, x - slabMeshThickness
        float separatorColor = 50 / 255f;
        float separatorStartX, separatorStartY, separatorStartZ;
        float separatorWidth, separatorHeight, separatorDepth;

        separatorStartX = getRightSideX(start, dimens) - slabMeshThickness;
        separatorStartY = getTopSlabY(start) - slabMeshThickness ;
        separatorStartZ = (float) (start.getZ() - dimens.getZ() + (backInserted ? slabMeshThickness : 0));

        separatorWidth = slabMeshThickness;
        separatorHeight = (float) dimens.getY();
        separatorDepth = (float) (dimens.getZ() - (backInserted ? slabMeshThickness : 0));

        drawCuboid(
                gl,
                new Point3D(separatorStartX, separatorStartY, separatorStartZ),
                new Point3D(separatorWidth, separatorHeight, separatorDepth),
                separatorColor
        );
    }
}
