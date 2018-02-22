package display._3d;

import com.jogamp.opengl.GL2;
import config.Config;
import javafx.geometry.Point3D;
import model.ArrangementAggregate;
import model.Element;
import model.Furniture;

import java.util.Iterator;

abstract class AggregatePerspectiveDrawer extends CuboidDrawer{

    void drawFurniture(GL2 gl, Furniture furniture, Point3D start, Point3D dimens){
        for(ArrangementAggregate arrangementAggregate : furniture.getFrontConfiguration().getConfiguration()){
            drawAggregate(gl, start, dimens, arrangementAggregate);
        }
    }

    private void drawAggregate(GL2 gl, Point3D furnitureStart, Point3D furnitureDimens, ArrangementAggregate column){
        Iterator<Element> elementsIter = column.iterator();
        while (elementsIter.hasNext()){

            if(elementsIter.hasNext()){
//               drawSeparator();
            }
        }
    }

    private void tryDrawingDrawer(GL2 gl, Element element, Point3D currentStart, Point3D furnitureDimens){
        if (element.getType().equals(Config.DRAWER_PL)){
    //        new DrawerDrawer().drawDrawer(gl, );
    //        new DrawerDrawer(false, true, false, false, 1).drawDrawer(
    //                gl,
    //                new Point3D(furnitureStartX + 2/Config.MESH_UNIT, furnitureStartY - 3 / Config.MESH_UNIT, 3), //furnitureStartZ + furnitureDepth / Config.MESH_UNIT
    //                new Point3D(364/Config.MESH_UNIT , 205/Config.MESH_UNIT, furnitureDepth / Config.MESH_UNIT)
    //        );
    //
        }
    }

    private void tryDrawingLeftDoor(){
//        new DoorDrawer(false, true, false, true,true, 1).drawDoor(
//                gl,
//                new Point3D(furnitureStartX, furnitureStartY, furnitureStartZ),
//                new Point3D(furnitureWidth/ Config.MESH_UNIT, furnitureHeight / Config.MESH_UNIT, furnitureDepth/ Config.MESH_UNIT)
//        );

    }

    abstract void drawSeparator(GL2 gl, Point3D start, Point3D dimens, boolean lastToTheLeft, boolean lastToTheRight, boolean lastToTheTop, boolean lastToTheBottom, boolean backInserted);

}
