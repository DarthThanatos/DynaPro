package display._3d;

import com.jogamp.opengl.GL2;
import config.Config;
import javafx.geometry.Point3D;
import model.ArrangementAggregate;
import model.Element;
import model.FrontConfiguration;
import model.Furniture;

import static util.SlabSidePositionUtil.*;

class RowPerspectiveDrawer extends AggregatePerspectiveDrawer {

    RowPerspectiveDrawer(int drawerTexture, int leftDoorTexture, int rightDoorTexture, boolean showFronts) {
        super(drawerTexture, leftDoorTexture, rightDoorTexture, showFronts);
    }

    @Override
    void drawElementSeparator(GL2 gl, Point3D currentPointWithOffsets, Point3D furnitureDimens, Element currentElement, ArrangementAggregate currentAggregate, Furniture furniture) {
        //start x,y,z - coords of the front plane
        //dimens - y - height of separator, z - depth of furniture, x - slabMeshThickness
        float separatorColor = 50 / 255f;
        float separatorStartX, separatorStartY, separatorStartZ;
        float separatorWidth, separatorHeight, separatorDepth;

        FrontConfiguration configuration = furniture.getFrontConfiguration();
        separatorStartX = getRightSideX((int) currentPointWithOffsets.getX(),currentElement.getWidth(), configuration.isElemWithIdLastToTheRight(currentElement.getId())) - Config.SLAB_THICKNESS;

        separatorStartY = getTopSlabY(currentPointWithOffsets, configuration.isElemWithIdLastToTheTop(currentElement.getId())) - Config.SLAB_THICKNESS;
        separatorStartZ = calculateFrontZ(currentPointWithOffsets.getZ(),  furnitureDimens.getZ(), furniture.getBackInserted());

        separatorWidth = Config.SLAB_THICKNESS;
        separatorHeight = currentAggregate.getElementSeparatorFirstDimension();
        separatorDepth = currentAggregate.getElementSeparatorSecondDimension();

        drawCuboid(
                gl,
                new Point3D(separatorStartX, separatorStartY, separatorStartZ),
                new Point3D(separatorWidth, separatorHeight, separatorDepth),
                separatorColor,false
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
                (int) currentPointWithOffsets.getY(),
                currentAggregate.getAggregateHeightWithGaps(false),
                configuration.isElemWithIdLastToTheBottom(currentAggregate.get(0).getId())
        );
        separatorStartZ = calculateFrontZ(currentPointWithOffsets.getZ(),  furnitureDimens.getZ(), furniture.getBackInserted());

        separatorWidth = currentAggregate.getAggregateSeparatorFirstDimension();
        separatorHeight = Config.SLAB_THICKNESS;
        separatorDepth = currentAggregate.getAggregateSeparatorSecondDimension();

        drawCuboid(gl,
                new Point3D(separatorStartX,separatorStartY,separatorStartZ),
                new Point3D(separatorWidth,separatorHeight,separatorDepth),
                separatorColor,false
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
                currentPoint.getY() - currentAggregate.get(0).getHeight() -  2 * Config.BETWEEN_ELEMENTS_HORIZONTAL_GAP,
                currentPoint.getZ()
        );
    }
}
