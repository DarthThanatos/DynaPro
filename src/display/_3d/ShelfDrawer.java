package display._3d;

import com.jogamp.opengl.GL2;
import config.Config;
import javafx.geometry.Point3D;
import model.Element;
import model.ShelfSlabFurnitureTree;

import static util.SlabSidePositionUtil.getLeftSideX;

class ShelfDrawer extends CuboidDrawer {

    private final boolean isLastToTheLeft;
    private final boolean backInserted;
    private final ShelfSlabFurnitureTree element;

    ShelfDrawer(ShelfSlabFurnitureTree element, boolean backInserted, boolean isLastToTheLeft){
        this.isLastToTheLeft = isLastToTheLeft;
        this.backInserted = backInserted;
        this.element = element;

    }

    void drawShelf(GL2 gl, Point3D start, Point3D dimens, int index){
        float shelfColor = 0 / 255f;
        float shelfStartX, shelfStartY, shelfStartZ;
        float shelfWidth, shelfHeight, shelfDepth;


        shelfStartX = getLeftSideX(start, isLastToTheLeft) + Config.SLAB_THICKNESS;
        shelfStartY = (float) (start.getY() - (dimens.getY() * ((float)(index) / (element.getTreeShelvesNumber() + 1)) + (1.0f / (element.getTreeShelvesNumber() + 1) * dimens.getY()))  );
        shelfStartZ = (float) (start.getZ() - dimens.getZ() + (backInserted ? Config.SLAB_THICKNESS : 0));

        shelfWidth = element.getShelfSlabFirstDimension();
        shelfHeight = Config.SLAB_THICKNESS;
        shelfDepth = element.getShelfSlabSecondDimension();

        drawCuboid(gl,
                new Point3D(shelfStartX,shelfStartY,shelfStartZ),
                new Point3D(shelfWidth,shelfHeight,shelfDepth),
                shelfColor,
                false
        );

    }
}
