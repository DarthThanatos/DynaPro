package display._3d;

import com.jogamp.opengl.GL2;
import config.Config;
import javafx.geometry.Point3D;

class DrawerDrawer extends CuboidDrawer{

    private boolean backInserted, lastToTheLeft, isLastToTheRight;
    private float slabMeshThickness = Config.SLAB_WIDTH / Config.MESH_UNIT;
    private float aroundMeshGap = 3/Config.MESH_UNIT, topMeshGap = 10/ Config.MESH_UNIT;

    DrawerDrawer(boolean backInserted, boolean lastToTheLeft, boolean lastToTheRight){
        this.backInserted = backInserted;
        this.lastToTheLeft = lastToTheLeft;
        this.isLastToTheRight = lastToTheRight;
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

        float leftWallColor = 120 / 255f;
        float leftWallStartX, leftWallStartY, leftWallStartZ;
        float leftWallWidth, leftWallHeight, leftWallDepth;

        leftWallStartX =  getLeftWallX(start);
        leftWallStartY = (float) (start.getY() - topMeshGap);

        int t_track_depth = (int) (dimens.getZ() - (backInserted ? slabMeshThickness : 0) - aroundMeshGap);
        leftWallStartZ = (float) (t_track_depth % 50) + 10/Config.MESH_UNIT + (backInserted ? slabMeshThickness : 0);

        leftWallWidth = slabMeshThickness;
        leftWallHeight = (float) (dimens.getZ() - topMeshGap - slabMeshThickness);
        leftWallDepth = (float) (t_track_depth - (t_track_depth % 50)) - 10/Config.MESH_UNIT;

        drawCuboid(gl,
                new Point3D(leftWallStartX,leftWallStartY,leftWallStartZ),
                new Point3D(leftWallWidth,leftWallHeight,leftWallDepth),
                leftWallColor
        );
    }

    private float getRightWallX(Point3D start, Point3D dimens){
        float distToRightSide = (float) (dimens.getX() + (isLastToTheRight ? Config.BETWEEN_ELEMENTS_VERTICAL_GAP / Config.MESH_UNIT : 10/ Config.MESH_UNIT));
        float rightSideX = (float) (start.getX() - distToRightSide);
        return (float) rightSideX - slabMeshThickness - 6/Config.MESH_UNIT - slabMeshThickness;
    }

    private void drawRightWall(GL2 gl, Point3D start, Point3D dimens){

        float rightWallColor = 120 / 255f;
        float rightWallStartX, rightWallStartY, rightWallStartZ;
        float rightWallWidth, rightWallHeight, rightWallDepth;

        rightWallStartX = getRightWallX(start, dimens);
        rightWallStartY = (float) (start.getY() - topMeshGap);

        int t_track_depth = (int) (dimens.getZ() - (backInserted ? slabMeshThickness : 0) - aroundMeshGap);
        rightWallStartZ = (float) (t_track_depth % 50) + 10/Config.MESH_UNIT + (backInserted ? slabMeshThickness : 0);

        rightWallWidth =  slabMeshThickness;
        rightWallHeight = (float) (dimens.getZ() - topMeshGap - slabMeshThickness);
        rightWallDepth = (float) (t_track_depth - (t_track_depth % 50)) - 10/Config.MESH_UNIT;

        drawCuboid(gl,
                new Point3D(rightWallStartX,rightWallStartY,rightWallStartZ),
                new Point3D(rightWallWidth,rightWallHeight,rightWallDepth),
                rightWallColor
        );

    }

    private void drawBottom(GL2 gl, Point3D start, Point3D dimens){

    }

    private void drawBack(GL2 gl, Point3D start, Point3D dimens){
        float backColor = 120 / 255f;
        float backStartX, backStartY, backStartZ;
        float backWidth, backHeight, backDepth;

        backStartX =  getLeftWallX(start) + slabMeshThickness;
        backStartY = (float) (start.getY() - topMeshGap);
        backStartZ = (float) start.getZ();

        backWidth = (float) dimens.getX();
        backHeight = (float) dimens.getY();
        backDepth = slabMeshThickness;

        drawCuboid(gl,
                new Point3D(backStartX,backStartY,backStartZ),
                new Point3D(backWidth,backHeight,backDepth),
                backColor
        );

    }

    private void drawFront(GL2 gl, Point3D start, Point3D dimens){
        float frontColor = 120 / 255f;
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
