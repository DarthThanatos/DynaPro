package presenter

import config.Config
import contract.DynProContract
import main.*
import model.Furniture
import model.FrontConfigurationVM

interface FurnitureSpecificsPresenter{
    fun attachView()
    val model: DynProContract.Model
    fun onFurnitureSelected(furnitureName: String)
    fun onFurnitureNameChanged(name: String)
    fun onNewProjectCreated()
    fun onFurnitureAdded(addedFurnitureName: String)
    fun onFurnitureRemoved(removedFurnitureName: String)
    fun onFurnitureTypeChanged(furnitureName: String)

}

class DynaProFurnitureSpecificsPresenter(override val model: DynProContract.Model, private val view: DynProContract.View): FurnitureSpecificsPresenter{

    private var currentlyDisplayed: Furniture? = null
    private val typeToImgMapper: Map<String, String> = mapOf(Pair(Config.DRAWER_PL, "icons/szuflada_front.png"), Pair(Config.DOOR_PL, "icons/drzwiczki_klamka_lewo.png"), Pair(Config.SHELF_PL, "icons/shelf.png"))

    private fun fetchFrontConfigurationFromFurniture(furniture: Furniture) =
            model.getCurrentProject().getFurnitureByName(furniture.name)!!.frontConfiguration

    override fun attachView() {
        onNewProjectCreated()
    }


    private fun onChooseFurnitureConfigurationPopup(furnitureName: String, elementId: String) {
        if(model.getFurnitureByName(furnitureName)?.frontConfiguration!!.columnOriented)
            view.displayFrontConfigurationColumnOrientedPopup(elementId)
        else view.displayFrontConfigurationRowOrientedPopup(elementId)
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

        frontConfigurationDisplayBinder.registerSubscriber(Config.CURRENT_FURNITURE, object:Binder.OnChange{
            override fun onChange(value: Any) {
                val (popupActionTriggered: Boolean, furnitureName: String, elementId: String) = value as FrontConfDisplayNotifyValue
                if(popupActionTriggered) onChooseFurnitureConfigurationPopup(furnitureName, elementId)
                else println("Clicked ${elementId}")
            }

        })
        view.displaySpecifics(
                if(furniture.type == Config.UPPER_MODULE) Config.NO_PEDESTAL else Config.PEDESTAL_EXISTS,
                if(furniture.backInserted) Config.BACK_INSERTED else Config.BACK_HPV,
                if(furniture.roofInserted) Config.ROOF_INSERTED else Config.ROOF_NOT_INSERTED
        )
    }

    private fun onRefreshView(furnitureName: String){
        if(model.isProject(furnitureName)) return
        currentlyDisplayed = model.getFurnitureByName(furnitureName)
        if(currentlyDisplayed==null) return
        registerSubscribers(currentlyDisplayed!!)
        onDisplayModelInformation(currentlyDisplayed!!)
    }


    override fun onNewProjectCreated() {
        onRefreshView( model.getDefaultFurniture().name) //default furniture is the first and the only one in dynaProModel
    }

    override fun onFurnitureRemoved(removedFurnitureName: String) {
        if(currentlyDisplayed?.name == removedFurnitureName){
            onRefreshView(model.getDefaultFurniture().name)
        }
    }


    override fun onFurnitureSelected(furnitureName: String) {
        onRefreshView(furnitureName)
    }


    override fun onFurnitureNameChanged(name: String) {
        onRefreshView(name) //set focus on newly created furniture
    }

    override fun onFurnitureAdded(addedFurnitureName: String) {
        onRefreshView(addedFurnitureName)
    }


    override fun onFurnitureTypeChanged(furnitureName: String) {
        onRefreshView(furnitureName)
    }





}