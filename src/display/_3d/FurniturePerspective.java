package display._3d;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import config.Config;
import javafx.geometry.Point3D;
import model.Furniture;

import java.awt.event.*;
import java.io.File;

import static com.jogamp.opengl.GL.*;

//import Jama.Matrix;

@SuppressWarnings("WeakerAccess")
public class FurniturePerspective extends  GLCanvas implements GLEventListener, MouseListener, MouseMotionListener, MouseWheelListener {

    private GLU glu = new GLU();
    private float rotationX, rotationY, buttonX, glButtonY, translationZ;
    private int drawerTexture, leftDoorTexture, rightDoorTexture;
    private Furniture furniture;

    @Override
    public void mousePressed(MouseEvent e) {
        buttonX = e.getX(); glButtonY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
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
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        translationZ -= e.getPreciseWheelRotation();
    }



    @Override
    public void mouseClicked(MouseEvent e) {
        buttonX = e.getX();
        glButtonY = e.getY();
    }



    @Override
    public void display(GLAutoDrawable drawable) {
        if(furniture == null) return;
        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glTranslatef(0f, 0f, -10.0f + translationZ);
        gl.glRotatef(rotationX, 1, 0, 0.0f);
        gl.glRotatef(rotationY, 0, 1, 0);

        float furnitureHeight = furniture.getHeight(), furnitureWidth = furniture.getWidth(), furnitureDepth = furniture.getDepth();
        float furnitureStartX = - furnitureWidth / (2), furnitureStartY = -5 * Config.MESH_UNIT + furnitureHeight , furnitureStartZ = - furnitureDepth / (2);
        Point3D furnitureStart = new Point3D(furnitureStartX, furnitureStartY, furnitureStartZ);
        Point3D furnitureDimens = new Point3D(furnitureWidth, furnitureHeight, furnitureDepth );
        new ModuleSkeletonDrawer(furniture).drawModuleSkeleton(
                gl,
                furnitureStart,
                furnitureDimens
        );
        if(furniture.getFrontConfiguration().getColumnOriented())
            new ColumnPerspectiveDrawer(drawerTexture, leftDoorTexture, rightDoorTexture).drawFurniture(gl, furniture, furnitureStart, furnitureDimens);
        else
            new RowPerspectiveDrawer(drawerTexture, leftDoorTexture, rightDoorTexture).drawFurniture(gl, furniture, furnitureStart, furnitureDimens);

        new MeshDrawer().drawMesh(gl, new Point3D(-5,-5,-5), new Point3D(10,10,10));
        gl.glFlush();

    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        animator.stop();
    }


    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        final GL2 gl = drawable.getGL().getGL2();
        if (height <= 0)
            height = 1;

        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        glu.gluPerspective(75.0f, h, 1.0, 20.0f);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    private FPSAnimator animator;


    private int newTextureKey(GL2 gl, String path){
        int res = -1;
        gl.glEnable(GL2.GL_TEXTURE_2D);
        try{

            File im = new File(path);
            Texture t = TextureIO.newTexture(im, true);
            res = t.getTextureObject(gl);

        }catch(Exception e){
            e.printStackTrace();
        }
        gl.glDisable(GL2.GL_TEXTURE_2D);
        return res;
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

        drawerTexture = newTextureKey(gl, "src/icons/szuflada_front.png");
        leftDoorTexture = newTextureKey(gl, "src/icons/drzwiczki_klamka_prawo.png");
        rightDoorTexture = newTextureKey(gl, "src/icons/drzwiczki_klamka_lewo.png");

        animator = new FPSAnimator(this, 300, true);
        animator.start();

    }

    public FurniturePerspective(){

        super(new GLCapabilities( GLProfile.get( GLProfile.GL2 )));
        addMouseListener(this);
        addMouseMotionListener(this);
        addGLEventListener(this);
        addMouseWheelListener(this);
    }


    public void setFurniture(Furniture furniture) {
        this.furniture = furniture;
    }
}