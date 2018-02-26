package model.slab

import config.Config
import model.Drawer
import util.SlabSidePositionUtil
import java.util.ArrayList

class DrawerLeftWallSlab(private val drawer: Drawer) : Slab {
    override val name: String = Config.DRAWER_LEFT
    override val scaleboard: ArrayList<Boolean> = arrayListOf(false, false, false, false)
    override val firstDimension: Int
        get() {
            val topGap = 10
            val lastToTheBottom = drawer.parentConfig.isElemWithIdLastToTheBottom(drawer.id)
            return (drawer.height - topGap - Config.SLAB_THICKNESS) - if (lastToTheBottom) Config.SLAB_THICKNESS else 0
        }
    //        leftWallHeight = (float) (dimens.getY() - topGap - Config.SLAB_THICKNESS) - (lastToTheBottom ?  Config.SLAB_THICKNESS : 0)- (float) (getTopSlabY(start, configuration.isElemWithIdLastToTheTop(drawer.getId())) - start.getY());
    //        leftWallStartY = getTopSlabY(start, configuration.isElemWithIdLastToTheTop(drawer.getId())) - Config.SLAB_THICKNESS - topGap;


    override val secondDimension: Int
        get() = drawer.getTTrackEndDepth().toInt()
}

class DrawerRightWallSlab(private val drawer: Drawer): Slab {

    override val name: String = Config.DRAWER_RIGHT

    override val scaleboard: ArrayList<Boolean> = arrayListOf(false, false, false, false)

    override val firstDimension: Int
        get() {
            val topGap = 10
            val lastToTheBottom = drawer.parentConfig.isElemWithIdLastToTheBottom(drawer.id)
            return (drawer.height - topGap - Config.SLAB_THICKNESS) - if (lastToTheBottom) Config.SLAB_THICKNESS else 0}
    //        rightWallHeight = (float) (dimens.getY() - topGap - Config.SLAB_THICKNESS) - (lastToTheBottom ? Config.SLAB_THICKNESS : 0) - (float) (getTopSlabY(start, configuration.isElemWithIdLastToTheTop(drawer.getId())) - start.getY());
    //        rightWallStartY = getTopSlabY(start, configuration.isElemWithIdLastToTheTop(drawer.getId())) - Config.SLAB_THICKNESS - topGap;

    override val secondDimension: Int
        get() = drawer.getTTrackEndDepth().toInt()

}

class DrawerBottomSlab(private val drawer: Drawer): Slab{
    override val name: String = Config.DRAWER_BOTTOM
    override val scaleboard: ArrayList<Boolean> = arrayListOf(false, false, false, false)
    override val firstDimension: Int
        get() = drawer.getTTrackEndDepth().toInt()
    //        bottomStartY = (float) (start.getY() - dimens.getY() + Config.SLAB_THICKNESS + 12 + 2*Config.SLAB_THICKNESS) + (lastToTheBottom ? Config.SLAB_THICKNESS: 0) + (float) (getTopSlabY(start, configuration.isElemWithIdLastToTheTop(drawer.getId())) - start.getY());

    override val secondDimension: Int
        get() = (drawer.getRightWallX(0, drawer.width) - drawer.getLeftWallX(0) - Config.SLAB_THICKNESS).toInt()

}

class DrawerBackSlab(private val drawer: Drawer): Slab{
    override val name: String = Config.DRAWER_BACK
    override val scaleboard: ArrayList<Boolean> = arrayListOf(false, false, false, false)
    override val firstDimension: Int
        get() {
            val lastToTheBottom = drawer.parentConfig.isElemWithIdLastToTheBottom(drawer.id)
            return (drawer.height - Config.SLAB_THICKNESS - 10 - (2 * Config.SLAB_THICKNESS) - 12) - if (lastToTheBottom) Config.SLAB_THICKNESS else 0
        }
    //        backStartY = getTopSlabY(start, configuration.isElemWithIdLastToTheTop(drawer.getId())) - Config.SLAB_THICKNESS - topGap;
    //        backHeight = (float) (dimens.getY() - Config.SLAB_THICKNESS - 10 - 2 *Config.SLAB_THICKNESS - 12) - (lastToTheBottom ? Config.SLAB_THICKNESS : 0) - (float) (getTopSlabY(start, configuration.isElemWithIdLastToTheTop(drawer.getId())) - start.getY());

    override val secondDimension: Int
        get() {
            return (drawer.getRightWallX(0, drawer.width) - drawer.getLeftWallX(0) - Config.SLAB_THICKNESS).toInt()
        }

}


class DrawerFrontSlab(private val drawer: Drawer): FrontSlab {
    override val name: String = Config.DRAWER_FRONT
    override val scaleboard: ArrayList<Boolean> = arrayListOf(true, true, true, true)
    override val firstDimension: Int
        get() = if(drawer.growthRingVerticallyOriented)drawer.height else drawer.width
    override val secondDimension: Int
        get() = if(drawer.growthRingVerticallyOriented)drawer.height else drawer.width

}

class DrawerShelfSlab(private val drawer: Drawer): Slab{
    override val name: String = Config.DRAWER_SHELF
    override val scaleboard: ArrayList<Boolean> = arrayListOf(false, false, true, false)
    override val firstDimension: Int
        get() = SlabSidePositionUtil.calculateElementMaxDepth(drawer.parentConfig.parentFurniture.depth.toDouble(), drawer.parentConfig.parentFurniture.backInserted).toInt()
    override val secondDimension: Int
        get() {
            val isLastToTheRight = drawer.parentConfig.isElemWithIdLastToTheRight(drawer.id)
            val lastToTheLeft = drawer.parentConfig.isElemWithIdLastToTheLeft(drawer.id)
            return (SlabSidePositionUtil.getRightSideX(0, drawer.width, isLastToTheRight) - SlabSidePositionUtil.getLeftSideX(0, lastToTheLeft) - (2 * Config.SLAB_THICKNESS)).toInt()
        }
}