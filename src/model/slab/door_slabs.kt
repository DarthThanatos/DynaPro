package model.slab

import config.Config
import model.SingleDoor
import java.util.ArrayList

class DoorFrontSlab(private val door: SingleDoor) : FrontSlab {
    override val name: String = Config.DOOR
    override val scaleboard: ArrayList<Boolean> = arrayListOf(true, true, true, true)
    override val firstDimension: Int
        get() = if(door.growthRingVerticallyOriented)door.height else door.width
    override val secondDimension: Int
        get() = if(door.growthRingVerticallyOriented) door.width else door.height
}