package display._3d;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import javafx.geometry.Point3D;

class MeshDrawer {


    void drawMesh(GL2 gl, Point3D startPoint, Point3D dimens) {
        float z = (float) startPoint.getZ();
        float x = (float) startPoint.getX();
        float y = (float) startPoint.getY();

        for (int i = (int) x; i < x + dimens.getX(); i++) {
            for (int j = (int) y; j < y + dimens.getY(); j++) {
                gl.glBegin(GL.GL_LINES);
                gl.glColor3f(1.0f, 0, 0);
                gl.glVertex3f(i, j, z);
                gl.glVertex3f(i + 1, j, z);

                gl.glVertex3f(i + 1, j, z);
                gl.glVertex3f(i + 1, j + 1, z);

                gl.glVertex3f(i + 1, j + 1, z);
                gl.glVertex3f(i, j + 1, z);

                gl.glVertex3f(i, j + 1, z);
                gl.glVertex3f(i, j, z);
                gl.glEnd();

            }
        }

        for (int k = 0; k < 2; k++) { // two meshes at opposite xs
            x = -x;
            for (int i = (int) y; i < y + dimens.getY(); i++) {
                for (int j = (int) z; j < z + dimens.getZ(); j++) {
                    gl.glBegin(GL.GL_LINES);
                    gl.glColor3f(1.0f, 0, 0);
                    gl.glVertex3f(x, i, j);
                    gl.glVertex3f(x, i + 1, j);

                    gl.glVertex3f(x, i + 1, j);
                    gl.glVertex3f(x, i + 1, j + 1);

                    gl.glVertex3f(x, i + 1, j + 1);
                    gl.glVertex3f(x, i, j + 1);

                    gl.glVertex3f(x, i, j + 1);
                    gl.glVertex3f(x, i, j);
                    gl.glEnd();
                }
            }
        }
        for (int i = (int) x; i < x + dimens.getX(); i++) {
            for (int j = (int) z; j < z + dimens.getZ(); j++) {
                gl.glBegin(GL.GL_LINES);
                gl.glColor3f(1.0f, 0, 0);
                gl.glVertex3f(i, y, j);
                gl.glVertex3f(i + 1, y, j);

                gl.glVertex3f(i + 1, y, j);
                gl.glVertex3f(i + 1, y, j + 1);

                gl.glVertex3f(i + 1, y, j + 1);
                gl.glVertex3f(i, y, j + 1);

                gl.glVertex3f(i, y, j + 1);
                gl.glVertex3f(i, y, j);
                gl.glEnd();

            }
        }
    }

}
