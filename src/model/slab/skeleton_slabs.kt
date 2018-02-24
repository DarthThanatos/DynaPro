package model.slab

import config.Config
import model.Furniture
import java.util.ArrayList

class RoofSlab(private val furniture: Furniture) : Slab {
    override val name: String = Config.ROOF

    override val scaleboard: ArrayList<Boolean>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val firstDimension: Int
        get() = if (furniture.roofInserted) (furniture.width - 2 * Config.SLAB_THICKNESS) else furniture.width

    override val secondDimension: Int
        get() = furniture.depth
}

class LeftSkeletonWallSlab(private val furniture: Furniture) : Slab {
    override val name: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val scaleboard: ArrayList<Boolean>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val firstDimension: Int
        get() = (furniture.height - (if (furniture.roofInserted) 0 else Config.SLAB_THICKNESS) - (if (furniture.hasPedestal) 0 else if (furniture.roofInserted) 0 else Config.SLAB_THICKNESS))
    override val secondDimension: Int
        get() = furniture.depth
}


class RightSkeletonWallSlab(private val furniture: Furniture): Slab {
    override val name: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val scaleboard: ArrayList<Boolean>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val firstDimension: Int
        get() = (furniture.height - (if (furniture.roofInserted) 0 else Config.SLAB_THICKNESS) - (if (furniture.hasPedestal) 0 else if (furniture.roofInserted) 0 else Config.SLAB_THICKNESS))
    override val secondDimension: Int
        get() = furniture.depth

}

class BottomOfSkeletonSlab(private val furniture: Furniture): Slab{
    override val name: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val scaleboard: ArrayList<Boolean>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val firstDimension: Int
        get() =
            if (furniture.hasPedestal)
                (furniture.width - 2 * Config.SLAB_THICKNESS)
            else
                (if (furniture.roofInserted) furniture.width - 2 * Config.SLAB_THICKNESS else furniture.width)

    override val secondDimension: Int
        get() = furniture.depth

}

class PedestalSlab( private val furniture: Furniture): Slab{
    override val name: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val scaleboard: ArrayList<Boolean>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val firstDimension: Int
        get() = (furniture.width - 2 * Config.SLAB_THICKNESS)
    override val secondDimension: Int
        get() = furniture.pedestalHeight;

}

class BackSkeletonSlab( private val furniture: Furniture): Slab{
    override val name: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val scaleboard: ArrayList<Boolean>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val firstDimension: Int
        get() = if (furniture.backInserted)
            furniture.height - (2 * Config.SLAB_THICKNESS) - (if (furniture.hasPedestal) furniture.pedestalHeight else 0)
        else
            furniture.height - if (furniture.hasPedestal) furniture.pedestalHeight else 0

    override val secondDimension: Int
        get() = if (furniture.backInserted) (furniture.width - 2 * Config.SLAB_THICKNESS) else furniture.width

}