package display._3d;

import com.jogamp.opengl.GL2;
import javafx.geometry.Point3D;

class InsertedSectorDrawer extends CuboidDrawer {

    void drawInsertedSector(GL2 gl, Point3D start, Point3D dimens){
        float width = 0.1f;
        float color1 = 120 / 255f;
        drawCuboid(gl, new Point3D(start.getX(),start.getY(), start.getZ()),
                new Point3D(width, dimens.getY(), dimens.getZ()), color1);
        drawCuboid(gl, new Point3D(start.getX() + dimens.getX() - width,start.getY(),start.getZ()),
                new Point3D(width, dimens.getY(), dimens.getZ()), color1);
        float color2 = 180 / 255f;
        drawCuboid(gl, new Point3D(width, start.getY(),start.getZ()),
                new Point3D(dimens.getX() - 2*width, width, dimens.getZ()), color2);
        drawCuboid(gl, new Point3D(width, start.getY() - dimens.getY() + width,start.getZ()),
                new Point3D(dimens.getX() - 2*width, width, dimens.getZ()), color2);
    }
}
