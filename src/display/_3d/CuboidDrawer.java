package display._3d;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES3;
import javafx.geometry.Point3D;

class CuboidDrawer {

    void drawCuboid(GL2 gl, Point3D startPoint, Point3D dimens, float color){
        float mox = (float) startPoint.getX();
        float moy = (float) (startPoint.getY() - dimens.getY());
        float moz = (float) startPoint.getZ();
        float ox = (float) (startPoint.getX() + dimens.getX());
        float oy = (float) startPoint.getY();
        float oz = (float) (startPoint.getZ() + dimens.getZ());

        gl.glBegin(GL2ES3.GL_QUADS);
        gl.glColor3f(color, color, color);

        gl.glVertex3f(ox, oy, moz); // Top Right Of The Quad (Top)
        gl.glVertex3f(mox, oy, moz); // Top Left Of The Quad (Top)
        gl.glVertex3f(mox, oy, oz); // Bottom Left Of The Quad (Top)
        gl.glVertex3f(ox, oy, oz); // Bottom Right Of The Quad (Top)
        gl.glEnd();

        gl.glBegin(GL2ES3.GL_QUADS);
        gl.glColor3f(color, color, color);

        gl.glVertex3f(ox, moy, oz); // Top Right Of The Quad
        gl.glVertex3f(mox, moy, oz); // Top Left Of The Quad
        gl.glVertex3f(mox, moy, moz); // Bottom Left Of The Quad
        gl.glVertex3f(ox, moy, moz); // Bottom Right Of The Quad
        gl.glEnd();

        gl.glBegin(GL2ES3.GL_QUADS);
        gl.glColor3f(color, color, color);

        gl.glVertex3f(ox, oy, oz); // Top Right Of The Quad (Front)
        gl.glVertex3f(mox, oy, oz); // Top Left Of The Quad (Front)
        gl.glVertex3f(mox, moy, oz); // Bottom Left Of The Quad
        gl.glVertex3f(ox, moy, oz); // Bottom Right Of The Quad
        gl.glEnd();

        gl.glBegin(GL2ES3.GL_QUADS);
        gl.glColor3f(color, color, color);

        gl.glVertex3f(ox, moy, moz); // Bottom Left Of The Quad
        gl.glVertex3f(mox, moy, moz); // Bottom Right Of The Quad
        gl.glVertex3f(mox, oy, moz); // Top Right Of The Quad (Back)
        gl.glVertex3f(ox, oy, moz); // Top Left Of The Quad (Back)
        gl.glEnd();

        gl.glBegin(GL2ES3.GL_QUADS);
        gl.glColor3f(color, color, color);

        gl.glVertex3f(mox, oy, oz); // Top Right Of The Quad (Left)
        gl.glVertex3f(mox, oy, moz); // Top Left Of The Quad (Left)
        gl.glVertex3f(mox, moy, moz); // Bottom Left Of The Quad
        gl.glVertex3f(mox, moy, oz); // Bottom Right Of The Quad
        gl.glEnd();

        gl.glBegin(GL2ES3.GL_QUADS);
        gl.glColor3f(color, color, color); //blue color

        gl.glVertex3f(ox, oy, moz); // Top Right Of The Quad (Right)
        gl.glVertex3f(ox, oy, oz); // Top Left Of The Quad
        gl.glVertex3f(ox, moy, oz); // Bottom Left Of The Quad
        gl.glVertex3f(ox, moy, moz); // Bottom Right Of The Quad
        gl.glEnd();
    }
}
