package model.slab

import config.Config
import model.Furniture
import java.util.ArrayList

class RoofSlab(private val furniture: Furniture) : Slab {
    override val name: String = Config.ROOF

    override val scaleboard: ArrayList<Boolean> = if (furniture.roofInserted) arrayListOf(false, false, false, false) else  arrayListOf(true, true, true, true)

    override val firstDimension: Int
        get() = if (furniture.roofInserted) (furniture.width - 2 * Config.SLAB_THICKNESS) else furniture.width

    override val secondDimension: Int
        get() = furniture.depth
}

class LeftSkeletonWallSlab(private val furniture: Furniture) : Slab {
    override val name: String = Config.LEFT_SKELETON_WALL
    override val scaleboard: ArrayList<Boolean> = arrayListOf(false, false, true, false)
    override val firstDimension: Int
        get() = (furniture.height - (if (furniture.roofInserted) 0 else Config.SLAB_THICKNESS) - (if (furniture.hasPedestal) 0 else if (furniture.roofInserted) 0 else Config.SLAB_THICKNESS))
    override val secondDimension: Int
        get() = furniture.depth
}


class RightSkeletonWallSlab(private val furniture: Furniture): Slab {
    override val name: String = Config.RIGHT_SKELETON_WALL
    override val scaleboard: ArrayList<Boolean> = arrayListOf(false, false, true, false)
    override val firstDimension: Int
        get() = (furniture.height - (if (furniture.roofInserted) 0 else Config.SLAB_THICKNESS) - (if (furniture.hasPedestal) 0 else if (furniture.roofInserted) 0 else Config.SLAB_THICKNESS))
    override val secondDimension: Int
        get() = furniture.depth

}

class BottomOfSkeletonSlab(private val furniture: Furniture): Slab{
    override val name: String = Config.BOTTOM_OF_SKELETON
    override val scaleboard: ArrayList<Boolean> = if(furniture.hasPedestal) arrayListOf(false, false, false, false) else if (furniture.roofInserted) arrayListOf(false, false, false, false) else  arrayListOf(true, true, true, true)
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
    override val name: String = Config.PEDESTAL
    override val scaleboard: ArrayList<Boolean> = arrayListOf(false, false, false, false)
    override val firstDimension: Int
        get() = (furniture.width - 2 * Config.SLAB_THICKNESS)
    override val secondDimension: Int
        get() = furniture.pedestalHeight

}

class BackSkeletonSlab( private val furniture: Furniture): Slab{
    override val name: String = Config.BACK
    override val scaleboard: ArrayList<Boolean> = arrayListOf(false, false, false, false)
    override val firstDimension: Int
        get() = if (furniture.backInserted)
            furniture.height - (2 * Config.SLAB_THICKNESS) - (if (furniture.hasPedestal) furniture.pedestalHeight else 0)
        else
            furniture.height - if (furniture.hasPedestal) furniture.pedestalHeight else 0

    override val secondDimension: Int
        get() = if (furniture.backInserted) (furniture.width - 2 * Config.SLAB_THICKNESS) else furniture.width

}