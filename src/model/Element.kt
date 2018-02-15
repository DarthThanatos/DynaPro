package model

import config.Config
import java.util.*

interface Element{
    val name: String
    val type: String
    var height: Int
    var width: Int
    val id: String
}

class Shelf(
        override val name: String = Config.SHELF_PL,
        override var height: Int = 50,
        override var width: Int = 25,
        override val id: String = UUID.randomUUID().toString(),
        override val type: String = Config.SHELF_PL
) : Element, PrintableElement by DefaultPrinter(){
    override fun toString(): String = print(this)
}

class Door(
        override val name: String = Config.DOOR_PL,
        override var height: Int = 50,
        override var width: Int = 25,
        override val id: String = UUID.randomUUID().toString(),
        override val type: String = Config.DOOR_PL
) : Element, PrintableElement by DefaultPrinter(){
    override fun toString(): String = print(this)
}

class Drawer(
        override val name: String= Config.DRAWER_PL,
        override var height: Int = 50,
        override var width: Int=25,
        override val id: String = UUID.randomUUID().toString(),
        override val type: String = Config.DRAWER_PL
): Element, PrintableElement by DefaultPrinter(){
    override fun toString(): String = print(this)
}

interface PrintableElement{
    fun print(element: Element): String
}

class DefaultPrinter: PrintableElement{
    override fun print(element: Element): String = "${element.type}(${element.name}, ${element.id})"

}