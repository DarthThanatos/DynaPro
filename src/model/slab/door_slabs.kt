package model.slab

import config.Config
import model.SingleDoor
import java.util.ArrayList

class DoorFrontSlab(private val door: SingleDoor) : Slab {
    override val name: String = Config.DOOR
    override val scaleboard: ArrayList<Boolean> = arrayListOf(true, true, true, true)
    override val firstDimension: Int
        get() = door.height
    override val secondDimension: Int
        get() = door.width
}