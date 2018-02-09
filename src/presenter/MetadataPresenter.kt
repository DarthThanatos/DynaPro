package presenter

import contract.DynProContract
import main.Binder
import main.furnitureNameJTABinder
import model.Furniture


interface MetadataPresenter{
    fun onMetadataSetSelected(furnitureName: String?)
    fun attachView()
    fun onDisplayFurnitureMetadata(furniture: Furniture)
    fun onFurnitureNameChanged(name: String)
    fun onNewProject()
    fun onFurnitureListChanged()
}

class DynProMetadataPresenter(private val dynProModel: DynProContract.Model, private var dynProView: DynProContract.View) : MetadataPresenter{

    override fun onFurnitureListChanged() {
        onDisplayFurnitureMetadata(dynProModel.defaultFurniture)
    }

    override fun onNewProject() {
//        onDisplayFurnitureMetadata(dynProModel.defaultFurniture)
        onMetadataSetSelected(Config.NEW_UPPER_MODULE_PL)
    }

    override fun onFurnitureNameChanged(name: String) {
        onDisplayFurnitureMetadata(dynProModel.getFurnitureByName(name))
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
        onMetadataSetSelected(Config.NEW_UPPER_MODULE_PL)
    }

    override fun onMetadataSetSelected(furnitureName: String?) {
        if(dynProModel.isProject(furnitureName)) return
        val furniture : Furniture = dynProModel.getFurnitureByName(furnitureName)
        onDisplayFurnitureMetadata(furniture)
        furnitureNameJTABinder.registerSubscriber(Config.CURRENT_FURNITURE, object: Binder.OnChange{override fun onChange(value:Any){ furniture.name = value.toString()}})
//        furnitureWidthSpinnerBinder.registerSubscriber()

    }
}