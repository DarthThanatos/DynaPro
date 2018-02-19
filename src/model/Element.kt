package model

import config.Config
import java.util.*

interface Element{
    var name: String
    val type: String
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

class Shelf(
        override var name: String = Config.SHELF_PL,
        override val type: String = Config.SHELF_PL
) : Element, PrintableElement by DefaultPrinter(), ElementCommonDefaultsSetter by DefaultCommonsSetter(){

    override fun toString(): String = print(this)
}

class LeftDoor(
        override var name: String = Config.LEFT_DOOR_PL,
        override val type: String = Config.LEFT_DOOR_PL
) : Element, PrintableElement by DefaultPrinter(),  ElementCommonDefaultsSetter by DefaultCommonsSetter(){

    override fun toString(): String = print(this)
}

class RightDoor(
        override var name: String = Config.RIGHT_DOOR_PL,
        override val type: String = Config.RIGHT_DOOR_PL
) : Element, PrintableElement by DefaultPrinter(),  ElementCommonDefaultsSetter by DefaultCommonsSetter(){

    override fun toString(): String = print(this)
}

class Drawer(
        override var name: String= Config.DRAWER_PL,
        override val type: String = Config.DRAWER_PL
): Element, PrintableElement by DefaultPrinter(),  ElementCommonDefaultsSetter by DefaultCommonsSetter(){

    override fun toString(): String = print(this)
}

interface PrintableElement{
    fun print(element: Element): String
}

class DefaultPrinter: PrintableElement{
    override fun print(element: Element): String = "${element.type}(${element.name}, ${element.id})"

}