package model

import config.Config

interface Element{
    val name: String
    var height: Int
    var width: Int
}

class Shelf(override val name: String = Config.SHELF_PL, override var height: Int = 50, override var width: Int = 25) : Element
class Door(override val name: String = Config.DOOR_PL, override var height: Int = 50, override var width: Int = 25) : Element
class Drawer(override val name: String= Config.DRAWER_PL, override var height: Int = 50, override var width: Int=25): Element