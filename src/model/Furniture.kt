package model

import config.Config
import contract.*
import model.slab.*
import kotlin.properties.Delegates


interface Furniture : SlabTree{
    var type: String
    var name: String
    var height: Int
    var width: Int
    var depth: Int
    var frontUnitPrice: Int
    var elementUnitPrice: Int
    val frontConfiguration: FrontConfiguration
    var roofInserted : Boolean
    var backInserted: Boolean
    val hasPedestal: Boolean
    var pedestalHeight: Int
    fun getRoofFirstDimension(): Int
    fun getRoofSecondDimension(): Int
    fun getLeftSkeletonWallFirstDimension(): Int
    fun getLeftSkeletonWallSecondDimension(): Int
    fun getRightSkeletonWallFirstDimension(): Int
    fun getRightSkeletonWallSecondDimension(): Int
    fun getBottomOfSkeletonFirstDimension(): Int
    fun getBottomOfSkeletonSecondDimension(): Int
    fun getPedestalSlabFirstDimension(): Int
    fun getPedestalSlabSecondDimension(): Int
    fun getBackSkeletonSlabSecondDimension(): Int
    fun getBackSkeletonSlabFirstDimension(): Int
    fun savedState():FurnitureSave
    fun getHdfsList(): List<Slab>
}

interface FurnitureSlabTree : SlabTree {

    var furniture: Furniture
    fun getRoofFirstDimension(): Int
    fun getRoofSecondDimension(): Int
    fun getLeftSkeletonWallFirstDimension(): Int
    fun getLeftSkeletonWallSecondDimension(): Int
    fun getRightSkeletonWallFirstDimension(): Int
    fun getRightSkeletonWallSecondDimension(): Int
    fun getBottomOfSkeletonFirstDimension(): Int
    fun getBottomOfSkeletonSecondDimension(): Int
    fun getPedestalSlabFirstDimension(): Int
    fun getPedestalSlabSecondDimension(): Int
    fun getBackSkeletonSlabSecondDimension(): Int
    fun getBackSkeletonSlabFirstDimension(): Int
    fun getHdfsList(): List<Slab>
}

class DefaultFurnitureSlabTree : SlabTree by DefaultSlabTree(), FurnitureSlabTree{

    override lateinit var furniture : Furniture

   override fun getRoofFirstDimension(): Int = RoofSlab(furniture).firstDimension

   override fun getRoofSecondDimension(): Int = RoofSlab(furniture).secondDimension

    override fun getLeftSkeletonWallFirstDimension(): Int = LeftSkeletonWallSlab(furniture).firstDimension

    override fun getLeftSkeletonWallSecondDimension(): Int = LeftSkeletonWallSlab(furniture).secondDimension

    override fun getRightSkeletonWallFirstDimension(): Int = RightSkeletonWallSlab(furniture).firstDimension

    override fun getRightSkeletonWallSecondDimension(): Int = RightSkeletonWallSlab(furniture).secondDimension

    override fun getBottomOfSkeletonFirstDimension(): Int = BottomOfSkeletonSlab(furniture).firstDimension

    override fun getBottomOfSkeletonSecondDimension(): Int = BottomOfSkeletonSlab(furniture).secondDimension

    override fun getPedestalSlabFirstDimension(): Int = PedestalSlab(furniture).firstDimension

    override fun getPedestalSlabSecondDimension(): Int = PedestalSlab(furniture).secondDimension

    override fun getBackSkeletonSlabSecondDimension(): Int = BackSkeletonSlab(furniture).secondDimension

    override fun getBackSkeletonSlabFirstDimension(): Int  = BackSkeletonSlab(furniture).firstDimension

    override fun getHdfsList(): List<Slab> = if(!furniture.backInserted) listOf(BackSkeletonSlab(furniture)) else listOf()

    override fun listOfSlabs(): List<Slab> {
        val listOfSlabs = listOf(
                BottomOfSkeletonSlab(furniture), RoofSlab(furniture), LeftSkeletonWallSlab(furniture), RightSkeletonWallSlab(furniture)
        )
        val pedestalCoveredRes = (if(furniture.hasPedestal)  listOfSlabs +  PedestalSlab(furniture)  else listOfSlabs)
        return if (furniture.backInserted) pedestalCoveredRes + BackSkeletonSlab(furniture) else pedestalCoveredRes
    }
}

class UpperModule(initialName: String, private val parentProject: Project, private val furnitureSlabTree: FurnitureSlabTree = DefaultFurnitureSlabTree(), private val restorableFurniture: RestorableFurniture = DefaultRestorableFurniture())
    : Furniture, FurnitureSlabTree by furnitureSlabTree, RestorableFurniture by restorableFurniture {

    override fun savedState(): FurnitureSave = restorableFurniture.saveState(this)

    override var pedestalHeight: Int = 0

    override val hasPedestal: Boolean = false

    override var roofInserted: Boolean = false

    override var backInserted: Boolean = false

    override var name: String by Delegates.observable(initialName){ _, oldValue, newValue ->  if(newValue != oldValue) parentProject.presenter?.onFurnitureNameChanged(newValue)}

    override var frontConfiguration: FrontConfiguration = UpperModuleFrontConfiguration(parentProject, this)

    override var type: String by Delegates.observable(Config.UPPER_MODULE){ _, _, _ -> parentProject.presenter?.onFurnitureTypeChanged(name) }

    override var frontUnitPrice: Int = 100

    override var elementUnitPrice: Int = 115

    override var height: Int by Delegates.observable(1000){ _, _, _ -> frontConfiguration.recalculateElementsDimens(); parentProject.presenter?.onFrontConfigDimensChanged(name)}

    override var width: Int by Delegates.observable(1500){ _, _, _ -> frontConfiguration.recalculateElementsDimens();  parentProject.presenter?.onFrontConfigDimensChanged(name)}

    override var depth: Int = 500


    init{
        frontConfiguration.recalculateElementsDimens()
        furnitureSlabTree.furniture = this
        furnitureSlabTree.actualSlabTree = this
        addChild("yolo", frontConfiguration)
    }

}

class BottomModule(initialName: String, private val parentProject: Project, private val furnitureSlabTree: FurnitureSlabTree = DefaultFurnitureSlabTree(), private val restorableFurniture: RestorableFurniture = DefaultRestorableFurniture())
    : Furniture, FurnitureSlabTree by furnitureSlabTree, RestorableFurniture by restorableFurniture{

    override fun savedState(): FurnitureSave = restorableFurniture.saveState(this)

    override val hasPedestal: Boolean = true

    override var roofInserted: Boolean = true

    override var backInserted: Boolean = true

    override var name: String by Delegates.observable(initialName){ _, oldValue, newValue ->  if(newValue != oldValue) parentProject.presenter?.onFurnitureNameChanged(newValue)}

    override var pedestalHeight: Int by Delegates.observable(50){_,_,_ -> frontConfiguration.recalculateElementsDimens(); parentProject.presenter?.onPedestalHeightChanged(name)}

    override var frontConfiguration: FrontConfiguration = BottomModuleFrontConfiguration(parentProject, this)

    override var type: String by Delegates.observable(Config.BOTTOM_MODULE){ _, _, _ -> parentProject.presenter?.onFurnitureTypeChanged(name) }

    override var frontUnitPrice : Int = 10

    override var elementUnitPrice : Int  = 10

    override var height : Int by Delegates.observable(1500){ _, _, _ -> frontConfiguration.recalculateElementsDimens(); parentProject.presenter?.onFrontConfigDimensChanged(name)}

    override var width : Int by Delegates.observable(1000){ _, _, _ -> frontConfiguration.recalculateElementsDimens(); parentProject.presenter?.onFrontConfigDimensChanged(name)}

    override var depth : Int = 500

    init{
        frontConfiguration.recalculateElementsDimens()
        furnitureSlabTree.furniture = this
        furnitureSlabTree.actualSlabTree = this
        addChild("yolo", frontConfiguration)
    }
}