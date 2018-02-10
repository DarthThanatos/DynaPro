package presenter

import config.Config
import contract.DynProContract
import main.*
import model.Furniture


interface MetadataPresenter{
    fun getCurrentDisplayedFurnitureName(): String
    fun onMetadataSetSelected(furnitureName: String)
    fun attachView()
    fun onDisplayFurnitureMetadata(furniture: Furniture)
    fun onFurnitureNameChanged(name: String)
    fun onNewProjectCreated()
    fun onFurnitureAdded(addedFurnitureName: String)
    fun onFurnitureRemoved(removedFurnitureName: String?)
}

class DynProMetadataPresenter(private val dynProModel: DynProContract.Model, private var dynProView: DynProContract.View) : MetadataPresenter{

    private var currentlyDisplayed: Furniture? = null

    override fun getCurrentDisplayedFurnitureName(): String = currentlyDisplayed?.name!!

    override fun onMetadataSetSelected(furnitureName: String) {
        if(dynProModel.isProject(furnitureName)) return
        currentlyDisplayed = dynProModel.getFurnitureByName(furnitureName)
        if(currentlyDisplayed == null) return
        registerSubscribers(currentlyDisplayed!!)
        onDisplayFurnitureMetadata(currentlyDisplayed!!)

    }

    private fun registerSubscribers(furniture: Furniture){
        furnitureNameJTABinder.registerSubscriber(Config.CURRENT_FURNITURE, object: Binder.OnChange{override fun onChange(value:Any){ furniture.name = value.toString()}})
        furnitureWidthSpinnerBinder.registerSubscriber(Config.CURRENT_FURNITURE, object: Binder.OnChange{ override fun onChange(value: Any) {furniture.width = value as Int}})
        furnitureHeightSpinnerBinder.registerSubscriber(Config.CURRENT_FURNITURE, object : Binder.OnChange{ override fun onChange(value: Any) { furniture.height = value as Int } })
        furnitureDepthSpinnerBinder.registerSubscriber(Config.CURRENT_FURNITURE, object : Binder.OnChange{ override fun onChange(value: Any) { furniture.depth = value as Int } })
        furnitureFrontPriceSpinnerBinder.registerSubscriber(Config.CURRENT_FURNITURE, object : Binder.OnChange{ override fun onChange(value: Any) { furniture.frontUnitPrice = value as Int } })
        furnitureModuleUnitPriceSpinnerBinder.registerSubscriber(Config.CURRENT_FURNITURE, object : Binder.OnChange{ override fun onChange(value: Any) { furniture.elementUnitPrice = value as Int } })
        furnitureTypeComboBinder.registerSubscriber(Config.CURRENT_FURNITURE, object: Binder.OnChange{ override fun onChange(value: Any) {onMetadataSetSelected(dynProModel.getFurnitureWithChangedType(furniture.name, value as String).name)} })
    }

    override fun onDisplayFurnitureMetadata(furniture: Furniture) {
        dynProView.displayMetadata(
                furniture.type,
                furniture.name,
                furniture.height,
                furniture.width,
                furniture.depth,
                furniture.frontUnitPrice,
                furniture.elementUnitPrice
        )
    }

    override fun attachView() {
        onNewProjectCreated()
    }

    override fun onNewProjectCreated() {
        onMetadataSetSelected( dynProModel.defaultFurniture.name) //default furniture is the first and the only one in model
    }


    override fun onFurnitureAdded(addedFurnitureName: String) {
        onMetadataSetSelected(addedFurnitureName)
    }

    override fun onFurnitureRemoved(removedFurnitureName: String?) {
        if(currentlyDisplayed?.name == removedFurnitureName){
            onMetadataSetSelected(dynProModel.defaultFurniture.name)
        }
    }


    override fun onFurnitureNameChanged(name: String) {
        onMetadataSetSelected(name) //set focus on newly created furniture
    }

}