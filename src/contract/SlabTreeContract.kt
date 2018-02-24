package contract

import model.slab.Slab


interface SlabTree{
    fun getTreeSlabList(): List<Slab>
    fun addChild(childIdentifier: String, slabTree: SlabTree)
    fun getSpecificChildSlabList(childIdentifier:String): List<Slab>
    fun removeChild(childIdentifier: String)
    fun listOfSlabs(): List<Slab>
}

open class DefaultSlabTree: SlabTree {

    override fun listOfSlabs(): List<Slab> = listOf<Slab>()

    val children: MutableMap<String, SlabTree> = mutableMapOf()

    override fun removeChild(childIdentifier: String) {
        children.remove(childIdentifier)
    }

    override fun addChild(childIdentifier: String, slabTree: SlabTree) {
        children[childIdentifier] = slabTree
    }

    override fun getSpecificChildSlabList(childIdentifier: String): List<Slab> =
            children.filter { entry -> entry.key == childIdentifier }.values.single().getTreeSlabList()

    override fun getTreeSlabList(): List<Slab> =
            listOfSlabs() + children.values.map { it.getTreeSlabList() }.flatMap { it }



}