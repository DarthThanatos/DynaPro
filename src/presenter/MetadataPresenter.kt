package presenter

import config.Config
import contract.DynProContract
import main.*
import model.Furniture


interface MetadataPresenter{
    fun getCurrentDisplayedFurnitureName(): String
    fun onFurnitureSelected(furnitureName: String)
    fun attachView()
    fun onFurnitureNameChanged(name: String)
    fun onNewProjectCreated()
    fun onFurnitureAdded(addedFurnitureName: String)
    fun onFurnitureRemoved(removedFurnitureName: String?)
}

class DynProMetadataPresenter(private val dynProModel: DynProContract.Model, private var dynProView: DynProContract.View) : MetadataPresenter{

    private var currentlyDisplayed: Furniture? = null
    private val typeToImgPath : Map<String, String> = mapOf(Pair(Config.UPPER_MODULE,"icons/szafa_2d.jpg"), Pair(Config.BOTTOM_MODULE, "icons/module.png"))
    private val typeEngToPolMap: Map<String, String> = mapOf(Pair(Config.UPPER_MODULE, Config.UPPER_MODULE_PL), Pair(Config.BOTTOM_MODULE, Config.BOTTOM_MODULE_PL))

    override fun getCurrentDisplayedFurnitureName(): String = currentlyDisplayed?.name!!

    private fun onRefreshView(furnitureName: String){
        if(dynProModel.isProject(furnitureName)) return
        currentlyDisplayed = dynProModel.getFurnitureByName(furnitureName)
        if(currentlyDisplayed == null) return
        registerSubscribers(currentlyDisplayed!!)
        onDisplayFurnitureMetadata(currentlyDisplayed!!)
    }

    override fun onFurnitureSelected(furnitureName: String) {
        onRefreshView(furnitureName)
    }

    private fun registerSubscribers(furniture: Furniture){
        furnitureNameJTABinder.registerSubscriber(Config.CURRENT_FURNITURE, object: Binder.OnChange{
            override fun onChange(value:Any){
                furniture.name = value.toString()
            }
        })
        furnitureWidthSpinnerBinder.registerSubscriber(Config.CURRENT_FURNITURE, object: Binder.OnChange{
            override fun onChange(value: Any) {
                furniture.width = value as Int
            }
        })
        furnitureHeightSpinnerBinder.registerSubscriber(Config.CURRENT_FURNITURE, object : Binder.OnChange{
            override fun onChange(value: Any) {
                furniture.height = value as Int }
        })
        furnitureDepthSpinnerBinder.registerSubscriber(Config.CURRENT_FURNITURE, object : Binder.OnChange{
            override fun onChange(value: Any) {
                furniture.depth = value as Int
            }
        })
        furnitureFrontPriceSpinnerBinder.registerSubscriber(Config.CURRENT_FURNITURE, object : Binder.OnChange{
            override fun onChange(value: Any) {
                furniture.frontUnitPrice = value as Int
            }
        })
        furnitureModuleUnitPriceSpinnerBinder.registerSubscriber(Config.CURRENT_FURNITURE, object : Binder.OnChange{
            override fun onChange(value: Any) {
                furniture.elementUnitPrice = value as Int
            }
        })
        furnitureTypeComboBinder.registerSubscriber(Config.CURRENT_FURNITURE, object: Binder.OnChange{
            override fun onChange(value: Any) {
                val translatedVal = typeEngToPolMap.filter{entry -> entry.value == value }.keys.single()
                if(furniture.type != translatedVal) {
                    val furnitureWithChangedType = dynProModel.getFurnitureWithChangedType(furniture.name, translatedVal)
                    onRefreshView(furnitureWithChangedType.name)
                    furnitureWithChangedType.type = translatedVal   // must be here to activate the observer
                }
            }
        })
    }

    private fun onDisplayFurnitureMetadata(furniture: Furniture) {
        dynProView.displayMetadata(
                typeEngToPolMap[furniture.type],
                furniture.name,
                furniture.height,
                furniture.width,
                furniture.depth,
                furniture.frontUnitPrice,
                furniture.elementUnitPrice,
                typeToImgPath[furniture.type]
        )
    }

    override fun attachView() {
        onNewProjectCreated()
    }

    override fun onNewProjectCreated() {
        onRefreshView( dynProModel.defaultFurniture.name) //default furniture is the first and the only one in model
    }


    override fun onFurnitureAdded(addedFurnitureName: String) {
        onRefreshView(addedFurnitureName)
    }

    override fun onFurnitureRemoved(removedFurnitureName: String?) {
        if(currentlyDisplayed?.name == removedFurnitureName){
            onRefreshView(dynProModel.defaultFurniture.name)
        }
    }


    override fun onFurnitureNameChanged(name: String) {
        onRefreshView(name) //set focus on newly created furniture
    }

}