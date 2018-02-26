package display._3d;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES3;
import config.Config;
import javafx.geometry.Point3D;

import static com.jogamp.opengl.GL.*;

class CuboidDrawer {


    void drawCuboid(GL2 gl, Point3D startPoint, Point3D dimens, float color, boolean transparentFront){
        float mox = (float) startPoint.getX() / Config.MESH_UNIT;
        float moy = (float) (startPoint.getY() - dimens.getY()) / Config.MESH_UNIT;
        float moz = (float) startPoint.getZ()/Config.MESH_UNIT;
        float ox = (float) (startPoint.getX() + dimens.getX()) / Config.MESH_UNIT;
        float oy = (float) startPoint.getY() / Config.MESH_UNIT;
        float oz = (float) (startPoint.getZ() + dimens.getZ()) / Config.MESH_UNIT;

        gl.glBegin(GL2ES3.GL_QUADS);
        gl.glColor4f(color, color, color, 0.5f);

        gl.glVertex3f(ox, oy, moz); // Top Right Of The Quad (Top)
        gl.glVertex3f(mox, oy, moz); // Top Left Of The Quad (Top)
        gl.glVertex3f(mox, oy, oz); // Bottom Left Of The Quad (Top)
        gl.glVertex3f(ox, oy, oz); // Bottom Right Of The Quad (Top)
        gl.glEnd();

        gl.glBegin(GL2ES3.GL_QUADS);
        gl.glColor4f(color, color, color, 0.5f);

        gl.glVertex3f(ox, moy, oz); // Top Right Of The Quad
        gl.glVertex3f(mox, moy, oz); // Top Left Of The Quad
        gl.glVertex3f(mox, moy, moz); // Bottom Left Of The Quad
        gl.glVertex3f(ox, moy, moz); // Bottom Right Of The Quad
        gl.glEnd();

//        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_COLOR);
        gl.glBegin(GL2ES3.GL_QUADS);
        gl.glColor4f(color, color, color, 0.5f);

        gl.glVertex3f(ox, oy, oz); // Top Right Of The Quad (Front)
        gl.glVertex3f(mox, oy, oz); // Top Left Of The Quad (Front)
        gl.glVertex3f(mox, moy, oz); // Bottom Left Of The Quad
        gl.glVertex3f(ox, moy, oz); // Bottom Right Of The Quad
        gl.glEnd();
        gl.glDisable(GL_BLEND);

        gl.glBegin(GL2ES3.GL_QUADS);
        gl.glColor4f(color, color, color, 0.5f);

        gl.glVertex3f(ox, moy, moz); // Bottom Left Of The Quad
        gl.glVertex3f(mox, moy, moz); // Bottom Right Of The Quad
        gl.glVertex3f(mox, oy, moz); // Top Right Of The Quad (Back)
        gl.glVertex3f(ox, oy, moz); // Top Left Of The Quad (Back)
        gl.glEnd();

        gl.glBegin(GL2ES3.GL_QUADS);
        gl.glColor4f(color, color, color, 0.5f);

        gl.glVertex3f(mox, oy, oz); // Top Right Of The Quad (Left)
        gl.glVertex3f(mox, oy, moz); // Top Left Of The Quad (Left)
        gl.glVertex3f(mox, moy, moz); // Bottom Left Of The Quad
        gl.glVertex3f(mox, moy, oz); // Bottom Right Of The Quad
        gl.glEnd();

        gl.glBegin(GL2ES3.GL_QUADS);
        gl.glColor4f(color, color, color, 0.5f);

        gl.glVertex3f(ox, oy, moz); // Top Right Of The Quad (Right)
        gl.glVertex3f(ox, oy, oz); // Top Left Of The Quad
        gl.glVertex3f(ox, moy, oz); // Bottom Left Of The Quad
        gl.glVertex3f(ox, moy, moz); // Bottom Right Of The Quad
        gl.glEnd();
    }

    void drawTexturedFront(GL2 gl, Point3D startPoint, Point3D dimens, int texture){
        float mox = (float) startPoint.getX() / Config.MESH_UNIT;
        float moy = (float) (startPoint.getY() - dimens.getY()) / Config.MESH_UNIT;
        float moz = (float) startPoint.getZ()/Config.MESH_UNIT;
        float ox = (float) (startPoint.getX() + dimens.getX()) / Config.MESH_UNIT;
        float oy = (float) startPoint.getY() / Config.MESH_UNIT;
        float oz = (float) (startPoint.getZ() + Config.SLAB_THICKNESS) / Config.MESH_UNIT;

        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, texture);

        gl.glBegin(GL2ES3.GL_QUADS);
        gl.glColor4f(1,1,1,0.5f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(ox, oy, oz); // Top Right Of The Quad (Front)
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(mox, oy, oz); // Top Left Of The Quad (Front)
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(mox, moy, oz); // Bottom Left Of The Quad
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(ox, moy, oz); // Bottom Right Of The Quad
        gl.glEnd();

        gl.glDisable(GL_BLEND);
        gl.glDisable(GL2.GL_TEXTURE_2D);
    }
}
