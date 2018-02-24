package util;

import config.Config;
import javafx.geometry.Point3D;

public class SlabSidePositionUtil {

    public static float getLeftSideX(Point3D start, boolean isLastToTheLeft){
        float distToLeftSide = (isLastToTheLeft ? Config.BETWEEN_ELEMENTS_VERTICAL_GAP : 10);
        return  (float) (start.getX() - distToLeftSide);
    }

    public static float getLeftSideX(int startX, boolean isLastToTheLeft){
        float distToLeftSide = (isLastToTheLeft ? Config.BETWEEN_ELEMENTS_VERTICAL_GAP : 10);
        return  (startX - distToLeftSide);
    }

    public static float getRightSideX(Point3D start, Point3D dimens, boolean isLastToTheRight){
        float distToRightSide = (float) (dimens.getX() + (isLastToTheRight ? Config.BETWEEN_ELEMENTS_VERTICAL_GAP : 10));
        return (float) (start.getX() + distToRightSide);

    }

    public static float getRightSideX(int startX, int width, boolean isLastToTheRight){
        float distToRightSide = (float) (width + (isLastToTheRight ? Config.BETWEEN_ELEMENTS_VERTICAL_GAP : 10));
        return (startX + distToRightSide);

    }

    public static float getTopSlabY(Point3D start, boolean isLastToTheTop){
        float distToTop =  isLastToTheTop ? Config.BETWEEN_ELEMENTS_HORIZONTAL_GAP  : 8;
        return (float) (start.getY() + distToTop);
    }

    public static float getTopSlabY(int startY, boolean isLastToTheTop){
        float distToTop =  isLastToTheTop ? Config.BETWEEN_ELEMENTS_HORIZONTAL_GAP  : 8;
        return (startY + distToTop);
    }

    public static float getBottomSlabY(Point3D start, Point3D dimens, boolean isLastToTheBottom){
        float distToBottom = (float) (dimens.getY() - (isLastToTheBottom ? Config.SLAB_THICKNESS - Config.BETWEEN_ELEMENTS_HORIZONTAL_GAP :  8));
        return (float) (start.getY() - distToBottom);
    }

    public static float getBottomSlabY(int startY, int height, boolean isLastToTheBottom){
        float distToBottom = (float) (height - (isLastToTheBottom ? Config.SLAB_THICKNESS - Config.BETWEEN_ELEMENTS_HORIZONTAL_GAP :  8));
        return  (startY - distToBottom);
    }

    public static float calculateFrontZ(double currentZ, double furniturDepth, boolean backInserted){
        return (float) (currentZ - furniturDepth + (backInserted ? Config.SLAB_THICKNESS : 0));
    }
    public static float calculateElementMaxDepth(double furnitureDepth, boolean backInserted){
        return (float) (furnitureDepth - (backInserted ? Config.SLAB_THICKNESS : 0));
    }

}
