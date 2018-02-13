package presenter

import config.Config
import contract.DynProContract
import main.Binder
import main.frontConfigurationOrientationBinder
import main.furnitureBackOptionsBinder
import main.furnitureRoofOptionsBinder
import model.Furniture
import model.FrontConfigurationVM

interface FurnitureSpecificsPresenter{
    fun attachView()
    fun onAddElementToFrontConfiguration(furnitureName: String?, columnIndex: Int)
    fun onFrontConfigurationElementAdded(furnitureName: String?, newElementIndex: Int)
    fun onRemoveElementFromConfiguration(furnitureName: String?, elementId: String)
    fun onFrontConfigurationElementRemoved(furnitureName: String?, columnIndex: Int)
    fun onFurnitureSelected(furnitureName: String)

    fun onFurnitureNameChanged(name: String)
    fun onNewProjectCreated()
    fun onFurnitureAdded(addedFurnitureName: String)
    fun onFurnitureRemoved(removedFurnitureName: String?)
    fun onFurnitureTypeChanged(furnitureName: String)

}

class DynaProFurnitureSpecificsPresenter(private val dynaProModel: DynProContract.Model, private val view: DynProContract.View): FurnitureSpecificsPresenter{

    private var currentlyDisplayed: Furniture? = null
    private val typeToImgMapper: Map<String, String> = mapOf(Pair(Config.DRAWER_PL, "icons/szuflada_front.png"), Pair(Config.DOOR_PL, "icons/drzwiczki_klamka_lewo.png"), Pair(Config.SHELF_PL, "icons/shelf.png"))

    private fun fetchFrontConfigurationFromFurniture(furniture: Furniture) =
            dynaProModel.currentProject.getFurnitureByName(furniture.name)!!.frontConfiguration

    override fun attachView() {
        onNewProjectCreated()
    }

    private fun registerSubscribers(furniture: Furniture){
        frontConfigurationOrientationBinder.registerSubscriber(Config.CURRENT_FURNITURE , object:Binder.OnChange{
            override fun onChange(value: Any) {
                fetchFrontConfigurationFromFurniture(furniture).columnOriented = value == Config.COLUMN_ORIENTED
            }
        })
        furnitureRoofOptionsBinder.registerSubscriber(Config.CURRENT_FURNITURE, object:Binder.OnChange{
            override fun onChange(value: Any) {
                furniture.roofInserted = value == Config.ROOF_INSERTED
            }
        })
        furnitureBackOptionsBinder.registerSubscriber(Config.CURRENT_FURNITURE, object:Binder.OnChange{
            override fun onChange(value: Any) {
                furniture.backInserted = value == Config.BACK_INSERTED
            }
        })
    }

    private fun onDisplayModelInformation(furniture: Furniture){
        val frontConfiguration = fetchFrontConfigurationFromFurniture(furniture)
        view.displayFrontConfiguration(
                FrontConfigurationVM(furniture.name, frontConfiguration.getConfiguration(), typeToImgMapper),
                if(frontConfiguration.columnOriented) Config.COLUMN_ORIENTED else Config.ROW_ORIENTED
        )
        view.displaySpecifics(
                if(furniture.type == Config.UPPER_MODULE) Config.NO_PEDESTAL else Config.PEDESTAL_EXISTS,
                if(furniture.backInserted) Config.BACK_INSERTED else Config.BACK_HPV,
                if(furniture.roofInserted) Config.ROOF_INSERTED else Config.ROOF_NOT_INSERTED
        )
    }

    private fun onRefreshView(furnitureName: String){
        if(dynaProModel.isProject(furnitureName)) return
        currentlyDisplayed = dynaProModel.getFurnitureByName(furnitureName)
        if(currentlyDisplayed==null) return
        registerSubscribers(currentlyDisplayed!!)
        onDisplayModelInformation(currentlyDisplayed!!)
    }

    override fun onFurnitureSelected(furnitureName: String) {
        onRefreshView(furnitureName)
    }

    override fun onNewProjectCreated() {
        onRefreshView( dynaProModel.defaultFurniture.name) //default furniture is the first and the only one in dynaProModel
    }

    override fun onFurnitureNameChanged(name: String) {
        onRefreshView(name) //set focus on newly created furniture
    }

    override fun onFurnitureAdded(addedFurnitureName: String) {
        onRefreshView(addedFurnitureName)
    }

    override fun onFurnitureRemoved(removedFurnitureName: String?) {
        if(currentlyDisplayed?.name == removedFurnitureName){
            onRefreshView(dynaProModel.defaultFurniture.name)
        }
    }

    override fun onFurnitureTypeChanged(furnitureName: String) {
        onRefreshView(furnitureName)
    }

    override fun onFrontConfigurationElementAdded(furnitureName: String?, newElementIndex: Int) {

    }

    override fun onRemoveElementFromConfiguration(furnitureName: String?, elementId:String) {
        dynaProModel.removeFrontElementFromFurniture(furnitureName, elementId)
    }

    override fun onFrontConfigurationElementRemoved(furnitureName: String?, columnIndex: Int) {

    }

    override fun onAddElementToFrontConfiguration(furnitureName: String?, columnIndex: Int) {

    }


}