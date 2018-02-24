package model.slab

import config.Config
import model.Element
import model.Furniture
import util.SlabSidePositionUtil
import java.util.ArrayList

class ShelfSlab(private val element: Element) : Slab {
    override val name: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val scaleboard: ArrayList<Boolean>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val firstDimension: Int
        get() {
            val startAbsoluteX = 0
            val isLastToTheLeft = element.parentConfig.isElemWithIdLastToTheLeft(element.id)
            val isLastToTheRight = element.parentConfig.isElemWithIdLastToTheRight(element.id)
            return (SlabSidePositionUtil.getRightSideX(startAbsoluteX, element.width, isLastToTheRight) - SlabSidePositionUtil.getLeftSideX(startAbsoluteX, isLastToTheLeft) - (2 * Config.SLAB_THICKNESS)).toInt()
        }
    override val secondDimension: Int
        get() {
            val furniture: Furniture = element.parentConfig.parentFurniture
            return (furniture.depth - if (furniture.backInserted) Config.SLAB_THICKNESS else 0).toInt()
        }
}