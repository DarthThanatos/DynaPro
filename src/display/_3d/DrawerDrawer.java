package display._3d;

import com.jogamp.opengl.GL2;
import config.Config;
import javafx.geometry.Point3D;
import model.Drawer;
import model.FrontConfiguration;
import model.Furniture;

class DrawerDrawer extends CuboidDrawer{

    private boolean backInserted, lastToTheLeft, isLastToTheRight;
    private float topGap = 10;
    private boolean lastToTheBottom;
    private int shelvesAtFrontBottomNumber;
    private Drawer drawer;
    private int drawerTexture;
    private Furniture furniture;

    DrawerDrawer(Drawer drawer, int drawerTexture, boolean backInserted, boolean lastToTheLeft, boolean lastToTheRight, boolean lastToTheBottom, int shelvesAtFrontBottomNumber, Furniture furniture){
        this.backInserted = backInserted;
        this.lastToTheLeft = lastToTheLeft;
        this.isLastToTheRight = lastToTheRight;
        this.lastToTheBottom = lastToTheBottom;
        this.shelvesAtFrontBottomNumber = shelvesAtFrontBottomNumber;
        this.drawer = drawer;
        this.drawerTexture = drawerTexture;
        this.furniture = furniture;
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
        float distToLeftSide = (lastToTheLeft ? Config.BETWEEN_ELEMENTS_VERTICAL_GAP  : 10);
        float leftSideX =  (float) (start.getX() - distToLeftSide);
        return leftSideX + Config.SLAB_THICKNESS + 6;
    }

    private void drawLeftWall(GL2 gl, Point3D start, Point3D dimens){

        float leftWallColor = 200 / 255f;
        float leftWallStartX, leftWallStartY, leftWallStartZ;
        float leftWallWidth, leftWallHeight, leftWallDepth;

        FrontConfiguration configuration = furniture.getFrontConfiguration();
        leftWallStartX =  getLeftWallX(start);
        leftWallStartY = (float) (start.getY() - topGap);
//        leftWallStartY = getTopSlabY(start, configuration.isElemWithIdLastToTheTop(drawer.getId())) - Config.SLAB_THICKNESS - topGap;
        leftWallStartZ = getStartZ(start, dimens);

        leftWallWidth = Config.SLAB_THICKNESS;
        leftWallHeight = (float) (dimens.getY() - topGap - Config.SLAB_THICKNESS) - (lastToTheBottom ?  Config.SLAB_THICKNESS : 0);
//        leftWallHeight = (float) (dimens.getY() - topGap - Config.SLAB_THICKNESS) - (lastToTheBottom ?  Config.SLAB_THICKNESS : 0)- (float) (getTopSlabY(start, configuration.isElemWithIdLastToTheTop(drawer.getId())) - start.getY());
        leftWallDepth = getTTrackEndDepth(dimens);

        drawCuboid(gl,
                new Point3D(leftWallStartX,leftWallStartY,leftWallStartZ),
                new Point3D(leftWallWidth,leftWallHeight,leftWallDepth),
                leftWallColor,
                false
        );
    }

    private float getRightWallX(Point3D start, Point3D dimens){
        float distToRightSide = (float) (dimens.getX() + (isLastToTheRight ? Config.BETWEEN_ELEMENTS_VERTICAL_GAP  : 10));
        float rightSideX = (float) (start.getX() + distToRightSide);
        return  rightSideX - Config.SLAB_THICKNESS - 6/Config.MESH_UNIT - Config.SLAB_THICKNESS;
    }


    private int getTTrackRawDepth(Point3D dimens){
        float aroundGap = 3;
        return (int) ((dimens.getZ() - (backInserted ? Config.SLAB_THICKNESS : 0) - aroundGap));
    }

    private float getTTrackEndDepth(Point3D dimens){
        int t_track_depth = getTTrackRawDepth(dimens);
        return  t_track_depth - (t_track_depth % 50) - 10;
    }

    private float getStartZ(Point3D start, Point3D dimens){
        return (float) (start.getZ() - getTTrackEndDepth(dimens));

    }

    private void drawRightWall(GL2 gl, Point3D start, Point3D dimens){

        float rightWallColor = 200 / 255f;
        float rightWallStartX, rightWallStartY, rightWallStartZ;
        float rightWallWidth, rightWallHeight, rightWallDepth;

        FrontConfiguration configuration = furniture.getFrontConfiguration();
        rightWallStartX = getRightWallX(start, dimens);
        rightWallStartY = (float) (start.getY() - topGap);
//        rightWallStartY = getTopSlabY(start, configuration.isElemWithIdLastToTheTop(drawer.getId())) - Config.SLAB_THICKNESS - topGap;
        rightWallStartZ = getStartZ(start, dimens);

        rightWallWidth =  Config.SLAB_THICKNESS;
        rightWallHeight = (float) (dimens.getY() - topGap - Config.SLAB_THICKNESS) - (lastToTheBottom ? Config.SLAB_THICKNESS : 0);
//        rightWallHeight = (float) (dimens.getY() - topGap - Config.SLAB_THICKNESS) - (lastToTheBottom ? Config.SLAB_THICKNESS : 0) - (float) (getTopSlabY(start, configuration.isElemWithIdLastToTheTop(drawer.getId())) - start.getY());
        rightWallDepth =  getTTrackEndDepth(dimens) ;

        drawCuboid(gl,
                new Point3D(rightWallStartX,rightWallStartY,rightWallStartZ),
                new Point3D(rightWallWidth,rightWallHeight,rightWallDepth),
                rightWallColor,
                false
        );

    }

    private void drawBottom(GL2 gl, Point3D start, Point3D dimens){
        float bottomColor = 120 / 255f;
        float bottomStartX, bottomStartY, bottomStartZ;
        float bottomWidth, bottomHeight, bottomDepth;

        FrontConfiguration configuration = furniture.getFrontConfiguration();
        bottomStartX = getLeftWallX(start) + Config.SLAB_THICKNESS;
        bottomStartY = (float) (start.getY() - dimens.getY() + Config.SLAB_THICKNESS + 12 + 2*Config.SLAB_THICKNESS) + (lastToTheBottom ? Config.SLAB_THICKNESS: 0);
//        bottomStartY = (float) (start.getY() - dimens.getY() + Config.SLAB_THICKNESS + 12 + 2*Config.SLAB_THICKNESS) + (lastToTheBottom ? Config.SLAB_THICKNESS: 0) + (float) (getTopSlabY(start, configuration.isElemWithIdLastToTheTop(drawer.getId())) - start.getY());
        bottomStartZ = getStartZ(start, dimens);

        bottomWidth = getRightWallX(start, dimens) - getLeftWallX(start) - Config.SLAB_THICKNESS;
        bottomHeight = Config.SLAB_THICKNESS;
        bottomDepth = getTTrackEndDepth(dimens);

        drawCuboid(gl,
                new Point3D(bottomStartX,bottomStartY,bottomStartZ),
                new Point3D(bottomWidth,bottomHeight,bottomDepth),
                bottomColor,
                false
        );

    }

    private void drawBack(GL2 gl, Point3D start, Point3D dimens){
        float backColor = 180 / 255f;
        float backStartX, backStartY, backStartZ;
        float backWidth, backHeight, backDepth;

        FrontConfiguration configuration = furniture.getFrontConfiguration();
        backStartX =  getLeftWallX(start) + Config.SLAB_THICKNESS;
        backStartY = (float) (start.getY() - topGap);
//        backStartY = getTopSlabY(start, configuration.isElemWithIdLastToTheTop(drawer.getId())) - Config.SLAB_THICKNESS - topGap;
        backStartZ = getStartZ(start, dimens);

        backWidth = getRightWallX(start, dimens) - getLeftWallX(start) - Config.SLAB_THICKNESS;
        backHeight = (float) (dimens.getY() - Config.SLAB_THICKNESS - 10 - 2 *Config.SLAB_THICKNESS - 12) - (lastToTheBottom ? Config.SLAB_THICKNESS : 0);
//        backHeight = (float) (dimens.getY() - Config.SLAB_THICKNESS - 10 - 2 *Config.SLAB_THICKNESS - 12) - (lastToTheBottom ? Config.SLAB_THICKNESS : 0) - (float) (getTopSlabY(start, configuration.isElemWithIdLastToTheTop(drawer.getId())) - start.getY());
        backDepth = Config.SLAB_THICKNESS;

        drawCuboid(gl,
                new Point3D(backStartX,backStartY,backStartZ),
                new Point3D(backWidth,backHeight,backDepth),
                backColor,
                false
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
        frontDepth = Config.SLAB_THICKNESS;

        drawCuboid(gl,
                new Point3D(frontStartX,frontStartY,frontStartZ),
                new Point3D(frontWidth,frontHeight,frontDepth),
                frontColor,
                true
        );

        drawTexturedFront(gl, start, dimens, drawerTexture);

        if(shelvesAtFrontBottomNumber > 0){
            drawShelfAtBottom(gl, start, dimens);
        }
    }


    private void drawShelfAtBottom(GL2 gl, Point3D start, Point3D dimens){
        float shelfColor = 120 / 255f;
        float shelfStartX, shelfStartY, shelfStartZ;
        float shelfWidth, shelfHeight, shelfDepth;


        shelfStartX = getLeftSideX(start, lastToTheLeft) + Config.SLAB_THICKNESS;
        shelfStartY = (float) (start.getY() - dimens.getY() + 8);
        shelfStartZ = (float) (start.getZ() - dimens.getZ() + (backInserted ? Config.SLAB_THICKNESS : 0));

        shelfWidth = getRightSideX(start, dimens, isLastToTheRight) - getLeftSideX(start, lastToTheLeft) - 2*Config.SLAB_THICKNESS;
        shelfHeight = Config.SLAB_THICKNESS;
        shelfDepth = (float) (dimens.getZ() - (backInserted ? Config.SLAB_THICKNESS : 0));

        drawCuboid(gl,
                new Point3D(shelfStartX,shelfStartY,shelfStartZ),
                new Point3D(shelfWidth,shelfHeight,shelfDepth),
                shelfColor,
                false
        );

    }
}
