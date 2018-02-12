package presenter

import contract.DynProContract

interface FurnitureSpecificsPresenter{
    fun onAddElementToFrontConfiguration(furnitureName: String?, columnIndex: Int)
    fun onFrontConfigurationElementAdded(furnitureName: String?, newElementIndex: Int)
    fun onRemoveElementFromConfiguration(furnitureName: String?, columnIndex: Int, elementIndex: Int)
    fun onFrontConfigurationElementRemoved(furnitureName: String?, columnIndex: Int)

}

class DynaProFurnitureSpecificsPresenter(private val model: DynProContract.Model, private val view: DynProContract.View): FurnitureSpecificsPresenter{

    override fun onFrontConfigurationElementAdded(furnitureName: String?, newElementIndex: Int) {

    }

    override fun onRemoveElementFromConfiguration(furnitureName: String?, columnIndex: Int, elementIndex: Int) {

    }

    override fun onFrontConfigurationElementRemoved(furnitureName: String?, columnIndex: Int) {

    }

    override fun onAddElementToFrontConfiguration(furnitureName: String?, columnIndex: Int) {

    }

}