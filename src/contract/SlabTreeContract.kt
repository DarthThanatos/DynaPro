package contract

import model.slab.FrontSlab
import model.slab.Slab
import java.awt.Dimension


interface SlabTree{
    var actualSlabTree: SlabTree
    fun getTreeSlabList(): List<Slab>
    fun addChild(childIdentifier: String, slabTree: SlabTree)
    fun getSpecificChildSlabList(childIdentifier:String): List<Slab>
    fun removeChild(childIdentifier: String)
    fun listOfSlabs(): List<Slab>
    fun resetChildren()
    fun slabsGroupedBySize(listOfSlabs: List<Slab>): Map<Dimension, List<Slab>>
    fun changeChildId(oldId: String, newId: String)
    var children: MutableMap<String, SlabTree>
    fun getCutLength(listOfSlabs: List<Slab>): Int
    fun getScaleBoardLength(listOfSlabs: List<Slab>): Int
    fun getAssessment(frontPrice: Int, commonSlabPrice: Int, listOfSlabs: List<Slab>): Int
}

open class DefaultSlabTree: SlabTree {

    override lateinit var actualSlabTree: SlabTree

    override fun listOfSlabs(): List<Slab> {
        return listOf<Slab>()
    }

    override fun changeChildId(oldId: String, newId: String) {
        val child = children.remove(oldId)!!
        children[newId] = child
    }

    override var children: MutableMap<String, SlabTree> = mutableMapOf()


    override fun resetChildren() {
        children = mutableMapOf()
    }

    override fun removeChild(childIdentifier: String) {
        children.remove(childIdentifier)
    }

    override fun addChild(childIdentifier: String, slabTree: SlabTree) {
        children[childIdentifier] = slabTree
    }

    override fun getSpecificChildSlabList(childIdentifier: String): List<Slab> =
        children.filter { entry -> entry.key == childIdentifier }.values.single().getTreeSlabList()

    override fun getTreeSlabList(): List<Slab>  {
        return actualSlabTree.listOfSlabs() + children.values.map { it.getTreeSlabList() }.flatMap { it }
    }


    override fun slabsGroupedBySize(listOfSlabs: List<Slab>): Map<Dimension, List<Slab>> =
            listOfSlabs.groupBy { Dimension(it.firstDimension, it.secondDimension) }



    override fun getCutLength(listOfSlabs: List<Slab>): Int = (listOfSlabs.sumBy {
        2 *it.firstDimension + 2 * it.secondDimension
    } * 0.8).toInt()

    override fun getScaleBoardLength(listOfSlabs: List<Slab>): Int =
            (listOfSlabs.sumBy {
                val northFirstDimension = it.firstDimension >= it.secondDimension

                ((if(it.scaleboard[0]) 1 else 0) * if(northFirstDimension)it.firstDimension else it.secondDimension) +
                        ((if(it.scaleboard[1]) 1 else 0) * if(northFirstDimension) it.secondDimension else it.firstDimension) +
                        ((if(it.scaleboard[2]) 1 else 0) * if(northFirstDimension)it.firstDimension else it.secondDimension) +
                        ((if(it.scaleboard[3]) 1 else 0) * if(northFirstDimension)it.secondDimension else it.firstDimension)

            } * 0.8).toInt()


    override fun getAssessment(frontPrice: Int, commonSlabPrice: Int, listOfSlabs: List<Slab>): Int {
        val (fronts, commonSlabs) = listOfSlabs.partition { it is FrontSlab }
        return (fronts.sumBy { it.firstDimension * it.secondDimension } * frontPrice + commonSlabs.sumBy { it.firstDimension * it.secondDimension } * commonSlabPrice) / (1000 * 1000)
    }

}