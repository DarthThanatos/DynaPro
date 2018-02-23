package display._3d;

import com.jogamp.opengl.GL2;
import config.Config;
import javafx.geometry.Point3D;
import model.*;

import java.util.Iterator;

abstract class AggregatePerspectiveDrawer extends CuboidDrawer{

    void drawFurniture(GL2 gl, Furniture furniture, Point3D start, Point3D dimens){
        Point3D currentPoint = new Point3D(start.getX(), start.getY(), start.getZ() + dimens.getZ());
        Iterator<ArrangementAggregate> aggregatesIter = furniture.getFrontConfiguration().getConfiguration().iterator();
        ArrangementAggregate arrangementAggregate = aggregatesIter.next();
        while (true){
            drawAggregate(gl, currentPoint, dimens, arrangementAggregate, furniture);
            if(aggregatesIter.hasNext()) {
                arrangementAggregate = aggregatesIter.next();
                drawAggregateSeparator(gl, currentPoint, dimens, arrangementAggregate, furniture);
                currentPoint = calculateNextAggregatePosition(arrangementAggregate, currentPoint, start);
            }
            else break;
        }
    }

    private void drawAggregate(GL2 gl,  Point3D currentPoint, Point3D furnitureDimens , ArrangementAggregate aggregate, Furniture furniture){
        Iterator<Element> elementsIter = aggregate.iterator();
        while (elementsIter.hasNext()){
            Element element = elementsIter.next();
            Point3D currentPointWithOffsets = getPointWithOffsets(currentPoint);
            tryDrawingDrawer(gl, furniture, element, currentPointWithOffsets, furnitureDimens, elementsIter.hasNext());
            tryDrawingLeftDoor(gl, furniture, element, currentPointWithOffsets, furnitureDimens, elementsIter.hasNext());
            tryDrawingRightDoor(gl, furniture, element, currentPointWithOffsets, furnitureDimens, elementsIter.hasNext());
            currentPoint = calculateNextElementPosition(element, currentPoint);
        }
    }

    private Point3D getPointWithOffsets(Point3D point){
        return new Point3D(
                point.getX() + Config.BETWEEN_ELEMENTS_VERTICAL_GAP,
                point.getY() - Config.BETWEEN_ELEMENTS_HORIZONTAL_GAP,
                point.getZ()
        );
    }

    private void tryDrawingDrawer(GL2 gl, Furniture furniture, Element element, Point3D currentPointWithOffsets, Point3D furnitureDimens, boolean isTheLastOne){
        if (element.getType().equals(Config.DRAWER_PL)){
            FrontConfiguration configuration = furniture.getFrontConfiguration();
            new DrawerDrawer(
                    (Drawer) element,
                    furniture.getBackInserted(),
                    configuration.isElemWithIdLastToTheLeft(element.getId()),
                    configuration.isElemWithIdLastToTheRight(element.getId()),
                    configuration.isElemWithIdLastToTheBottom(element.getId()),
                    element.getShelvesNumber()
            ).drawDrawer(
                    gl,
                    currentPointWithOffsets,
                    new Point3D(element.getWidth(), element.getHeight(), furnitureDimens.getZ())
            );

            if(!furniture.getFrontConfiguration().getColumnOriented() && !isTheLastOne){
                drawElementSeparator(gl, currentPointWithOffsets, furnitureDimens, element, furniture);
            }
    //        new DrawerDrawer(false, true, false, false, 1).drawDrawer(
    //                gl,
    //                new Point3D(furnitureStartX + 2/Config.MESH_UNIT, furnitureStartY - 3 / Config.MESH_UNIT, 3), //furnitureStartZ + furnitureDepth / Config.MESH_UNIT
    //                new Point3D(364/Config.MESH_UNIT , 205/Config.MESH_UNIT, furnitureDepth / Config.MESH_UNIT)
    //        );

        }
    }

    private void tryDrawingLeftDoor(GL2 gl, Furniture furniture, Element element, Point3D currentPointWithOffsets, Point3D furnitureDimens, boolean isTheLastOne){
        if(element.getType().equals(Config.LEFT_DOOR_PL)){
            FrontConfiguration configuration = furniture.getFrontConfiguration();
            new LeftDoorDrawer(
                    (LeftDoor) element,
                    furniture.getBackInserted(),
                    configuration.isElemWithIdLastToTheLeft(element.getId()),
                    configuration.isElemWithIdLastToTheRight(element.getId())
            ).drawDoor(
                    gl,
                    currentPointWithOffsets,
                    new Point3D(element.getWidth(), element.getHeight(), furnitureDimens.getZ())
            );
            if(!isTheLastOne){
                drawElementSeparator(gl, currentPointWithOffsets, furnitureDimens, element, furniture);
            }
        }

//        new DoorDrawer(false, true, false, true,true, 1).drawDoor(
//                gl,
//                new Point3D(furnitureStartX, furnitureStartY, furnitureStartZ),
//                new Point3D(furnitureWidth/ Config.MESH_UNIT, furnitureHeight / Config.MESH_UNIT, furnitureDepth/ Config.MESH_UNIT)
//        );

    }

    private void tryDrawingRightDoor(GL2 gl, Furniture furniture, Element element, Point3D currentPointWithOffsets, Point3D furnitureDimens, boolean isTheLastOne){
        if(element.getType().equals(Config.RIGHT_DOOR_PL)){
            FrontConfiguration configuration = furniture.getFrontConfiguration();
            new RightDoorDrawer(
                    (RightDoor)element,
                    furniture.getBackInserted(),
                    configuration.isElemWithIdLastToTheLeft(element.getId()),
                    configuration.isElemWithIdLastToTheRight(element.getId())
            ).drawDoor(
                    gl,
                    currentPointWithOffsets,
                    new Point3D(element.getWidth(), element.getHeight(), furnitureDimens.getZ())
            );

            if(!isTheLastOne){
                drawElementSeparator(gl, currentPointWithOffsets, furnitureDimens, element, furniture);
            }
        }
    }

    abstract void drawElementSeparator(GL2 gl, Point3D currentPointWithOffsets, Point3D furnitureDimens, Element currentElement, Furniture furniture);
    abstract void drawAggregateSeparator(GL2 gl, Point3D currentPointWithOffsets, Point3D furnitureDimens, ArrangementAggregate currentAggregate, Furniture furniture);
    abstract Point3D calculateNextElementPosition(Element currentElement, Point3D currentPoint);
    abstract Point3D calculateNextAggregatePosition(ArrangementAggregate currenctAggregate, Point3D currentPoint, Point3D startPoint);
}
