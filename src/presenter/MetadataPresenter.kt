package presenter

import contract.DynProContract
import main.Binder
import main.furnitureNameJTABinder
import javax.swing.event.ChangeListener


interface MetadataPresenter{
    fun onMetadataSetSelected(furnitureName: String?)
    fun attachView(dynProView: DynProContract.View)
    fun onDisplayFurnitureMetadata(furnitureProperty: FurnitureProperty)
}

class DynProMetadataPresenter(private val dynProModel: DynProContract.Model) : MetadataPresenter{

    lateinit private var dynProView : DynProContract.View

    override fun onDisplayFurnitureMetadata(furnitureProperty: FurnitureProperty) {
        dynProView.displayMetadata(
                furnitureProperty.type.get(),
                furnitureProperty.name.get(),
                furnitureProperty.height.get(),
                furnitureProperty.width.get(),
                furnitureProperty.depth.get(),
                furnitureProperty.frontUnitPrice.get(),
                furnitureProperty.elementUnitPrice.get()
        )
    }

    override fun attachView(dynProView: DynProContract.View) {
        this.dynProView = dynProView
        onDisplayFurnitureMetadata(dynProModel.defaultFurniture)
        dynProModel.currentProject.addChangeListener(Config.META_PRESENTER_SUB, ChangeListener { onDisplayFurnitureMetadata(dynProModel.defaultFurniture) })
    }

    override fun onMetadataSetSelected(furnitureName: String?) {
        if(dynProModel.isProject(furnitureName)) return
        val furniture : FurnitureProperty = dynProModel.getFurnitureByName(furnitureName)
        onDisplayFurnitureMetadata(furniture)
        furnitureNameJTABinder.registerSubscriber(Config.CURRENT_FURNITURE, object: Binder.OnChange{override fun onChange(value:Any){ furniture.name.set(value.toString()); System.out.println("meta")}})
//        furnitureWidthSpinnerBinder.registerSubscriber()

    }
}