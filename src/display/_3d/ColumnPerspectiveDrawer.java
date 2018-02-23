package display._3d;

import com.jogamp.opengl.GL2;
import config.Config;
import javafx.geometry.Point3D;
import model.ArrangementAggregate;
import model.Element;
import model.FrontConfiguration;
import model.Furniture;

class ColumnPerspectiveDrawer extends AggregatePerspectiveDrawer{

    ColumnPerspectiveDrawer(int drawerTexture, int leftDoorTexture, int rightDoorTexture) {
        super(drawerTexture, leftDoorTexture, rightDoorTexture);
    }

    @Override
    void drawElementSeparator(GL2 gl, Point3D currentPointWithOffsets, Point3D furnitureDimens, Element currentElement, Furniture furniture) {
        float separatorColor = 120 / 255f;
        float separatorStartX, separatorStartY, separatorStartZ;
        float separatorWidth, separatorHeight, separatorDepth;

        FrontConfiguration configuration = furniture.getFrontConfiguration();
        separatorStartX = getLeftSideX(currentPointWithOffsets, configuration.isElemWithIdLastToTheLeft(currentElement.getId())) + Config.SLAB_THICKNESS;
        separatorStartY = getBottomSlabY(currentPointWithOffsets, new Point3D(0,currentElement.getHeight(), 0), configuration.isElemWithIdLastToTheBottom(currentElement.getId()));
        separatorStartZ = (float) (currentPointWithOffsets.getZ() - furnitureDimens.getZ() + (furniture.getBackInserted() ? Config.SLAB_THICKNESS : 0));

        separatorWidth = getRightSideX(currentPointWithOffsets, new Point3D(currentElement.getWidth(),0, 0), configuration.isElemWithIdLastToTheRight(currentElement.getId())) - getLeftSideX(currentPointWithOffsets, configuration.isElemWithIdLastToTheLeft(currentElement.getId())) - 2*Config.SLAB_THICKNESS;
        separatorHeight = Config.SLAB_THICKNESS;
        separatorDepth = (float) (furnitureDimens.getZ() - (furniture.getBackInserted() ? Config.SLAB_THICKNESS : 0));

        drawCuboid(gl,
                new Point3D(separatorStartX,separatorStartY,separatorStartZ),
                new Point3D(separatorWidth,separatorHeight,separatorDepth),
                separatorColor
        );

    }

    @Override
    void drawAggregateSeparator(GL2 gl, Point3D currentPointWithOffsets, Point3D furnitureDimens, ArrangementAggregate currentAggregate, Furniture furniture) {
        //start x,y,z - coords of the front plane
        //dimens - y - height of separator, z - depth of furniture, x - slabMeshThickness
        float separatorColor = 50 / 255f;
        float separatorStartX, separatorStartY, separatorStartZ;
        float separatorWidth, separatorHeight, separatorDepth;

        FrontConfiguration configuration = furniture.getFrontConfiguration();
        separatorStartX = getRightSideX(currentPointWithOffsets, new Point3D(currentAggregate.get(0).getWidth(), 0,0), configuration.isElemWithIdLastToTheRight(currentAggregate.get(0).getId())) - Config.SLAB_THICKNESS;
        separatorStartY = getTopSlabY(currentPointWithOffsets, configuration.isElemWithIdLastToTheTop(currentAggregate.get(0).getId())) - Config.SLAB_THICKNESS;
        separatorStartZ = (float) (currentPointWithOffsets.getZ() - furnitureDimens.getZ() + (furniture.getBackInserted() ? Config.SLAB_THICKNESS : 0));

        separatorWidth = Config.SLAB_THICKNESS;
        separatorHeight = separatorStartY - getBottomSlabY(currentPointWithOffsets, new Point3D(0, currentAggregate.getAggregateHeightWithGaps(true), 0), true);
        separatorDepth = (float) (furnitureDimens.getZ() - (furniture.getBackInserted() ? Config.SLAB_THICKNESS : 0));

        drawCuboid(
                gl,
                new Point3D(separatorStartX, separatorStartY, separatorStartZ),
                new Point3D(separatorWidth, separatorHeight, separatorDepth),
                separatorColor
        );

    }

    @Override
    Point3D calculateNextElementPosition(Element currentElement, Point3D currentPoint) {
        return new Point3D(
                currentPoint.getX(),
                currentPoint.getY() - currentElement.getHeight() - 2 * Config.BETWEEN_ELEMENTS_HORIZONTAL_GAP,
                currentPoint.getZ()
        );
    }

    @Override
    Point3D calculateNextAggregatePosition(ArrangementAggregate currentAggregate, Point3D currentPoint, Point3D startPoint) {
        return new Point3D(
                currentPoint.getX() + currentAggregate.get(0).getWidth() + 2 * Config.BETWEEN_ELEMENTS_VERTICAL_GAP,
                startPoint.getY(),
                currentPoint.getZ()
        );
    }

}
