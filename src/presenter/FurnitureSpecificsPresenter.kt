package presenter

import config.Config
import contract.DynProContract
import main.*
import model.Furniture
import model.FrontConfigurationVM

interface FurnitureSpecificsPresenter{
    fun attachView()
    val model: DynProContract.Model
    fun onNewProjectCreated()
    fun onFurnitureRemoved(removedFurnitureName: String)
    fun onFrontConfigurationChanged(furnitureName: String)
    fun onFurnitureTypeChanged(furnitureName: String)
    fun onFrontConfigOrientationChanged(parentFurnitureName: String)
    fun onFurnitureNameChanged(name: String)
    fun onFurnitureAdded(addedFurnitureName: String)
    fun onFurnitureSelected(furnitureName: String)
    fun onModifyFrontConfigElement(furnitureName: String, elementId: String, selectedType: String, width: Int, height: Int, elemName: String)
}

class DynaProFurnitureSpecificsPresenter(override val model: DynProContract.Model, private val view: DynProContract.View): FurnitureSpecificsPresenter{
    override fun onModifyFrontConfigElement(furnitureName: String, elementId: String, selectedType: String, width: Int, height: Int, elemName: String) {
        println("$furnitureName, $elementId, $selectedType, $width, $height, $elemName")
    }

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
                val columnOriented = value == Config.COLUMN_ORIENTED
                if(columnOriented != fetchFrontConfigurationFromFurniture(furniture).columnOriented)
                    fetchFrontConfigurationFromFurniture(furniture).updateOrientation(value == Config.COLUMN_ORIENTED)

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
        changeFrontConfigurationDisplay(furniture)
        view.displaySpecifics(
                if(furniture.type == Config.UPPER_MODULE) Config.NO_PEDESTAL else Config.PEDESTAL_EXISTS,
                if(furniture.backInserted) Config.BACK_INSERTED else Config.BACK_HPV,
                if(furniture.roofInserted) Config.ROOF_INSERTED else Config.ROOF_NOT_INSERTED
        )
    }

    private fun changeFrontConfigurationDisplay(furniture: Furniture){
        val frontConfiguration = fetchFrontConfigurationFromFurniture(furniture)
        view.displayFrontConfiguration(
                FrontConfigurationVM(furniture.name, frontConfiguration.columnOriented, frontConfiguration.getConfiguration(), typeToImgMapper),
                if(frontConfiguration.columnOriented) Config.COLUMN_ORIENTED else Config.ROW_ORIENTED
        )

        frontConfigurationDisplayBinder.registerSubscriber(Config.CURRENT_FURNITURE, object:Binder.OnChange{
            override fun onChange(value: Any) {
                val (popupActionTriggered: Boolean, furnitureName: String, elementId: String) = value as FrontConfDisplayNotifyValue
                if(popupActionTriggered) onChooseFurnitureConfigurationPopup(furnitureName, elementId)
                else println("Clicked $elementId")
            }

        })
    }

    private fun refreshView(furnitureName: String){
        if(model.isProject(furnitureName)) return
        currentlyDisplayed = model.getFurnitureByName(furnitureName)
        if(currentlyDisplayed==null) return
        registerSubscribers(currentlyDisplayed!!)
        onDisplayModelInformation(currentlyDisplayed!!)
    }


    override fun onNewProjectCreated() {
        refreshView( model.getDefaultFurniture().name) //default furniture is the first and the only one in dynaProModel
    }

    override fun onFurnitureRemoved(removedFurnitureName: String) {
        if(currentlyDisplayed?.name == removedFurnitureName){
            refreshView(model.getDefaultFurniture().name)
        }
    }

    override fun onFrontConfigOrientationChanged(parentFurnitureName: String) {
        refreshView(parentFurnitureName)
    }

    override fun onFurnitureTypeChanged(furnitureName: String){
        refreshView(furnitureName)
    }

    override fun onFurnitureSelected(furnitureName: String) {
        refreshView(furnitureName)
    }


    override fun onFurnitureNameChanged(name: String) {
        refreshView(name) //set focus on newly created furniture
    }

    override fun onFurnitureAdded(addedFurnitureName: String) {
        refreshView(addedFurnitureName)
    }

    override fun onFrontConfigurationChanged(furnitureName: String) {
        changeFrontConfigurationDisplay(model.getFurnitureByName(furnitureName)!!)
    }



}