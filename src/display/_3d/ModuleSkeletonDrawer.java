package display._3d;

import com.jogamp.opengl.GL2;
import config.Config;
import javafx.geometry.Point3D;

class ModuleSkeletonDrawer extends CuboidDrawer {

    private boolean roofInserted, hasPedestal, backInserted;
    private float slabMeshThickness = Config.SLAB_WIDTH / Config.MESH_UNIT;
    private float pedestalMeshHeight;

    ModuleSkeletonDrawer(boolean roofInserted, boolean hasPedestal, boolean backInserted, float pedestalMeshHeight){
        this.roofInserted = roofInserted;
        this.hasPedestal = hasPedestal;
        this.backInserted = backInserted;
        this.pedestalMeshHeight = pedestalMeshHeight;
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

        roofStartX = roofInserted ? (float) (furnitureStart.getX() + slabMeshThickness) : (float) furnitureStart.getX();
        roofStartY = (float) furnitureStart.getY();
        roofStartZ = roofInserted ? ((float)  furnitureStart.getZ()) : (float) furnitureStart.getZ();

        roofWidth = roofInserted ? (float) (furnitureDimens.getX() - 2 * slabMeshThickness) : (float) furnitureDimens.getX();
        roofHeight = slabMeshThickness;
        roofDepth =
                roofInserted
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

        leftWallStartX = (float) furnitureStart.getX();
        leftWallStartY = roofInserted ? (float) furnitureStart.getY() : (float) (furnitureStart.getY() - slabMeshThickness);
        leftWallStartZ = (float) furnitureStart.getZ();

        leftWallWidth = slabMeshThickness;
        leftWallHeight = (float) (furnitureDimens.getY() - (roofInserted ? 0 : slabMeshThickness) - (hasPedestal ? 0 : (roofInserted ? 0 : slabMeshThickness)));
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

        rightWallStartX = (float) (furnitureStart.getX() + furnitureDimens.getX() - slabMeshThickness);
        rightWallStartY = roofInserted ? (float) furnitureStart.getY() : (float) (furnitureStart.getY() - slabMeshThickness);
        rightWallStartZ = (float) furnitureStart.getZ();

        rightWallWidth = slabMeshThickness;
        rightWallHeight = (float) (furnitureDimens.getY() - (roofInserted ? 0 : slabMeshThickness) - (hasPedestal ? 0 : (roofInserted ? 0 : slabMeshThickness)));
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
                hasPedestal
                        ? (float) (furnitureStart.getX() + slabMeshThickness)
                        : (float) (roofInserted ? furnitureStart.getX() + slabMeshThickness : furnitureStart.getX());

        bottomStartY =
                hasPedestal
                ? (float) (furnitureStart.getY() - furnitureDimens.getY() + pedestalMeshHeight + slabMeshThickness)
                : (float) (furnitureStart.getY() - furnitureDimens.getY()  + slabMeshThickness);

        bottomStartZ = (float) furnitureStart.getZ();

        bottomWidth =
                hasPedestal
                        ? (float) (furnitureDimens.getX() - 2 * slabMeshThickness)
                        : (float) (roofInserted ? furnitureDimens.getX() - 2 * slabMeshThickness : furnitureDimens.getX());

        bottomHeight = slabMeshThickness;
        bottomDepth =  (float) furnitureDimens.getZ();

        drawCuboid(gl,
                new Point3D(bottomStartX,bottomStartY,bottomStartZ),
                new Point3D(bottomWidth,bottomHeight,bottomDepth),
                bottomColor
        );

        if(hasPedestal) drawPedestal(gl, furnitureStart, furnitureDimens);
    }


    private void drawPedestal(GL2 gl, Point3D furnitureStart, Point3D furnitureDimens){
        float pedestalColor = 120 / 255f;
        float pedestalStartX, pedestalStartY, pedestalStartZ;
        float pedestalWidth, pedestalHeight, pedestalDepth;

        pedestalStartX = (float) (furnitureStart.getX() + slabMeshThickness);
        pedestalStartY = (float) (furnitureStart.getY() - furnitureDimens.getY() + pedestalMeshHeight);
        pedestalStartZ = (float) (furnitureStart.getZ() + furnitureDimens.getZ() - 2 * slabMeshThickness);

        pedestalWidth = (float) (furnitureDimens.getX() - 2 * slabMeshThickness);
        pedestalHeight = pedestalMeshHeight;
        pedestalDepth = slabMeshThickness;

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
        float hdfThickness = Config.HDF_BACK_THICKNESS / Config.MESH_UNIT;

        backStartX = backInserted ? (float) (furnitureStart.getX() + slabMeshThickness) : (float) furnitureStart.getX();
        backStartY = backInserted ? (float) (furnitureStart.getY() - slabMeshThickness) : (float) furnitureStart.getY();
        backStartZ = backInserted ? (float) furnitureStart.getZ() : (float) furnitureStart.getZ() - hdfThickness;

        backWidth = backInserted ? (float) (furnitureDimens.getX() - 2 * slabMeshThickness) : (float) furnitureDimens.getX();
        backHeight =
                backInserted
                        ? ((float) (furnitureDimens.getY() - 2 * slabMeshThickness - (hasPedestal ? pedestalMeshHeight : 0) ))
                        : (float) furnitureDimens.getY() - (hasPedestal ? pedestalMeshHeight : 0);
        backDepth = backInserted ? slabMeshThickness : hdfThickness;

        drawCuboid(gl,
                new Point3D(backStartX,backStartY,backStartZ),
                new Point3D(backWidth,backHeight,backDepth),
                backColor
        );

    }

}
