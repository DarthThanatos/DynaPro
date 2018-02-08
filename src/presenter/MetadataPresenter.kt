package presenter

import contract.DynProContract
import main.Binder
import main.furnitureNameJTABinder
import main.furnitureWidthSpinnerBinder
import model.Furniture


interface MetadataPresenter{
    fun onMetadataSetSelected(furnitureName: String?)
    fun attachView(dynProView: DynProContract.View)
}

class DynProMetadataPresenter(private val dynProModel: DynProContract.Model) : MetadataPresenter{

    lateinit private var dynProView : DynProContract.View

    override fun attachView(dynProView: DynProContract.View) {
        this.dynProView = dynProView
        onMetadataSetSelected(Config.NEW_UPPER_MODULE_PL)
    }

    override fun onMetadataSetSelected(furnitureName: String?) {
        if(dynProModel.isProject(furnitureName)) return
        val furniture : Furniture = dynProModel.getFurnitureByName(furnitureName)
        dynProView.displayMetadata(
                furniture.type,
                furniture.name,
                furniture.height,
                furniture.width,
                furniture.depth,
                furniture.frontUnitPrice,
                furniture.elementUnitPrice
        )
        furnitureNameJTABinder.registerSubscriber(Config.CURRENT_FURNITURE, object: Binder.OnChange{override fun onChange(value:Any){ furniture.name = value.toString(); System.out.println("meta")}})
//        furnitureWidthSpinnerBinder.registerSubscriber()

    }
}