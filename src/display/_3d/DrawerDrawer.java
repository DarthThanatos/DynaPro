package display._3d;

import com.jogamp.opengl.GL2;
import config.Config;
import javafx.geometry.Point3D;

class DrawerDrawer extends CuboidDrawer{

    private boolean backInserted, lastToTheLeft, isLastToTheRight;
    private float slabMeshThickness = Config.SLAB_WIDTH / Config.MESH_UNIT;
    private float aroundMeshGap = 3/Config.MESH_UNIT, topMeshGap = 10/ Config.MESH_UNIT;
    private boolean lastToTheBottom;

    DrawerDrawer(boolean backInserted, boolean lastToTheLeft, boolean lastToTheRight, boolean lastToTheBottom){
        this.backInserted = backInserted;
        this.lastToTheLeft = lastToTheLeft;
        this.isLastToTheRight = lastToTheRight;
        this.lastToTheBottom = lastToTheBottom;
    }

    void drawDrawer(GL2 gl, Point3D start, Point3D dimens){
        //start: x,y,z: front upperLeftBack coordinates
        //dimens: x and y are width and height coming from the front, z is the depth of the furniture
        drawFront(gl, start, dimens);
        drawBack(gl, start, dimens);
        drawLeftWall(gl, start, dimens);
        drawRightWall(gl, start, dimens);
        drawBottom(gl, start, dimens);
    }

    private float getLeftWallX(Point3D start){
        float distToLeftSide = (lastToTheLeft ? Config.BETWEEN_ELEMENTS_VERTICAL_GAP / Config.MESH_UNIT : 10/ Config.MESH_UNIT);
        float leftSideX =  (float) (start.getX() - distToLeftSide);
        return leftSideX + slabMeshThickness + 6/Config.MESH_UNIT;
    }

    private void drawLeftWall(GL2 gl, Point3D start, Point3D dimens){

        float leftWallColor = 200 / 255f;
        float leftWallStartX, leftWallStartY, leftWallStartZ;
        float leftWallWidth, leftWallHeight, leftWallDepth;

        leftWallStartX =  getLeftWallX(start);
        leftWallStartY = (float) (start.getY() - topMeshGap);
        leftWallStartZ = getStartZ(start, dimens);

        leftWallWidth = slabMeshThickness;
        leftWallHeight = (float) (dimens.getY() - topMeshGap - slabMeshThickness) - (lastToTheBottom ?  slabMeshThickness : 0);
        leftWallDepth = getTTrackEndDepth(dimens) / Config.MESH_UNIT;

        drawCuboid(gl,
                new Point3D(leftWallStartX,leftWallStartY,leftWallStartZ),
                new Point3D(leftWallWidth,leftWallHeight,leftWallDepth),
                leftWallColor
        );
    }

    private float getRightWallX(Point3D start, Point3D dimens){
        float distToRightSide = (float) (dimens.getX() + (isLastToTheRight ? Config.BETWEEN_ELEMENTS_VERTICAL_GAP / Config.MESH_UNIT : 10/ Config.MESH_UNIT));
        float rightSideX = (float) (start.getX() + distToRightSide);
        return  rightSideX - slabMeshThickness - 6/Config.MESH_UNIT - slabMeshThickness;
    }


    private int getTTrackRawDepth(Point3D dimens){
        return (int) ((dimens.getZ() - (backInserted ? slabMeshThickness : 0) - aroundMeshGap) * Config.MESH_UNIT);
    }

    private float getTTrackEndDepth(Point3D dimens){
        int t_track_depth = getTTrackRawDepth(dimens);
        return  t_track_depth - (t_track_depth % 50) - 10;
    }

    private float getStartZ(Point3D start, Point3D dimens){
        return (float) (start.getZ() - getTTrackEndDepth(dimens)/ Config.MESH_UNIT);

    }

    private void drawRightWall(GL2 gl, Point3D start, Point3D dimens){

        float rightWallColor = 200 / 255f;
        float rightWallStartX, rightWallStartY, rightWallStartZ;
        float rightWallWidth, rightWallHeight, rightWallDepth;

        rightWallStartX = getRightWallX(start, dimens);
        rightWallStartY = (float) (start.getY() - topMeshGap);
        rightWallStartZ = getStartZ(start, dimens);

        rightWallWidth =  slabMeshThickness;
        rightWallHeight = (float) (dimens.getY() - topMeshGap - slabMeshThickness) - (lastToTheBottom ? slabMeshThickness : 0);
        System.out.println("Wall Height " + rightWallHeight * Config.MESH_UNIT);
        rightWallDepth =  getTTrackEndDepth(dimens) / Config.MESH_UNIT;

        drawCuboid(gl,
                new Point3D(rightWallStartX,rightWallStartY,rightWallStartZ),
                new Point3D(rightWallWidth,rightWallHeight,rightWallDepth),
                rightWallColor
        );

    }

    private void drawBottom(GL2 gl, Point3D start, Point3D dimens){
        float bottomColor = 120 / 255f;
        float bottomStartX, bottomStartY, bottomStartZ;
        float bottomWidth, bottomHeight, bottomDepth;

        bottomStartX = getLeftWallX(start) + slabMeshThickness;
        bottomStartY = (float) (start.getY() - dimens.getY() + slabMeshThickness + 12/Config.MESH_UNIT + 2*slabMeshThickness) + (lastToTheBottom ? slabMeshThickness: 0);
        bottomStartZ = getStartZ(start, dimens);

        bottomWidth = getRightWallX(start, dimens) - getLeftWallX(start) - slabMeshThickness;
        bottomHeight = slabMeshThickness;
        bottomDepth = getTTrackEndDepth(dimens)/ Config.MESH_UNIT ;

        System.out.println("Width: " + (bottomWidth * Config.MESH_UNIT) + ", height: " + (bottomHeight * Config.MESH_UNIT) + ", depth: " + (bottomDepth * Config.MESH_UNIT) + ", ttrackrest: " + getTTrackEndDepth(dimens) + ", Z: " + bottomStartZ * Config.MESH_UNIT + ", " + (start.getZ()) * Config.MESH_UNIT +  ", " + (start.getZ() - dimens.getZ()) * Config.MESH_UNIT);
        drawCuboid(gl,
                new Point3D(bottomStartX,bottomStartY,bottomStartZ),
                new Point3D(bottomWidth,bottomHeight,bottomDepth),
                bottomColor
        );

    }

    private void drawBack(GL2 gl, Point3D start, Point3D dimens){
        float backColor = 180 / 255f;
        float backStartX, backStartY, backStartZ;
        float backWidth, backHeight, backDepth;

        backStartX =  getLeftWallX(start) + slabMeshThickness;
        backStartY = (float) (start.getY() - topMeshGap);
        backStartZ = getStartZ(start, dimens);

        backWidth = getRightWallX(start, dimens) - getLeftWallX(start) - slabMeshThickness;
        backHeight = (float) (dimens.getY() - slabMeshThickness - 10/Config.MESH_UNIT - 2 *slabMeshThickness - 12/Config.MESH_UNIT) - (lastToTheBottom ? slabMeshThickness : 0);
        backDepth = slabMeshThickness;

        drawCuboid(gl,
                new Point3D(backStartX,backStartY,backStartZ),
                new Point3D(backWidth,backHeight,backDepth),
                backColor
        );

    }

    private void drawFront(GL2 gl, Point3D start, Point3D dimens){
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

    }
}
