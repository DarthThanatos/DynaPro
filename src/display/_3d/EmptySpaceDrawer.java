package display._3d;

import com.jogamp.opengl.GL2;
import javafx.geometry.Point3D;
import model.ShelfSlabFurnitureTree;

class EmptySpaceDrawer extends ShelfDrawer{

    private final ShelfSlabFurnitureTree element;

    EmptySpaceDrawer(ShelfSlabFurnitureTree element, boolean backInserted, boolean isLastToTheLeft) {
        super(element, backInserted, isLastToTheLeft);
        this.element = element;
    }

    void draw(GL2 gl, Point3D start, Point3D dimens){

        for(int i = 0; i < element.getTreeShelvesNumber(); i++){
            drawShelf(gl, start, dimens, i);
        }
    }
}
