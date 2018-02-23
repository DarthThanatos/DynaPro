package display._3d;

import com.jogamp.opengl.GL2;
import config.Config;
import javafx.geometry.Point3D;
import model.Furniture;

class ModuleSkeletonDrawer extends CuboidDrawer {

    private Furniture furniture;

    ModuleSkeletonDrawer(Furniture furniture){
        this.furniture = furniture;
    }

    void drawModuleSkeleton(GL2 gl, Point3D furnitureStart, Point3D furnitureDimens){
        drawRoof(gl, furnitureStart, furnitureDimens);
        drawLeftWall(gl, furnitureStart, furnitureDimens);
        drawRightWall(gl, furnitureStart, furnitureDimens);
        drawBottom(gl, furnitureStart, furnitureDimens);
        drawBack(gl, furnitureStart, furnitureDimens);
    }

    private void drawRoof(GL2 gl, Point3D furnitureStart, Point3D furnitureDimens){
        float roofTopColor = 180 / 255f;
        float roofStartX, roofStartY, roofStartZ;
        float roofWidth, roofHeight, roofDepth;

        roofStartX = furniture.getRoofInserted() ? (float) (furnitureStart.getX() + Config.SLAB_THICKNESS) : (float) furnitureStart.getX();
        roofStartY = (float) furnitureStart.getY();
        roofStartZ = furniture.getRoofInserted() ? ((float)  furnitureStart.getZ()) : (float) furnitureStart.getZ();

        roofWidth = furniture.getRoofInserted() ? (float) (furnitureDimens.getX() - 2 * Config.SLAB_THICKNESS) : (float) furnitureDimens.getX();
        roofHeight = Config.SLAB_THICKNESS;
        roofDepth =
                furniture.getRoofInserted()
                        ? ((float) furnitureDimens.getZ())
                        : (float) furnitureDimens.getZ();

        drawCuboid(gl,
                new Point3D(roofStartX,roofStartY,roofStartZ),
                new Point3D(roofWidth,roofHeight,roofDepth),
                roofTopColor
        );
    }

    private void drawLeftWall(GL2 gl, Point3D furnitureStart, Point3D furnitureDimens){
        float leftWallColor = 220 / 255f;
        float leftWallStartX, leftWallStartY, leftWallStartZ;
        float leftWallWidth, leftWallHeight, leftWallDepth;

        leftWallStartX = (float) furnitureStart.getX() ;
        leftWallStartY = furniture.getRoofInserted() ? (float) furnitureStart.getY() : (float) (furnitureStart.getY() - Config.SLAB_THICKNESS);
        leftWallStartZ = (float) furnitureStart.getZ();

        leftWallWidth =  Config.SLAB_THICKNESS;
        leftWallHeight = (float) (furnitureDimens.getY() - (furniture.getRoofInserted() ? 0 : Config.SLAB_THICKNESS) - (furniture.getHasPedestal() ? 0 : (furniture.getRoofInserted() ? 0 : Config.SLAB_THICKNESS)));
        leftWallDepth = (float) furnitureDimens.getZ();

        drawCuboid(gl,
                new Point3D(leftWallStartX,leftWallStartY,leftWallStartZ),
                new Point3D(leftWallWidth,leftWallHeight,leftWallDepth),
                leftWallColor
        );

    }

    private void drawRightWall(GL2 gl, Point3D furnitureStart, Point3D furnitureDimens){
        float rightWallColor = 200 / 255f;
        float rightWallStartX, rightWallStartY, rightWallStartZ;
        float rightWallWidth, rightWallHeight, rightWallDepth;

        rightWallStartX = (float) (furnitureStart.getX() + furnitureDimens.getX() - Config.SLAB_THICKNESS);
        rightWallStartY = furniture.getRoofInserted() ? (float) furnitureStart.getY() : (float) (furnitureStart.getY() - Config.SLAB_THICKNESS);
        rightWallStartZ = (float) furnitureStart.getZ();

        rightWallWidth =  Config.SLAB_THICKNESS;
        rightWallHeight = (float) (furnitureDimens.getY() - (furniture.getRoofInserted() ? 0 : Config.SLAB_THICKNESS) - (furniture.getHasPedestal() ? 0 : (furniture.getRoofInserted() ? 0 : Config.SLAB_THICKNESS)));
        rightWallDepth = (float) furnitureDimens.getZ();

        drawCuboid(gl,
                new Point3D(rightWallStartX,rightWallStartY,rightWallStartZ),
                new Point3D(rightWallWidth,rightWallHeight,rightWallDepth),
                rightWallColor
        );

    }

    private void drawBottom(GL2 gl, Point3D furnitureStart, Point3D furnitureDimens){
        float bottomColor = 180 / 255f;
        float bottomStartX, bottomStartY, bottomStartZ;
        float bottomWidth, bottomHeight, bottomDepth;

        bottomStartX =
                furniture.getHasPedestal()
                        ? (float) (furnitureStart.getX() + Config.SLAB_THICKNESS)
                        : (float) (furniture.getRoofInserted() ? furnitureStart.getX() + Config.SLAB_THICKNESS : furnitureStart.getX());

        bottomStartY =
                furniture.getHasPedestal()
                ? (float) (furnitureStart.getY() - furnitureDimens.getY() + furniture.getPedestalHeight() + Config.SLAB_THICKNESS)
                : (float) (furnitureStart.getY() - furnitureDimens.getY()  + Config.SLAB_THICKNESS);

        bottomStartZ = (float) furnitureStart.getZ();

        bottomWidth =
                furniture.getHasPedestal()
                        ? (float) (furnitureDimens.getX() - 2 * Config.SLAB_THICKNESS)
                        : (float) (furniture.getRoofInserted() ? furnitureDimens.getX() - 2 * Config.SLAB_THICKNESS : furnitureDimens.getX());

        bottomHeight = Config.SLAB_THICKNESS;
        bottomDepth =  (float) furnitureDimens.getZ();

        drawCuboid(gl,
                new Point3D(bottomStartX,bottomStartY,bottomStartZ),
                new Point3D(bottomWidth,bottomHeight,bottomDepth),
                bottomColor
        );

        if(furniture.getHasPedestal()) drawPedestal(gl, furnitureStart, furnitureDimens);
    }


    private void drawPedestal(GL2 gl, Point3D furnitureStart, Point3D furnitureDimens){
        float pedestalColor = 120 / 255f;
        float pedestalStartX, pedestalStartY, pedestalStartZ;
        float pedestalWidth, pedestalHeight, pedestalDepth;

        pedestalStartX = (float) (furnitureStart.getX() + Config.SLAB_THICKNESS);
        pedestalStartY = (float) (furnitureStart.getY() - furnitureDimens.getY() + furniture.getPedestalHeight());
        pedestalStartZ = (float) (furnitureStart.getZ() + furnitureDimens.getZ() - 2 * Config.SLAB_THICKNESS);

        pedestalWidth = (float) (furnitureDimens.getX() - 2 * Config.SLAB_THICKNESS);
        pedestalHeight = furniture.getPedestalHeight();
        pedestalDepth = Config.SLAB_THICKNESS;

        drawCuboid(gl,
                new Point3D(pedestalStartX,pedestalStartY,pedestalStartZ),
                new Point3D(pedestalWidth,pedestalHeight,pedestalDepth),
                pedestalColor
        );
    }


    private void drawBack(GL2 gl, Point3D furnitureStart, Point3D furnitureDimens){
        float backColor = 60 / 255f;
        float backStartX, backStartY, backStartZ;
        float backWidth, backHeight, backDepth;

        backStartX = furniture.getBackInserted() ? (float) (furnitureStart.getX() + Config.SLAB_THICKNESS) : (float) furnitureStart.getX();
        backStartY = furniture.getBackInserted()  ? (float) (furnitureStart.getY() - Config.SLAB_THICKNESS) : (float) furnitureStart.getY();
        backStartZ = furniture.getBackInserted()  ? (float) furnitureStart.getZ() : (float) furnitureStart.getZ() - Config.HDF_BACK_THICKNESS;

        backWidth = furniture.getBackInserted()  ? (float) (furnitureDimens.getX() - 2 * Config.SLAB_THICKNESS) : (float) furnitureDimens.getX();
        backHeight =
                furniture.getBackInserted()
                        ? ((float) (furnitureDimens.getY() - 2 * Config.SLAB_THICKNESS - (furniture.getHasPedestal() ? furniture.getPedestalHeight() : 0) ))
                        : (float) furnitureDimens.getY() - (furniture.getHasPedestal() ? furniture.getPedestalHeight() : 0);
        backDepth = furniture.getBackInserted()  ? Config.SLAB_THICKNESS : Config.HDF_BACK_THICKNESS;

        drawCuboid(gl,
                new Point3D(backStartX,backStartY,backStartZ),
                new Point3D(backWidth,backHeight,backDepth),
                backColor
        );

    }

}
