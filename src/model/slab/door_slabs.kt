package model.slab

import model.SingleDoor
import java.util.ArrayList

class DoorFrontSlab(private val door: SingleDoor) : Slab {
    override val name: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val scaleboard: ArrayList<Boolean>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val firstDimension: Int
        get() = door.height
    override val secondDimension: Int
        get() = door.width
}