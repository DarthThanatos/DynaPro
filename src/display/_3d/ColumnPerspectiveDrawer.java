package display._3d;

import com.jogamp.opengl.GL2;
import config.Config;
import javafx.geometry.Point3D;

class ColumnPerspectiveDrawer extends AggregatePerspectiveDrawer{

    @Override
    void drawSeparator(GL2 gl, Point3D start, Point3D dimens, boolean lastToTheLeft, boolean lastToTheRight, boolean lastToTheTop, boolean lastToTheBottom, boolean backInserted) {
        //start x,y,z - coords of the front plane
        //dimens - y - height of separator, z - depth of furniture, x - slabMeshThickness
        float separatorColor = 50 / 255f;
        float separatorStartX, separatorStartY, separatorStartZ;
        float separatorWidth, separatorHeight, separatorDepth;

        separatorStartX = getRightSideX(start, dimens, lastToTheRight) - slabMeshThickness;
        separatorStartY = getTopSlabY(start, lastToTheTop) - slabMeshThickness ;
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
