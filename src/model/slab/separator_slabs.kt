package model.slab

import config.Config
import model.ArrangementAggregate
import util.SlabSidePositionUtil
import java.util.ArrayList

class ColumnOrientedAggregateSeparatorSlab(private val parentAggregate: ArrangementAggregate, index: Int) : Slab {
    override val firstDimension: Int
        get() {
            val absoluteStartY = 0
            val configuration = parentAggregate.parentConfiguration
            val currentElement = parentAggregate[0]
            val separatorStartY = SlabSidePositionUtil.getTopSlabY(absoluteStartY, configuration.isElemWithIdLastToTheBottom(currentElement.id)) - Config.SLAB_THICKNESS;
            return (separatorStartY - SlabSidePositionUtil.getBottomSlabY(absoluteStartY,  parentAggregate.getAggregateHeightWithGaps(true), true)).toInt()
        }

    override val secondDimension: Int
        get() {
            val furniture = parentAggregate.parentConfiguration.parentFurniture
            return SlabSidePositionUtil.calculateElementMaxDepth(furniture.depth.toDouble(), furniture.backInserted).toInt()
        }

    override val name: String = Config.SEPARATOR + index
    override val scaleboard: ArrayList<Boolean> = arrayListOf(false, secondDimension > firstDimension, firstDimension > secondDimension, false)
}

class ColumnOrientedElementSeparatorSlab(private val parentAggregate: ArrangementAggregate, index: Int) : Slab {
    override val name: String = Config.SEPARATOR + index

    override val scaleboard: ArrayList<Boolean> = arrayListOf(false,secondDimension > firstDimension, firstDimension > secondDimension, false)

    override val firstDimension: Int
        get(){
            val absoluteStartX = 0
            val currentElement = parentAggregate[0]
            val configuration = parentAggregate.parentConfiguration
            return (SlabSidePositionUtil.getRightSideX(absoluteStartX, currentElement.width, configuration.isElemWithIdLastToTheRight(currentElement.id))
                    - SlabSidePositionUtil.getLeftSideX(absoluteStartX, configuration.isElemWithIdLastToTheLeft(currentElement.id)) - (2 * Config.SLAB_THICKNESS)).toInt();
        }

    override val secondDimension: Int
        get() {
            val furniture = parentAggregate.parentConfiguration.parentFurniture
            return SlabSidePositionUtil.calculateElementMaxDepth(furniture.depth.toDouble(), furniture.backInserted).toInt()
        }
}

class RowOrientedAggregateSeparatorSlab(private val parentAggregate: ArrangementAggregate, index: Int): Slab {
    override val name: String = Config.SEPARATOR + index
    override val scaleboard: ArrayList<Boolean> = arrayListOf(false, secondDimension > firstDimension, firstDimension > secondDimension, false)
    override val firstDimension: Int
        get(){
            val absoluteStartX = 0
            return (SlabSidePositionUtil.getRightSideX(absoluteStartX, parentAggregate.getAggregateWidthWithGaps(false), true) -
                    SlabSidePositionUtil.getLeftSideX(absoluteStartX, true) - (2 * Config.SLAB_THICKNESS)).toInt()
        }
    override val secondDimension: Int
        get() {
            val furniture = parentAggregate.parentConfiguration.parentFurniture
            return SlabSidePositionUtil.calculateElementMaxDepth(furniture.depth.toDouble(), furniture.backInserted).toInt()
        }
}

class RowOrientedElementSeparatorSlab(private val parentAggregate: ArrangementAggregate, index: Int) : Slab {
    override val name: String = Config.SEPARATOR + index

    override val scaleboard: ArrayList<Boolean> = arrayListOf(false, secondDimension > firstDimension, firstDimension > secondDimension, false)

    override val firstDimension: Int
        get() {
            val absoluteStartY = 0
            val configuration = parentAggregate.parentConfiguration
            val currentElement = parentAggregate[0]
            val separatorStartY = SlabSidePositionUtil.getTopSlabY(absoluteStartY, configuration.isElemWithIdLastToTheBottom(currentElement.id)) - Config.SLAB_THICKNESS
            return (separatorStartY - SlabSidePositionUtil.getBottomSlabY(absoluteStartY,  currentElement.height, configuration.isElemWithIdLastToTheBottom(currentElement.id))).toInt()
        }

    override val secondDimension: Int
        get() {
            val furniture = parentAggregate.parentConfiguration.parentFurniture
            return SlabSidePositionUtil.calculateElementMaxDepth(furniture.depth.toDouble(), furniture.backInserted).toInt()
        }
}

