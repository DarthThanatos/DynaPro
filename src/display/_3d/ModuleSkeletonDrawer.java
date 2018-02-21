package display._3d;

import com.jogamp.opengl.GL2;
import config.Config;
import javafx.geometry.Point3D;

class ModuleSkeletonDrawer extends CuboidDrawer {

    private boolean roofInserted, hasPedestal, backInserted;
    private float slabThickness = Config.SLAB_WIDTH / Config.MESH_UNIT;
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

        roofStartX = roofInserted ? (float) (furnitureStart.getX() + slabThickness) : (float) furnitureStart.getX();
        roofStartY = (float) furnitureStart.getY();
        roofStartZ = roofInserted ? ((float)  furnitureStart.getZ()) : (float) furnitureStart.getZ();

        roofWidth = roofInserted ? (float) (furnitureDimens.getX() - 2 * slabThickness) : (float) furnitureDimens.getX();
        roofHeight = slabThickness;
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
        leftWallStartY = roofInserted ? (float) furnitureStart.getY() : (float) (furnitureStart.getY() - slabThickness);
        leftWallStartZ = (float) furnitureStart.getZ();

        leftWallWidth = slabThickness;
        leftWallHeight = (float) (furnitureDimens.getY() - (roofInserted ? 0 : slabThickness) - (hasPedestal ? 0 : (roofInserted ? 0 : slabThickness)));
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

        rightWallStartX = (float) (furnitureStart.getX() + furnitureDimens.getX() - slabThickness);
        rightWallStartY = roofInserted ? (float) furnitureStart.getY() : (float) (furnitureStart.getY() - slabThickness);
        rightWallStartZ = (float) furnitureStart.getZ();

        rightWallWidth = slabThickness;
        rightWallHeight = (float) (furnitureDimens.getY() - (roofInserted ? 0 : slabThickness) - (hasPedestal ? 0 : (roofInserted ? 0 : slabThickness)));
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
                        ? (float) (furnitureStart.getX() +  slabThickness)
                        : (float) (roofInserted ? furnitureStart.getX() +  slabThickness : furnitureStart.getX());

        bottomStartY =
                hasPedestal
                ? (float) (furnitureStart.getY() - furnitureDimens.getY() + pedestalMeshHeight + slabThickness )
                : (float) (furnitureStart.getY() - furnitureDimens.getY()  + slabThickness);

        bottomStartZ = (float) furnitureStart.getZ();

        bottomWidth =
                hasPedestal
                        ? (float) (furnitureDimens.getX() - 2 * slabThickness)
                        : (float) (roofInserted ? furnitureDimens.getX() - 2 * slabThickness : furnitureDimens.getX());

        bottomHeight = slabThickness;
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

        pedestalStartX = (float) (furnitureStart.getX() + slabThickness);
        pedestalStartY = (float) (furnitureStart.getY() - furnitureDimens.getY() + pedestalMeshHeight);
        pedestalStartZ = (float) (furnitureStart.getZ() + furnitureDimens.getZ() - 2 * slabThickness);

        pedestalWidth = (float) (furnitureDimens.getX() - 2 * slabThickness);
        pedestalHeight = pedestalMeshHeight;
        pedestalDepth = slabThickness;

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

        backStartX = backInserted ? (float) (furnitureStart.getX() + slabThickness) : (float) furnitureStart.getX();
        backStartY = backInserted ? (float) (furnitureStart.getY() - slabThickness) : (float) furnitureStart.getY();
        backStartZ = backInserted ? (float) furnitureStart.getZ() : (float) furnitureStart.getZ() - hdfThickness;

        backWidth = backInserted ? (float) (furnitureDimens.getX() - 2 * slabThickness) : (float) furnitureDimens.getX();
        backHeight =
                backInserted
                        ? ((float) (furnitureDimens.getY() - 2 * slabThickness - (hasPedestal ? pedestalMeshHeight : 0) ))
                        : (float) furnitureDimens.getY() - (hasPedestal ? pedestalMeshHeight : 0);
        backDepth = backInserted ? slabThickness : hdfThickness;

        drawCuboid(gl,
                new Point3D(backStartX,backStartY,backStartZ),
                new Point3D(backWidth,backHeight,backDepth),
                backColor
        );

    }

}
