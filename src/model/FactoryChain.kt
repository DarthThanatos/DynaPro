package model

import config.Config
import contract.DynProContract


interface FactoriesChain{
    fun getChain():List<FurnitureFactory>
}

class AllFurnitureTypesChain(private val parentProject: Project): FactoriesChain{
    override fun getChain(): List<FurnitureFactory> = listOf(UpperModulesFactory(parentProject), BottomModulesFactory(parentProject))

}

interface FurnitureFactory{
    fun typeCorrect(type: String): Boolean
    fun createFurnitureChild(name:String): Furniture
    fun createFurnitureChild(furniture: Furniture): Furniture
}

class UpperModulesFactory(private val parentProject: Project): FurnitureFactory{

    override fun createFurnitureChild(furniture: Furniture): UpperModule {
        val res = UpperModule(furniture.name, parentProject)
        res.depth = furniture.depth
        res.height = furniture.height
        res.width = furniture.width
        res.frontUnitPrice = furniture.frontUnitPrice
        res.elementUnitPrice = furniture.elementUnitPrice
        res.backInserted = furniture.backInserted
        res.roofInserted = furniture.roofInserted
        res.frontConfiguration = UpperModuleFrontConfiguration(parentProject, furniture)
        return res
    }

    override fun typeCorrect(type: String): Boolean = type == Config.UPPER_MODULE

    override fun createFurnitureChild(name: String): Furniture = UpperModule(name, parentProject)

}

class BottomModulesFactory(private val parentProject: Project): FurnitureFactory {

    override fun createFurnitureChild(furniture: Furniture): BottomModule {
        val res = BottomModule(furniture.name, parentProject)
        res.depth = furniture.depth
        res.height = furniture.height
        res.width = furniture.width
        res.frontUnitPrice = furniture.frontUnitPrice
        res.elementUnitPrice = furniture.elementUnitPrice
        res.backInserted = furniture.backInserted
        res.roofInserted = furniture.roofInserted
        res.frontConfiguration = BottomModuleFrontConfiguration(parentProject, furniture)
        return res
    }

    override fun typeCorrect(type: String): Boolean = type == Config.BOTTOM_MODULE

    override fun createFurnitureChild(name: String): Furniture = BottomModule(name, parentProject)

}
