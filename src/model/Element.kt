package model

import config.Config
import contract.DefaultSlabTree
import contract.SlabTree
import javafx.geometry.Point3D
import model.slab.*
import java.util.*


interface Element : SlabTree{
    var name: String
    val type: String
    var parentConfig: FrontConfiguration
    var height: Int
    var width: Int
    var id: String
    var blockedHeight: Boolean
    var blockedWidth: Boolean
    var growthRingVerticallyOriented: Boolean
    var shelvesNumber: Int
}

interface ElementCommonDefaultsSetter{
    var height: Int
    var width: Int
    var id: String
    var blockedHeight: Boolean
    var blockedWidth: Boolean
    var growthRingVerticallyOriented: Boolean
    var shelvesNumber: Int
}

class DefaultCommonsSetter: ElementCommonDefaultsSetter{
    override var height: Int = 50
    override var width: Int = 25
    override var id: String = UUID.randomUUID().toString()
    override var blockedHeight: Boolean = false
    override var blockedWidth: Boolean = false
    override var growthRingVerticallyOriented: Boolean= true
    override var shelvesNumber: Int = 0


}

class EmptySpace(
        override var name: String = Config.EMPTY_SPACE,
        override val type: String = Config.EMPTY_SPACE, override var parentConfig: FrontConfiguration
) : Element, PrintableElement by DefaultPrinter(), ElementCommonDefaultsSetter by DefaultCommonsSetter(), SlabTree by DefaultSlabTree() {

    override fun toString(): String = print(this)
}


interface ShelfSlabFurnitureTree : SlabTree{
    fun getShelfSlabFirstDimension(): Int
    fun getShelfSlabSecondDimension(): Int
}

open class DefaultShelfSlabFurnitureTree(private val defaultSlabTree: DefaultSlabTree = DefaultSlabTree()) : ShelfSlabFurnitureTree, SlabTree by defaultSlabTree{

    override fun getShelfSlabFirstDimension(): Int = ShelfSlab(element).firstDimension

    override fun getShelfSlabSecondDimension(): Int  = ShelfSlab(element).secondDimension

    lateinit var element: Element

    override fun listOfSlabs(): List<Slab> {
        val slabs = mutableListOf<Slab>()
        for( i in 0 until element.shelvesNumber) slabs.add(ShelfSlab(element))
        return  slabs
    }

}

interface SingleDoorSlabTree : SlabTree, ShelfSlabFurnitureTree{
    fun getDoorSlabSecondDimension() :Int
    fun getDoorSlabFirstDimension() : Int
}

class DefaultSingleDoorSlabTree : SingleDoorSlabTree, DefaultShelfSlabFurnitureTree(){

    override fun getDoorSlabSecondDimension() : Int = DoorFrontSlab(element as SingleDoor).secondDimension

    override fun getDoorSlabFirstDimension() : Int = DoorFrontSlab(element as SingleDoor).firstDimension

    override fun listOfSlabs(): List<Slab> = listOf(DoorFrontSlab(element as SingleDoor)) + super.listOfSlabs()

}

abstract class SingleDoor(private val defaultSingleDoorSlabTree: DefaultSingleDoorSlabTree = DefaultSingleDoorSlabTree())
    : Element, SingleDoorSlabTree by defaultSingleDoorSlabTree{

    init{
        defaultSingleDoorSlabTree.element = this
    }
}

class LeftDoor(
        override var name: String = Config.LEFT_DOOR_PL,
        override val type: String = Config.LEFT_DOOR_PL, override var parentConfig: FrontConfiguration
) : PrintableElement by DefaultPrinter(),  ElementCommonDefaultsSetter by DefaultCommonsSetter(), SingleDoor(){


    override fun toString(): String = print(this)
}

class RightDoor(
        override var name: String = Config.RIGHT_DOOR_PL,
        override val type: String = Config.RIGHT_DOOR_PL, override var parentConfig: FrontConfiguration
): PrintableElement by DefaultPrinter(),  ElementCommonDefaultsSetter by DefaultCommonsSetter(), SingleDoor(){

    override fun toString(): String = print(this)
}

class Drawer(
        override var name: String= Config.DRAWER_PL,
        override val type: String = Config.DRAWER_PL, override var parentConfig: FrontConfiguration
): Element, PrintableElement by DefaultPrinter(),  ElementCommonDefaultsSetter by DefaultCommonsSetter(), SlabTree by DefaultSlabTree(){

    fun getTTrackRawDepth(): Int {
        val aroundGap = 3f
        return (parentConfig.parentFurniture.depth - (if (parentConfig.parentFurniture.backInserted) Config.SLAB_THICKNESS else 0).toDouble() - aroundGap.toDouble()).toInt()
    }


    fun getLeftWallX(startX: Int) : Float = getLeftWallX(Point3D(startX.toDouble(), 0.0, 0.0))

    fun getRightWallX(startX: Int, width: Int) : Float = getRightWallX(Point3D(startX.toDouble(), 0.0, 0.0), Point3D(width.toDouble(), 0.0, 0.0))

    fun getLeftWallX(start: Point3D): Float {
        val lastToTheLeft = parentConfig.isElemWithIdLastToTheLeft(id)
        val distToLeftSide = (if (lastToTheLeft) Config.BETWEEN_ELEMENTS_VERTICAL_GAP else 10).toFloat()
        val leftSideX = (start.x - distToLeftSide).toFloat()
        return leftSideX + Config.SLAB_THICKNESS.toFloat() + 6f
    }


    fun getRightWallX(start: Point3D, dimens: Point3D): Float {
        val isLastToTheRight = parentConfig.isElemWithIdLastToTheRight(id)
        val distToRightSide = (dimens.x + if (isLastToTheRight) Config.BETWEEN_ELEMENTS_VERTICAL_GAP else 10).toFloat()
        val rightSideX = (start.x + distToRightSide).toFloat()
        return rightSideX - Config.SLAB_THICKNESS.toFloat() - 6 / Config.MESH_UNIT - Config.SLAB_THICKNESS.toFloat()
    }


    fun getTTrackEndDepth(): Float
    {
        val t_track_depth = getTTrackRawDepth()
        return (t_track_depth - t_track_depth % 50 - 10).toFloat()
    }

    fun getLeftWallFirstDimension(): Int = DrawerLeftWallSlab(this).firstDimension

    fun getLeftWallSecondDimension(): Int = DrawerLeftWallSlab(this).secondDimension

    fun getRightWallFirstDimension(): Int = DrawerRightWallSlab(this).firstDimension

    fun getRightWallSecondDimension(): Int = DrawerRightWallSlab(this).secondDimension

    fun getBottomSlabSecondDimension(): Int = DrawerBottomSlab(this).secondDimension

    fun getBottomSlabFirstDimenstion(): Int = DrawerBottomSlab(this).firstDimension

    fun getBackSlabSecondDimension(): Int = DrawerBackSlab(this).secondDimension

    fun getBackSlabFirstDimension(): Int = DrawerBackSlab(this).firstDimension

    fun getFrontSlabFirstDimension(): Int = DrawerFrontSlab(this).firstDimension

    fun getFrontSlabSecondDimension(): Int = DrawerFrontSlab(this).secondDimension

    fun getShelfSlabSecondDimension(): Int = DrawerShelfSlab(this).secondDimension

    fun getShelfSlabFirstDimension(): Int = DrawerShelfSlab(this).firstDimension

    override fun listOfSlabs(): List<Slab> = listOf(
            DrawerLeftWallSlab(this), DrawerRightWallSlab(this), DrawerBottomSlab(this),
            DrawerBackSlab(this), DrawerFrontSlab(this), DrawerShelfSlab(this)
    )

    override fun toString(): String = print(this)
}

interface PrintableElement{
    fun print(element: Element): String
}

class DefaultPrinter: PrintableElement{
    override fun print(element: Element): String = "${element.type}(${element.name}, ${element.id})"

}