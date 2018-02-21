package display._3d;

import com.jogamp.opengl.GL2;
import config.Config;
import javafx.geometry.Point3D;

class DrawerDrawer extends CuboidDrawer{

    private boolean backInserted, lastToTheLeft, isLastToTheRight;
    private float slabMeshThickness = Config.SLAB_WIDTH / Config.MESH_UNIT;
    private int aroundGap = 3, topGap = 10;

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

    private void drawLeftWall(GL2 gl, Point3D start, Point3D dimens){

        float leftWallColor = 120 / 255f;
        float leftWallStartX, leftWallStartY, leftWallStartZ;
        float leftWallWidth, leftWallHeight, leftWallDepth;

        leftWallStartX = (float) (start.getX() + slabMeshThickness + aroundGap) + (Config.BETWEEN_ELEMENTS_VERTICAL_GAP / (lastToTheLeft ? 1 : 2)) ;
        leftWallStartY = (float) (start.getY() - topGap);

        int t_track_depth = (int) (dimens.getZ() - (backInserted ? slabMeshThickness : 0) - aroundGap);
        leftWallStartZ = (float) (t_track_depth % 50) + 10 + (backInserted ? slabMeshThickness : 0);

        leftWallWidth = (float) slabMeshThickness;
        leftWallHeight = (float) (dimens.getZ() - topGap - slabMeshThickness);
        leftWallDepth = (float) (t_track_depth - (t_track_depth % 50)) - 10;

        drawCuboid(gl,
                new Point3D(leftWallStartX,leftWallStartY,leftWallStartZ),
                new Point3D(leftWallWidth,leftWallHeight,leftWallDepth),
                leftWallColor
        );
    }

    private void drawRightWall(GL2 gl, Point3D start, Point3D dimens){
        float rightWallColor = 120 / 255f;
        float rightWallStartX, rightWallStartY, rightWallStartZ;
        float rightWallWidth, rightWallHeight, rightWallDepth;

        rightWallStartX = (float) (start.getX() + dimens.getX() + 2 * Config.BETWEEN_ELEMENTS_VERTICAL_GAP - slabMeshThickness - aroundGap);
        rightWallStartY = (float) (start.getY() - topGap);

        int t_track_depth = (int) (dimens.getZ() - (backInserted ? slabMeshThickness : 0) - aroundGap);
        rightWallStartZ = (float) (t_track_depth % 50) + 10 + (backInserted ? slabMeshThickness : 0);

        rightWallWidth = (float) slabMeshThickness;
        rightWallHeight = (float) (dimens.getZ() - topGap - slabMeshThickness);
        rightWallDepth = (float) (t_track_depth - (t_track_depth % 50)) - 10;

        drawCuboid(gl,
                new Point3D(rightWallStartX,rightWallStartY,rightWallStartZ),
                new Point3D(rightWallWidth,rightWallHeight,rightWallDepth),
                rightWallColor
        );

    }

    private void drawBottom(GL2 gl, Point3D start, Point3D dimens){

    }

    private void drawBack(GL2 gl, Point3D start, Point3D dimens){

    }

    private void drawFront(GL2 gl, Point3D start, Point3D dimens){

    }
}
