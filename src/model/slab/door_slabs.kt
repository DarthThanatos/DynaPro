package model.slab

import config.Config
import model.DoubleDoor
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

class DoubleDoorSingleSlab(private val doubleDoor: DoubleDoor, override val name: String): FrontSlab{
    override val scaleboard: ArrayList<Boolean>
        get() = arrayListOf(true, true, true, true)
    override val firstDimension: Int
        get() = if(doubleDoor.growthRingVerticallyOriented) doubleDoor.height else doubleDoor.getSingleDoorWidth()
    override val secondDimension: Int
        get() = if(doubleDoor.growthRingVerticallyOriented)  doubleDoor.getSingleDoorWidth() else doubleDoor.height

}