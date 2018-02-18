package extra;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
//import Jama.Matrix;
import javax.swing.*;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.PMVMatrix;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import javafx.geometry.Point3D;

import java.awt.event.*;
import java.io.File;
import java.nio.FloatBuffer;
import java.util.*;

import static com.jogamp.opengl.GL.*;

public class Cube implements GLEventListener, MouseListener, MouseMotionListener, MouseWheelListener {

    //    public static DisplayMode dm, dm_old;
    private GLU glu = new GLU();
    private float rquad = 0.0f;
    private float rotationX, rotationY, buttonX, glButtonY, mathButtonY, translationZ;
    private int texture;



    private double[][] vToColumnWiseMd(double v[]){
        double[][] res = new double[4][4];
        for(int j = 0; j < 4; j ++){
            for (int i = 0; i < 4; i++){
                res[i][j] = v[i * 4 + j];
            }
        }
        return res;

    }

    private double[][] vToRowWiseMd(double v[]){
        double [][] res = new double[4][4];
        double[][]columnWise = vToColumnWiseMd(v);
        for(int i = 0; i < 4; i ++){
            for (int j = 0; j < 4; j++){
                res[i][j] = columnWise[j][i];
            }
        }
        return res;

    }

    private float[][] vToColumnWiseMf(float v[]){
        float[][] res = new float[4][4];
        for(int j = 0; j < 4; j ++){
            for (int i = 0; i < 4; i++){
                res[i][j] = v[i * 4 + j];
            }
        }
        return res;

    }

    private float[][] vToRowWiseMf(float v[]){
        float [][] res = new float[4][4];
        float[][]columnWise = vToColumnWiseMf(v);
        for(int i = 0; i < 4; i ++){
            for (int j = 0; j < 4; j++){
                res[i][j] = columnWise[j][i];
            }
        }
        return res;

    }

    private float[][] pmvBufferToColumnWiseArray(PMVMatrix pmvMatrix){
        return vToColumnWiseMf(pmvMatrix.glGetPMatrixf().array());
    }

    private float[][] pmvBufferToRowWiseArray(PMVMatrix pmvMatrix){
        return vToRowWiseMf(pmvMatrix.glGetPMatrixf().array());
    }

    private void printM4x4(float [][] m, String title){
        System.out.println(title);
        for(int i = 0; i < 4; i++){
            for (int j = 0 ; j < 4; j++){
                System.out.print(m[i][j] + " ");
            }
            System.out.println();
        }
    }

    private void utils(){
//        Matrix projMatInversed = new Matrix(vToRowWiseMd(projection)).inverse();
//        Matrix mVMatInversed = new Matrix(vToRowWiseMd(modelView)).inverse();
//        printM4x4(vToColumnWiseMf(projection), "column wise");
//        printM4x4(vToRowWiseMf(projection), "row wise");
        printM4x4(pmvBufferToColumnWiseArray(new PMVMatrix()), "column wise");
        printM4x4(pmvBufferToRowWiseArray(new PMVMatrix()), "row wise");

    }



    @Override
    public void mousePressed(MouseEvent e) {
        buttonX = e.getX(); glButtonY = e.getY();
        System.out.println("Pressed");
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        System.out.println("released");
        buttonX = e.getX(); glButtonY = e.getY();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(e.getX() != buttonX)
            rotationY += (e.getX() - buttonX);
        buttonX = e.getX();
        if(e.getY() != glButtonY)
            rotationX += (e.getY() - glButtonY);
        glButtonY = e.getY();
        System.out.println("dragged");
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        translationZ -= e.getPreciseWheelRotation();
    }


    private double[] recentWorldCoordinatesAtClickedPoint = new double[3], previousWorldCoordinatesAtClickedPoint = new double[3];

    @Override
    public void mouseClicked(MouseEvent e) {
        if(
                recentWorldCoordinatesAtClickedPoint[0] != previousWorldCoordinatesAtClickedPoint[0]
                        || recentWorldCoordinatesAtClickedPoint[1] != previousWorldCoordinatesAtClickedPoint[1]
                        || recentWorldCoordinatesAtClickedPoint[2] != previousWorldCoordinatesAtClickedPoint[2]
                ){
            System.out.println("Most recently clicked: 2D: " + buttonX + ", " + mathButtonY + "; 3D: " +
                    recentWorldCoordinatesAtClickedPoint[0] + ", "
                    + recentWorldCoordinatesAtClickedPoint[1]
                    + ", " + recentWorldCoordinatesAtClickedPoint[2]
            );
            previousWorldCoordinatesAtClickedPoint[0] = recentWorldCoordinatesAtClickedPoint[0];
            previousWorldCoordinatesAtClickedPoint[1] = recentWorldCoordinatesAtClickedPoint[1];
            previousWorldCoordinatesAtClickedPoint[2] = recentWorldCoordinatesAtClickedPoint[2];

        }
        buttonX = e.getX();
        glButtonY = e.getY();
    }

    @Override
    public void display(GLAutoDrawable drawable) {

        final GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        gl.glTranslatef(0f, 0f, -10.0f + translationZ);

        gl.glRotatef(rotationX, 1, 0, 0.0f);
        gl.glRotatef(rotationY, 0, 1, 0);

        float height = 0.1f;
        float width = 0.1f;
        float depth = 0.1f;
        float rift = 0.1f;
        HashMap<String, Point3D> colorMap = new HashMap<String, Point3D>();
        colorMap.put("Front", new Point3D(222f / 255f, 184f / 255f, 135f / 255f));
        drawCuboid(gl, new Point3D(-0, 2, -0), new Point3D(2, 2, 2), colorMap, true);

        drawCuboid(gl, new Point3D(-2 + width, 0, 0), new Point3D(width, 2, 2), colorMap, false);

        drawCuboid(gl, new Point3D(-0, 0, 0), new Point3D(width, 2, 2), colorMap, false);
        drawCuboid(gl, new Point3D(-0, -2, -0), new Point3D(2, 2, 2), colorMap, false);
        colorMap.put("Front", new Point3D(0.0f / 255f, 180f / 255f, 144f / 255f));
        drawCuboid(gl, new Point3D(0, 2, -2), new Point3D(2, 6, width), colorMap, false);

        drawInsertedSector(gl, new Point3D(0,0,0), new Point3D(2,2,2));
        drawMesh(gl);
        double[] projection = new double[16], modelView = new double[16];
        int viewport[] = new int[4];


        gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, modelView, 0);
        gl.glGetDoublev(GL2.GL_PROJECTION_MATRIX, projection, 0);
        gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);
        float viewportHeight = viewport[3];
        mathButtonY= viewportHeight - glButtonY;
        FloatBuffer z = GLBuffers.newDirectFloatBuffer(1);
        gl.glReadPixels((int)buttonX, (int)mathButtonY, 1, 1, GL2.GL_DEPTH_COMPONENT, GL2.GL_FLOAT, z);
        glu.gluUnProject(
                (double) buttonX,
                (double) mathButtonY,
                z.get(0),
                modelView, 0,
                projection, 0,
                viewport, 0,
                recentWorldCoordinatesAtClickedPoint, 0
        );

//        System.out.println(z.get(0) + ", " + "Most recently clicked: 2D: " + buttonX + ", " + y + "; 3D: " + recentWorldCoordinatesAtClickedPoint[0] + ", " + recentWorldCoordinatesAtClickedPoint[1] + ", " + recentWorldCoordinatesAtClickedPoint[2]);

        gl.glFlush();

        rquad -= 0.15f;
    }

    private void drawInsertedSector(GL2 gl, Point3D start, Point3D dimens){
        float width = 0.1f;
//        drawCuboid(gl, new Point3D(0,-2,0), new Point3D(2,2,2));
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

    private void drawCasting(GL2 gl) {
        float z = -4;
        float x = 4;
        float y = -4;
        gl.glBegin(GL2.GL_QUADS); // Start Drawing The Cube
        gl.glColor3f(1f, 0f, 0f);
        gl.glVertex3f(1.0f, 1.0f, z); // Top Right Of The Quad (Front)
        gl.glVertex3f(-1.0f, 1.0f, z); // Top Left Of The Quad (Front)
        gl.glVertex3f(-1.0f, -1.0f, z); // Bottom Left Of The Quad
        gl.glVertex3f(1.0f, -1.0f, z); // Bottom Right Of The Quad
        gl.glEnd();

        gl.glBegin(GL2.GL_QUADS); // Start Drawing The Cube
        gl.glColor3f(1f, 0f, 0f);
        gl.glVertex3f(x, 1.0f, 1.0f); // Top Right Of The Quad (Left)
        gl.glVertex3f(x, 1.0f, -1.0f); // Top Left Of The Quad (Left)
        gl.glVertex3f(x, -1.0f, -1.0f); // Bottom Left Of The Quad
        gl.glVertex3f(x, -1.0f, 1.0f); // Bottom Right Of The Quad
        gl.glEnd();

        gl.glBegin(GL2.GL_QUADS); // Start Drawing The Cube
        gl.glColor3f(1f, 0f, 0f); //red color
        gl.glVertex3f(1.0f, y, -1.0f); // Top Right Of The Quad (Top)
        gl.glVertex3f(-1.0f, y, -1.0f); // Top Left Of The Quad (Top)
        gl.glVertex3f(-1.0f, y, 1.0f); // Bottom Left Of The Quad (Top)
        gl.glVertex3f(1.0f, y, 1.0f); // Bottom Right Of The Quad (Top)
        gl.glEnd();
    }

    private void drawCuboid(GL2 gl) {
        //giving different colors to different sides
        gl.glBegin(GL2.GL_QUADS); // Start Drawing The Cube
        gl.glColor3f(1f, 0f, 0f); //red color
        gl.glVertex3f(1.0f, 1.0f, -1.0f); // Top Right Of The Quad (Top)
        gl.glVertex3f(-1.0f, 1.0f, -1.0f); // Top Left Of The Quad (Top)
        gl.glVertex3f(-1.0f, 1.0f, 1.0f); // Bottom Left Of The Quad (Top)
        gl.glVertex3f(1.0f, 1.0f, 1.0f); // Bottom Right Of The Quad (Top)

        gl.glColor3f(0f, 1f, 0f); //green color
        gl.glVertex3f(1.0f, -1.0f, 1.0f); // Top Right Of The Quad
        gl.glVertex3f(-1.0f, -1.0f, 1.0f); // Top Left Of The Quad
        gl.glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Left Of The Quad
        gl.glVertex3f(1.0f, -1.0f, -1.0f); // Bottom Right Of The Quad

        gl.glColor3f(0f, 0f, 1f); //blue color
        gl.glVertex3f(1.0f, 1.0f, 1.0f); // Top Right Of The Quad (Front)
        gl.glVertex3f(-1.0f, 1.0f, 1.0f); // Top Left Of The Quad (Front)
        gl.glVertex3f(-1.0f, -1.0f, 1.0f); // Bottom Left Of The Quad
        gl.glVertex3f(1.0f, -1.0f, 1.0f); // Bottom Right Of The Quad

        gl.glColor3f(1f, 1f, 0f); //yellow (red + green)
        gl.glVertex3f(1.0f, -1.0f, -1.0f); // Bottom Left Of The Quad
        gl.glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Right Of The Quad
        gl.glVertex3f(-1.0f, 1.0f, -1.0f); // Top Right Of The Quad (Back)
        gl.glVertex3f(1.0f, 1.0f, -1.0f); // Top Left Of The Quad (Back)

        gl.glColor3f(1f, 0f, 1f); //purple (red + green)
        gl.glVertex3f(-1.0f, 1.0f, 1.0f); // Top Right Of The Quad (Left)
        gl.glVertex3f(-1.0f, 1.0f, -1.0f); // Top Left Of The Quad (Left)
        gl.glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Left Of The Quad
        gl.glVertex3f(-1.0f, -1.0f, 1.0f); // Bottom Right Of The Quad

        gl.glColor3f(0f, 1f, 1f); //sky blue (blue +green)
        gl.glVertex3f(1.0f, 1.0f, -1.0f); // Top Right Of The Quad (Right)
        gl.glVertex3f(1.0f, 1.0f, 1.0f); // Top Left Of The Quad
        gl.glVertex3f(1.0f, -1.0f, 1.0f); // Bottom Left Of The Quad
        gl.glVertex3f(1.0f, -1.0f, -1.0f); // Bottom Right Of The Quad
        gl.glEnd(); // Done Drawing The Quad
    }

    private void drawCuboid(GL2 gl, Point3D startPoint, Point3D dimens, Map<String, Point3D> colorMap, boolean drawerImg) {
        //giving different colors to different sides
//        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_LINE);
//        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_FILL);


//        gl.glEnable( GL2.GL_LIGHTING );
//        gl.glEnable( GL2.GL_LIGHT0 );
//        gl.glEnable( GL2.GL_NORMALIZE );
        // weak RED ambient
//        float[] ambientLight = { 0.1f, 0.f, 0.f,0f };
//        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight, 0);
//
//        // multicolor diffuse
//        float[] diffuseLight = { 1f,2f,1f,0f };
//        gl.glLightfv( GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuseLight, 0 );
//        float[] lightPos = {0, 2, 2};
//        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos, 0);

        gl.glBegin(GL2.GL_QUADS); // Start Drawing The Cube

        gl.glColor4f(210.0f / 255f, 180f / 255f, 144f / 255f, 0.5f); //red color(top)

        gl.glVertex3f(((float) startPoint.getX()), ((float) startPoint.getY()), ((float) startPoint.getZ() - ((float) dimens.getZ()))); // Top Right Of The Quad (Top)
        gl.glVertex3f(((float) startPoint.getX() - ((float) dimens.getX())), ((float) startPoint.getY()), ((float) startPoint.getZ() - ((float) dimens.getZ()))); // Top Left Of The Quad (Top)
        gl.glVertex3f(((float) startPoint.getX() - ((float) dimens.getX())), ((float) startPoint.getY()), ((float) startPoint.getZ())); // Bottom Left Of The Quad (Top)
        gl.glVertex3f(((float) startPoint.getX()), ((float) startPoint.getY()), ((float) startPoint.getZ())); // Bottom Right Of The Quad (Top)

        gl.glColor4f(222f / 255f, 184f / 255f, 135f / 255f, 0.5f); //green color

        gl.glVertex3f(((float) startPoint.getX()), ((float) startPoint.getY() - (float) dimens.getY()), ((float) startPoint.getZ())); // Top Right Of The Quad
        gl.glVertex3f(((float) startPoint.getX() - ((float) dimens.getX())), ((float) startPoint.getY() - (float) dimens.getY()), ((float) startPoint.getZ())); // Top Left Of The Quad
        gl.glVertex3f(((float) startPoint.getX() - ((float) dimens.getX())), ((float) startPoint.getY() - (float) dimens.getY()), ((float) startPoint.getZ() - ((float) dimens.getZ()))); // Bottom Left Of The Quad
        gl.glVertex3f(((float) startPoint.getX()), ((float) startPoint.getY() - (float) dimens.getY()), ((float) startPoint.getZ() - ((float) dimens.getZ()))); // Bottom Right Of The Quad

        gl.glEnd();

        gl.glEnable(GL_TEXTURE_2D);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, texture);

        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        gl.glBegin(GL2ES3.GL_QUADS);
        Point3D color = colorMap.get("Front");
        if(!drawerImg)gl.glColor4f((float) color.getX(), (float) color.getY(), (float) color.getZ(), 0.5f); //green color
        else gl.glColor4f(1,1,1,0.5f); //green color
        if(drawerImg)gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(((float) startPoint.getX()), ((float) startPoint.getY()), (float) startPoint.getZ()); // Top Right Of The Quad (Front)
        if(drawerImg)    gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(((float) startPoint.getX() - ((float) dimens.getX())), ((float) startPoint.getY()), (float) startPoint.getZ()); // Top Left Of The Quad (Front)
        if(drawerImg)gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(((float) startPoint.getX() - ((float) dimens.getX())), ((float) startPoint.getY() - (float) dimens.getY()), (float) startPoint.getZ()); // Bottom Left Of The Quad
        if(drawerImg)  gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(((float) startPoint.getX()), ((float) startPoint.getY() - (float) dimens.getY()), (float) startPoint.getZ()); // Bottom Right Of The Quad
        gl.glEnd();

        gl.glDisable(GL_TEXTURE_2D);
        gl.glDisable(GL_BLEND);

        gl.glColor4f(245f / 255f, 222f / 255f, 179f / 255f, 0.5f); //green color
        gl.glBegin(GL2ES3.GL_QUADS);

        gl.glVertex3f(((float) startPoint.getX()), ((float) startPoint.getY() - (float) dimens.getY()), ((float) startPoint.getZ() - ((float) dimens.getZ()))); // Bottom Left Of The Quad
        gl.glVertex3f(((float) startPoint.getX() - ((float) dimens.getX())), ((float) startPoint.getY() - (float) dimens.getY()), ((float) startPoint.getZ() - ((float) dimens.getZ()))); // Bottom Right Of The Quad
        gl.glVertex3f(((float) startPoint.getX() - ((float) dimens.getX())), ((float) startPoint.getY()), ((float) startPoint.getZ() - ((float) dimens.getZ()))); // Top Right Of The Quad (Back)
        gl.glVertex3f(((float) startPoint.getX()), ((float) startPoint.getY()), ((float) startPoint.getZ() - ((float) dimens.getZ()))); // Top Left Of The Quad (Back)

        gl.glColor4f(222f / 255f, 184f / 255f, 135f / 255f, 0.5f); //green color

        gl.glVertex3f(((float) startPoint.getX() - ((float) dimens.getX())), ((float) startPoint.getY()), ((float) startPoint.getZ())); // Top Right Of The Quad (Left)
        gl.glVertex3f(((float) startPoint.getX() - ((float) dimens.getX())), ((float) startPoint.getY()), ((float) startPoint.getZ() - ((float) dimens.getZ()))); // Top Left Of The Quad (Left)
        gl.glVertex3f(((float) startPoint.getX() - ((float) dimens.getX())), ((float) startPoint.getY() - (float) dimens.getY()), ((float) startPoint.getZ() - ((float) dimens.getZ()))); // Bottom Left Of The Quad
        gl.glVertex3f(((float) startPoint.getX() - ((float) dimens.getX())), ((float) startPoint.getY() - (float) dimens.getY()), ((float) startPoint.getZ())); // Bottom Right Of The Quad

        gl.glColor4f(245f / 255f, 222f / 255f, 179f / 255f, .5f); //green color

        gl.glVertex3f(((float) startPoint.getX()), ((float) startPoint.getY()), ((float) startPoint.getZ() - ((float) dimens.getZ()))); // Top Right Of The Quad (Right)
        gl.glVertex3f(((float) startPoint.getX()), ((float) startPoint.getY()), ((float) startPoint.getZ())); // Top Left Of The Quad
        gl.glVertex3f(((float) startPoint.getX()), ((float) startPoint.getY() - (float) dimens.getY()), ((float) startPoint.getZ())); // Bottom Left Of The Quad
        gl.glVertex3f(((float) startPoint.getX()), ((float) startPoint.getY() - (float) dimens.getY()), ((float) startPoint.getZ() - ((float) dimens.getZ()))); // Bottom Right Of The Quad
        gl.glEnd(); // Done Drawing The Quad


        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_LINE);
        gl.glBegin(GL2.GL_POLYGON); // Start Drawing The Cube
        gl.glColor4f(0f, 0f, 0f, .5f); //blue color (front)
        gl.glVertex3f(((float) startPoint.getX()), ((float) startPoint.getY()), (float) startPoint.getZ()); // Top Right Of The Quad (Front)
        gl.glVertex3f(((float) startPoint.getX() - ((float) dimens.getX())), ((float) startPoint.getY()), (float) startPoint.getZ()); // Top Left Of The Quad (Front)
        gl.glVertex3f(((float) startPoint.getX() - ((float) dimens.getX())), ((float) startPoint.getY() - (float) dimens.getY()), (float) startPoint.getZ()); // Bottom Left Of The Quad
        gl.glVertex3f(((float) startPoint.getX()), ((float) startPoint.getY() - (float) dimens.getY()), (float) startPoint.getZ()); // Bottom Right Of The Quad
        gl.glEnd(); // Done Drawing The Quad
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_FILL);

//        gl.glBegin(GL.GL_LINES);
//        gl.glColor3f(0.0f, 0, 0);
//        gl.glVertex3f( ((float) (startPoint.getX() + 0.05f )), ((float) startPoint.getY()), (float) (startPoint.getZ() - dimens.getZ()) + 0.15f ); // Top Right Of The Quad (Front)
//        gl.glVertex3f( ((float) (startPoint.getX() ) + 0.05f ), ((float) (startPoint.getY() - dimens.getY()) ), (float) (startPoint.getZ()- dimens.getZ()) + 0.15f ); // Top Right Of The Quad (Front)
//        gl.glEnd();

        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_LINE);
        gl.glBegin(GL2.GL_POLYGON); // Start Drawing The Cube
        gl.glColor3f(0f, 0f, 0f); //blue color (front)
        gl.glVertex3f(((float) startPoint.getX()), ((float) startPoint.getY() - (float) dimens.getY()), ((float) startPoint.getZ())); // Top Right Of The Quad
        gl.glVertex3f(((float) startPoint.getX() - ((float) dimens.getX())), ((float) startPoint.getY() - (float) dimens.getY()), ((float) startPoint.getZ())); // Top Left Of The Quad
        gl.glVertex3f(((float) startPoint.getX() - ((float) dimens.getX())), ((float) startPoint.getY() - (float) dimens.getY()), ((float) startPoint.getZ() - ((float) dimens.getZ()))); // Bottom Left Of The Quad
        gl.glVertex3f(((float) startPoint.getX()), ((float) startPoint.getY() - (float) dimens.getY()), ((float) startPoint.getZ() - ((float) dimens.getZ()))); // Bottom Right Of The Quad
        gl.glEnd(); // Done Drawing The Quad
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_FILL);

        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_LINE);
        gl.glBegin(GL2.GL_POLYGON); // Start Drawing The Cube
        gl.glColor3f(0f, 0f, 0f); //blue color (front)
        gl.glVertex3f(((float) startPoint.getX()), ((float) startPoint.getY()), (float) startPoint.getZ()); // Top Right Of The Quad (Front)
        gl.glVertex3f(((float) startPoint.getX() - ((float) dimens.getX())), ((float) startPoint.getY()), (float) startPoint.getZ()); // Top Left Of The Quad (Front)
        gl.glVertex3f(((float) startPoint.getX() - ((float) dimens.getX())), ((float) startPoint.getY() - (float) dimens.getY()), (float) startPoint.getZ()); // Bottom Left Of The Quad
        gl.glVertex3f(((float) startPoint.getX()), ((float) startPoint.getY() - (float) dimens.getY()), (float) startPoint.getZ()); // Bottom Right Of The Quad
        gl.glEnd(); // Done Drawing The Quad
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_FILL);

        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_LINE);
        gl.glBegin(GL2.GL_POLYGON); // Start Drawing The Cube
        gl.glColor3f(0f, 0f, 0f); //blue color (front)
        gl.glVertex3f(((float) startPoint.getX()), ((float) startPoint.getY() - (float) dimens.getY()), ((float) startPoint.getZ() - ((float) dimens.getZ()))); // Bottom Left Of The Quad
        gl.glVertex3f(((float) startPoint.getX() - ((float) dimens.getX())), ((float) startPoint.getY() - (float) dimens.getY()), ((float) startPoint.getZ() - ((float) dimens.getZ()))); // Bottom Right Of The Quad
        gl.glVertex3f(((float) startPoint.getX() - ((float) dimens.getX())), ((float) startPoint.getY()), ((float) startPoint.getZ() - ((float) dimens.getZ()))); // Top Right Of The Quad (Back)
        gl.glVertex3f(((float) startPoint.getX()), ((float) startPoint.getY()), ((float) startPoint.getZ() - ((float) dimens.getZ()))); // Top Left Of The Quad (Back)
        gl.glEnd(); // Done Drawing The Quad
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_FILL);

        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_LINE);
        gl.glBegin(GL2.GL_POLYGON); // Start Drawing The Cube
        gl.glColor3f(0f, 0f, 0f); //blue color (front)
        gl.glVertex3f(((float) startPoint.getX() - ((float) dimens.getX())), ((float) startPoint.getY()), ((float) startPoint.getZ())); // Top Right Of The Quad (Left)
        gl.glVertex3f(((float) startPoint.getX() - ((float) dimens.getX())), ((float) startPoint.getY()), ((float) startPoint.getZ() - ((float) dimens.getZ()))); // Top Left Of The Quad (Left)
        gl.glVertex3f(((float) startPoint.getX() - ((float) dimens.getX())), ((float) startPoint.getY() - (float) dimens.getY()), ((float) startPoint.getZ() - ((float) dimens.getZ()))); // Bottom Left Of The Quad
        gl.glVertex3f(((float) startPoint.getX() - ((float) dimens.getX())), ((float) startPoint.getY() - (float) dimens.getY()), ((float) startPoint.getZ())); // Bottom Right Of The Quad
        gl.glEnd(); // Done Drawing The Quad
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_FILL);

        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_LINE);
        gl.glBegin(GL2.GL_POLYGON); // Start Drawing The Cube
        gl.glColor3f(0f, 0f, 0f); //blue color (front)
        gl.glVertex3f(((float) startPoint.getX()), ((float) startPoint.getY()), ((float) startPoint.getZ() - ((float) dimens.getZ()))); // Top Right Of The Quad (Right)
        gl.glVertex3f(((float) startPoint.getX()), ((float) startPoint.getY()), ((float) startPoint.getZ())); // Top Left Of The Quad
        gl.glVertex3f(((float) startPoint.getX()), ((float) startPoint.getY() - (float) dimens.getY()), ((float) startPoint.getZ())); // Bottom Left Of The Quad
        gl.glVertex3f(((float) startPoint.getX()), ((float) startPoint.getY() - (float) dimens.getY()), ((float) startPoint.getZ() - ((float) dimens.getZ()))); // Bottom Right Of The Quad
        gl.glEnd(); // Done Drawing The Quad
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_FILL);

    }

    private void drawMesh(GL2 gl) {
        float z = -4;
        float x = 4;
        float y = -4;

        for (int i = -4; i < 4; i++) {
            for (int j = -4; j < 4; j++) {
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

        for (int k = 0; k < 2; k++) {
            x = -x;
            for (int i = -4; i < 4; i++) {
                for (int j = -4; j < 4; j++) {
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
        for (int i = -4; i < 4; i++) {
            for (int j = -4; j < 4; j++) {
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

    private void drawCuboid(GL2 gl, Point3D startPoint, Point3D dimens, float color){
        float mox = (float) startPoint.getX();
        float moy = (float) (startPoint.getY() - dimens.getY());
        float moz = (float) startPoint.getZ();
        float ox = (float) (startPoint.getX() + dimens.getX());
        float oy = (float) startPoint.getY();
        float oz = (float) (startPoint.getZ() + dimens.getZ());
        gl.glBegin(GL2ES3.GL_QUADS);
//        gl.glColor3f(1.0f, 0f, 0f); //red color
        gl.glColor3f(color, color, color); //blue color

        gl.glVertex3f(ox, oy, moz); // Top Right Of The Quad (Top)
        gl.glVertex3f(mox, oy, moz); // Top Left Of The Quad (Top)
        gl.glVertex3f(mox, oy, oz); // Bottom Left Of The Quad (Top)
        gl.glVertex3f(ox, oy, oz); // Bottom Right Of The Quad (Top)
        gl.glEnd();

        gl.glBegin(GL2ES3.GL_QUADS);
//        gl.glColor3f(0f, 1f, 0f); //green color
        gl.glColor3f(color, color, color); //blue color

        gl.glVertex3f(ox, moy, oz); // Top Right Of The Quad
        gl.glVertex3f(mox, moy, oz); // Top Left Of The Quad
        gl.glVertex3f(mox, moy, moz); // Bottom Left Of The Quad
        gl.glVertex3f(ox, moy, moz); // Bottom Right Of The Quad
        gl.glEnd();

        gl.glBegin(GL2ES3.GL_QUADS);
//        gl.glColor3f(0f, 0f, 1f); //blue color
        gl.glColor3f(color, color, color); //blue color

        gl.glVertex3f(ox, oy, oz); // Top Right Of The Quad (Front)
        gl.glVertex3f(mox, oy, oz); // Top Left Of The Quad (Front)
        gl.glVertex3f(mox, moy, oz); // Bottom Left Of The Quad
        gl.glVertex3f(ox, moy, oz); // Bottom Right Of The Quad
        gl.glEnd();

        gl.glBegin(GL2ES3.GL_QUADS);
        gl.glColor3f(color, color, color); //yellow (red + green)

        gl.glVertex3f(ox, moy, moz); // Bottom Left Of The Quad
        gl.glVertex3f(mox, moy, moz); // Bottom Right Of The Quad
        gl.glVertex3f(mox, oy, moz); // Top Right Of The Quad (Back)
        gl.glVertex3f(ox, oy, moz); // Top Left Of The Quad (Back)
        gl.glEnd();

        gl.glBegin(GL2ES3.GL_QUADS);
//        gl.glColor3f(1f, 0f, 1f); //purple (red + green)
        gl.glColor3f(color, color, color); //blue color

        gl.glVertex3f(mox, oy, oz); // Top Right Of The Quad (Left)
        gl.glVertex3f(mox, oy, moz); // Top Left Of The Quad (Left)
        gl.glVertex3f(mox, moy, moz); // Bottom Left Of The Quad
        gl.glVertex3f(mox, moy, oz); // Bottom Right Of The Quad
        gl.glEnd();

        gl.glBegin(GL2ES3.GL_QUADS);
//        gl.glColor3f(0f, 1f, 1f); //sky blue (blue +green)
        gl.glColor3f(color, color, color); //blue color

        gl.glVertex3f(ox, oy, moz); // Top Right Of The Quad (Right)
        gl.glVertex3f(ox, oy, oz); // Top Left Of The Quad
        gl.glVertex3f(ox, moy, oz); // Bottom Left Of The Quad
        gl.glVertex3f(ox, moy, moz); // Bottom Right Of The Quad
        gl.glEnd();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        // TODO Auto-generated method stub
    }


    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

        // TODO Auto-generated method stub
        final GL2 gl = drawable.getGL().getGL2();
        if (height <= 0)
            height = 1;

        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        glu.gluPerspective(75.0f, h, 1.0, 20.0f);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
//        gl.glLoadIdentity();
//        gl.glRotatef((float) (.5f * 2 * Math.PI), 0.0f, 1.0f,0.0f);
    }

    @Override
    public void init(GLAutoDrawable drawable) {

        final GL2 gl = drawable.getGL().getGL2();
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glClearColor(1, 1, 1, 0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);

        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
        gl.glHint(GL_LINE_SMOOTH_HINT, GL2.GL_NICEST);

        gl.glEnable(GL2.GL_TEXTURE_2D);
        try{

            File im = new File("src/icons/szuflada_front.png ");
            Texture t = TextureIO.newTexture(im, true);
            texture= t.getTextureObject(gl);

        }catch(Exception e){
            e.printStackTrace();
        }
        gl.glDisable(GL2.GL_TEXTURE_2D);
    }

    public static void main(String[] args) {

        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        // The canvas
        final GLCanvas glcanvas = new GLCanvas(capabilities);
        Cube cube = new Cube();
        glcanvas.addMouseListener(cube);
        glcanvas.addMouseMotionListener(cube);
        glcanvas.addGLEventListener(cube);
        glcanvas.addMouseWheelListener(cube);
        glcanvas.setSize(400, 400);

        final JFrame frame = new JFrame(" Multicolored cube");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(glcanvas);
        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setVisible(true);
        final FPSAnimator animator = new FPSAnimator(glcanvas, 300, true);

        animator.start();
    }


}