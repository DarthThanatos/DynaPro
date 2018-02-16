package model

import config.Config
import contract.FactoriesChain
import contract.TypedFactory

class AllFurnitureTypesChain(private val parentProject: Project): FactoriesChain<FurnitureFactory> {
    override fun getChain(): List<FurnitureFactory> = listOf(UpperModulesFactory(parentProject), BottomModulesFactory(parentProject))
}

interface FurnitureFactory: TypedFactory{
    fun createFurnitureChild(name:String): Furniture
    fun createFurnitureChild(furniture: Furniture): Furniture
}

interface FurniturePropertiesSetter{
    fun copied(newFurniture: Furniture, oldFurniture: Furniture) : Furniture
}

class DefaultFurniturePropertiesCopier : FurniturePropertiesSetter{
    override fun copied(newFurniture: Furniture, oldFurniture: Furniture) : Furniture {
        newFurniture.depth = oldFurniture.depth
        newFurniture.height = oldFurniture.height
        newFurniture.width = oldFurniture.width
        newFurniture.frontUnitPrice = oldFurniture.frontUnitPrice
        newFurniture.elementUnitPrice = oldFurniture.elementUnitPrice
        newFurniture.backInserted = oldFurniture.backInserted
        newFurniture.roofInserted = oldFurniture.roofInserted
        return newFurniture
    }

}

class UpperModulesFactory(private val parentProject: Project): FurnitureFactory, FurniturePropertiesSetter by DefaultFurniturePropertiesCopier(){

    override fun createFurnitureChild(furniture: Furniture): Furniture =
            copied(UpperModule(furniture.name, parentProject), furniture)

    override fun typeCorrect(type: String): Boolean = type == Config.UPPER_MODULE

    override fun createFurnitureChild(name: String): Furniture = UpperModule(name, parentProject)

}

class BottomModulesFactory(private val parentProject: Project): FurnitureFactory, FurniturePropertiesSetter by DefaultFurniturePropertiesCopier() {

    override fun createFurnitureChild(furniture: Furniture): Furniture =
            copied(BottomModule(furniture.name, parentProject), furniture)

    override fun typeCorrect(type: String): Boolean = type == Config.BOTTOM_MODULE

    override fun createFurnitureChild(name: String): Furniture = BottomModule(name, parentProject)

}
