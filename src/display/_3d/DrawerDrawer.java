package display._3d;

import com.jogamp.opengl.GL2;
import config.Config;
import javafx.geometry.Point3D;
import model.Drawer;
import model.FrontConfiguration;
import model.Furniture;

import static util.SlabSidePositionUtil.getLeftSideX;
import static util.SlabSidePositionUtil.getRightSideX;

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


    private void drawLeftWall(GL2 gl, Point3D start, Point3D dimens){

        float leftWallColor = 200 / 255f;
        float leftWallStartX, leftWallStartY, leftWallStartZ;
        float leftWallWidth, leftWallHeight, leftWallDepth;

        FrontConfiguration configuration = furniture.getFrontConfiguration();
        leftWallStartX =  drawer.getLeftWallX(start);
        leftWallStartY = (float) (start.getY() - topGap);
        leftWallStartZ = getStartZ(start, dimens);

        leftWallWidth = Config.SLAB_THICKNESS;
        leftWallHeight = drawer.getLeftWallFirstDimension();
        leftWallDepth = drawer.getLeftWallSecondDimension();

        drawCuboid(gl,
                new Point3D(leftWallStartX,leftWallStartY,leftWallStartZ),
                new Point3D(leftWallWidth,leftWallHeight,leftWallDepth),
                leftWallColor,
                false
        );
    }


    private float getStartZ(Point3D start, Point3D dimens){
        return (float) (start.getZ() - drawer.getTTrackEndDepth());

    }

    private void drawRightWall(GL2 gl, Point3D start, Point3D dimens){

        float rightWallColor = 200 / 255f;
        float rightWallStartX, rightWallStartY, rightWallStartZ;
        float rightWallWidth, rightWallHeight, rightWallDepth;

        FrontConfiguration configuration = furniture.getFrontConfiguration();
        rightWallStartX = drawer.getRightWallX(start, dimens);
        rightWallStartY = (float) (start.getY() - topGap);
        rightWallStartZ = getStartZ(start, dimens);

        rightWallWidth =  Config.SLAB_THICKNESS;
        rightWallHeight = drawer.getRightWallFirstDimension();
        rightWallDepth =  drawer.getRightWallSecondDimension();

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
        bottomStartX = drawer.getLeftWallX(start) + Config.SLAB_THICKNESS;
        bottomStartY = (float) (start.getY() - dimens.getY() + Config.SLAB_THICKNESS + 12 + 2*Config.SLAB_THICKNESS) + (lastToTheBottom ? Config.SLAB_THICKNESS: 0);
        bottomStartZ = getStartZ(start, dimens);

        bottomWidth = drawer.getBottomSlabSecondDimension();
        bottomHeight = Config.SLAB_THICKNESS;
        bottomDepth = drawer.getBottomSlabFirstDimenstion();

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
        backStartX =  drawer.getLeftWallX(start) + Config.SLAB_THICKNESS;
        backStartY = (float) (start.getY() - topGap);
        backStartZ = getStartZ(start, dimens);

        backWidth = drawer.getBackSlabSecondDimension();
        backHeight = drawer.getBackSlabFirstDimension();
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

        frontWidth = drawer.getFrontSlabSecondDimension();
        frontHeight = drawer.getFrontSlabFirstDimension();
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

        shelfWidth = drawer.getShelfSlabFirstDimension();
        shelfHeight = Config.SLAB_THICKNESS;
        shelfDepth = drawer.getShelfSlabSecondDimension();

        drawCuboid(gl,
                new Point3D(shelfStartX,shelfStartY,shelfStartZ),
                new Point3D(shelfWidth,shelfHeight,shelfDepth),
                shelfColor,
                false
        );

    }
}
