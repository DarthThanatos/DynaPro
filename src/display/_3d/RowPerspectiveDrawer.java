package display._3d;

import com.jogamp.opengl.GL2;
import config.Config;
import javafx.geometry.Point3D;
import model.ArrangementAggregate;
import model.Element;
import model.FrontConfiguration;
import model.Furniture;

class RowPerspectiveDrawer extends AggregatePerspectiveDrawer {

    @Override
    void drawElementSeparator(GL2 gl, Point3D currentPointWithOffsets, Point3D furnitureDimens, Element currentElement, Furniture furniture) {        //start x,y,z - coords of the front plane
        //dimens - y - height of separator, z - depth of furniture, x - slabMeshThickness
        float separatorColor = 50 / 255f;
        float separatorStartX, separatorStartY, separatorStartZ;
        float separatorWidth, separatorHeight, separatorDepth;

        FrontConfiguration configuration = furniture.getFrontConfiguration();
        separatorStartX = getRightSideX(
                currentPointWithOffsets,
                new Point3D(currentElement.getWidth(), 0,0),
                configuration.isElemWithIdLastToTheRight(currentElement.getId())
        ) - Config.SLAB_THICKNESS;

        separatorStartY = getTopSlabY(currentPointWithOffsets, configuration.isElemWithIdLastToTheTop(currentElement.getId())) - Config.SLAB_THICKNESS;
        separatorStartZ = (float) (currentPointWithOffsets.getZ() - furnitureDimens.getZ() + (furniture.getBackInserted() ? Config.SLAB_THICKNESS : 0));

        separatorWidth = Config.SLAB_THICKNESS;
        separatorHeight = separatorStartY - getBottomSlabY(currentPointWithOffsets, new Point3D(0, currentElement.getHeight(), 0), configuration.isElemWithIdLastToTheBottom(currentElement.getId()));
        separatorDepth = (float) (furnitureDimens.getZ() - (furniture.getBackInserted() ? Config.SLAB_THICKNESS : 0));

        drawCuboid(
                gl,
                new Point3D(separatorStartX, separatorStartY, separatorStartZ),
                new Point3D(separatorWidth, separatorHeight, separatorDepth),
                separatorColor
        );

    }

    @Override
    void drawAggregateSeparator(GL2 gl, Point3D currentPointWithOffsets, Point3D furnitureDimens, ArrangementAggregate currentAggregate, Furniture furniture) {
        float separatorColor = 120 / 255f;
        float separatorStartX, separatorStartY, separatorStartZ;
        float separatorWidth, separatorHeight, separatorDepth;

        FrontConfiguration configuration = furniture.getFrontConfiguration();
        separatorStartX = getLeftSideX(currentPointWithOffsets, true) + Config.SLAB_THICKNESS;
        separatorStartY = getBottomSlabY(
                currentPointWithOffsets,
                new Point3D(currentAggregate.getAggregateWidthWithGaps(false), 0, 0),
                configuration.isElemWithIdLastToTheBottom(currentAggregate.get(0).getId())
        );
        separatorStartZ = (float) (currentPointWithOffsets.getZ() - furnitureDimens.getZ() + (furniture.getBackInserted() ? Config.SLAB_THICKNESS : 0));

        separatorWidth =
                getRightSideX(currentPointWithOffsets, new Point3D(currentAggregate.getAggregateWidthWithGaps(false), 0, 0),true)
                        - getLeftSideX(currentPointWithOffsets, true) - 2*Config.SLAB_THICKNESS;
        separatorHeight = Config.SLAB_THICKNESS;
        separatorDepth = (float) (furnitureDimens.getZ() - (furniture.getBackInserted() ? Config.SLAB_THICKNESS : 0));

        drawCuboid(gl,
                new Point3D(separatorStartX,separatorStartY,separatorStartZ),
                new Point3D(separatorWidth,separatorHeight,separatorDepth),
                separatorColor
        );
    }

    @Override
    Point3D calculateNextElementPosition(Element currentElement, Point3D currentPoint) {
        return new Point3D(
                currentPoint.getX()  + currentElement.getWidth() + 2 * Config.BETWEEN_ELEMENTS_VERTICAL_GAP,
                currentPoint.getY(),
                currentPoint.getZ()
        );
    }

    @Override
    Point3D calculateNextAggregatePosition(ArrangementAggregate currentAggregate, Point3D currentPoint, Point3D startPoint) {
        return new Point3D(
                startPoint.getX(),
                currentPoint.getY() + currentAggregate.get(0).getHeight() +  2 * Config.BETWEEN_ELEMENTS_HORIZONTAL_GAP,
                currentPoint.getZ()
        );
    }
}
