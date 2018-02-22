package display._3d;

import com.jogamp.opengl.GL2;
import config.Config;
import javafx.geometry.Point3D;

class RowPerspectiveDrawer extends AggregatePerspectiveDrawer {

    @Override
    void drawSeparator(GL2 gl, Point3D start, Point3D dimens, boolean lastToTheLeft, boolean lastToTheRight, boolean lastToTheTop, boolean lastToTheBottom, boolean backInserted) {

        float separatorColor = 120 / 255f;
        float separatorStartX, separatorStartY, separatorStartZ;
        float separatorWidth, separatorHeight, separatorDepth;


        separatorStartX = getLeftSideX(start, lastToTheLeft) + slabMeshThickness;
        separatorStartY = (float) (start.getY() - dimens.getY() + 8/ Config.MESH_UNIT);
        separatorStartZ = (float) (start.getZ() - dimens.getZ() + (backInserted ? slabMeshThickness : 0));

        separatorWidth = getRightSideX(start, dimens, lastToTheRight) - getLeftSideX(start, lastToTheLeft) - 2*slabMeshThickness;
        separatorHeight = slabMeshThickness;
        separatorDepth = (float) (dimens.getZ() - (backInserted ? slabMeshThickness : 0));

        drawCuboid(gl,
                new Point3D(separatorStartX,separatorStartY,separatorStartZ),
                new Point3D(separatorWidth,separatorHeight,separatorDepth),
                separatorColor
        );
    }
}
