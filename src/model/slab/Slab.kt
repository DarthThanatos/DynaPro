package model.slab

import java.util.ArrayList


interface Slab{
    val name: String
    val scaleboard: ArrayList<Boolean>
    val firstDimension: Int
    val secondDimension: Int
}

interface FrontSlab: Slab