package model

import config.Config
import contract.DynProContract


interface FactoriesChain{
    fun getChain():List<FurnitureFactory>
}

class AllFurnitureTypesChain(private val presenter: DynProContract.Presenter): FactoriesChain{
    override fun getChain(): List<FurnitureFactory> = listOf(UpperModulesFactory(presenter), BottomModulesFactory(presenter))

}

interface FurnitureFactory{
    fun typeCorrect(type: String): Boolean
    fun  createFurnitureChild(name:String): Furniture
    fun createFurnitureChild(furniture: Furniture): Furniture
}

class UpperModulesFactory(private val presenter: DynProContract.Presenter): FurnitureFactory{

    override fun createFurnitureChild(furniture: Furniture): UpperModule {
        val res = UpperModule(furniture.name, presenter)
        res.depth = furniture.depth
        res.height = furniture.height
        res.width = furniture.width
        res.frontUnitPrice = furniture.frontUnitPrice
        res.elementUnitPrice = furniture.elementUnitPrice
        res.frontConfiguration = UpperModuleFrontConfiguration(presenter, furniture.frontConfiguration)
        return res
    }

    override fun typeCorrect(type: String): Boolean = type == Config.UPPER_MODULE

    override fun createFurnitureChild(name: String): Furniture = UpperModule(name, presenter)

}

class BottomModulesFactory(private val presenter: DynProContract.Presenter): FurnitureFactory {

    override fun createFurnitureChild(furniture: Furniture): BottomModule {
        val res = BottomModule(furniture.name, presenter)
        res.depth = furniture.depth
        res.height = furniture.height
        res.width = furniture.width
        res.frontUnitPrice = furniture.frontUnitPrice
        res.elementUnitPrice = furniture.elementUnitPrice
        res.frontConfiguration = BottomModuleFrontConfiguration(presenter, furniture.frontConfiguration)
        return res
    }

    override fun typeCorrect(type: String): Boolean = type == Config.BOTTOM_MODULE

    override fun createFurnitureChild(name: String): Furniture = BottomModule(name, presenter)

}
