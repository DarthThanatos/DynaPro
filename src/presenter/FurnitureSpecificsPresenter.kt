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
    fun onModifyFrontConfigElement(furnitureName: String, elementId: String, selectedType: String, width: Int, height: Int, elemName: String, widthBlocked: Boolean, heightBlocked: Boolean, growthRingVertical: Boolean, shelvesNumber: Int)
    fun onFrontConfigDimensChanged(furnitureName: String)
    fun onPedestalHeightChanged(name: String)
}

class DynaProFurnitureSpecificsPresenter(override val model: DynProContract.Model, private val view: DynProContract.View): FurnitureSpecificsPresenter{

    private var currentlyDisplayed: Furniture? = null
    private val typeToImgMapper: Map<String, String> = mapOf(
            Pair(Config.DRAWER_PL, "icons/szuflada_front.png"),
            Pair(Config.LEFT_DOOR_PL, "icons/drzwiczki_klamka_prawo.png"),
            Pair(Config.RIGHT_DOOR_PL, "icons/drzwiczki_klamka_lewo.png"),
            Pair(Config.SHELF_PL, "icons/shelf.png")
    )

    private fun fetchFrontConfigurationFromFurniture(furniture: Furniture) = fetchFrontConfigurationHavingName(furniture.name)

    private fun fetchFrontConfigurationHavingName(furnitureName: String) =
            model.getCurrentProject().getFurnitureByName(furnitureName)!!.frontConfiguration

    private fun fetchConfigElem(furnitureName: String, elementId: String) =
            fetchFrontConfigurationHavingName(furnitureName).fetchElementWithId(elementId)

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
        pedestalHeightDisplayBinder.registerSubscriber(Config.CURRENT_FURNITURE, object : Binder.OnChange{
            override fun onChange(value: Any) {
                println( furniture.frontConfiguration.getBlockedHeight())
                if(furniture.hasPedestal and (furniture.height - furniture.frontConfiguration.getBlockedHeight() >= value as Int))
                    furniture.pedestalHeight = value
                else refreshView(furniture.name)
            }
        })
    }

    private fun onDisplayModelInformation(furniture: Furniture){
        changeFrontConfigurationDisplay(furniture)
        view.displaySpecifics(
                if(!furniture.hasPedestal) Config.NO_PEDESTAL else Config.PEDESTAL_EXISTS,
                if(furniture.backInserted) Config.BACK_INSERTED else Config.BACK_HPV,
                if(furniture.roofInserted) Config.ROOF_INSERTED else Config.ROOF_NOT_INSERTED,
                furniture.pedestalHeight
        )
    }

    private fun changeFrontConfigurationDisplay(furniture: Furniture){
        val frontConfiguration = fetchFrontConfigurationFromFurniture(furniture)
        view.displayFrontConfiguration(
                FrontConfigurationVM(furniture.name, frontConfiguration.columnOriented, frontConfiguration.getConfiguration(), typeToImgMapper, furniture.width, furniture.height),
                if(frontConfiguration.columnOriented) Config.COLUMN_ORIENTED else Config.ROW_ORIENTED
        )

        frontConfigurationDisplayBinder.registerSubscriber(Config.CURRENT_FURNITURE, object:Binder.OnChange{
            override fun onChange(value: Any) {
                val (popupActionTriggered: Boolean, furnitureName: String, elementId: String) = value as FrontConfDisplayNotifyValue
                if(popupActionTriggered) onChooseFurnitureConfigurationPopup(furnitureName, elementId)
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

    override fun onModifyFrontConfigElement(furnitureName: String, elementId: String, selectedType: String, width: Int, height: Int, elemName: String, widthBlocked: Boolean, heightBlocked: Boolean, growthRingVertical: Boolean, shelvesNumber: Int) {
        val configElem = fetchConfigElem(furnitureName, elementId)
        val shouldNotifyViewAboutChange =
                (configElem.width != width) or (configElem.height != height) or  (configElem.name != elemName) or (configElem.type != selectedType) or
                (configElem.blockedWidth != widthBlocked) or (configElem.blockedHeight != heightBlocked) or
                (configElem.growthRingVerticallyOriented != growthRingVertical) or (configElem.shelvesNumber != shelvesNumber)

        if(configElem.blockedWidth != widthBlocked) {
            val frontConfig = fetchFrontConfigurationHavingName(furnitureName)
            configElem.blockedWidth = widthBlocked
            if(frontConfig.columnOriented)frontConfig.propagateAggregateSpecificBlock(elementId, widthBlocked)
            frontConfig.recalculateElementsDimens()
        }
        if(configElem.blockedHeight != heightBlocked) {
            val frontConfig = fetchFrontConfigurationHavingName(furnitureName)
            configElem.blockedHeight = heightBlocked
            if(!frontConfig.columnOriented) frontConfig.propagateAggregateSpecificBlock(elementId, heightBlocked)
            frontConfig.recalculateElementsDimens()
        }
        if(configElem.width != width) {
            val frontConfig = fetchFrontConfigurationHavingName(furnitureName)
            configElem.width = width
            if(frontConfig.columnOriented)frontConfig.propagateAggragateSpecificDimen(elementId)
            frontConfig.recalculateElementsDimens()
        }
        if(configElem.height != height) {
            val frontConfig = fetchFrontConfigurationHavingName(furnitureName)
            configElem.height = height
            if(!frontConfig.columnOriented)frontConfig.propagateAggragateSpecificDimen(elementId)
            frontConfig.recalculateElementsDimens()
        }
        if(configElem.name != elemName) configElem.name = elemName
        if(configElem.growthRingVerticallyOriented != growthRingVertical) configElem.growthRingVerticallyOriented = growthRingVertical
        if(configElem.shelvesNumber != shelvesNumber) configElem.shelvesNumber = shelvesNumber
        if(configElem.type != selectedType) fetchFrontConfigurationHavingName(furnitureName).changeTypeOfElem(elementId, selectedType)
        if(shouldNotifyViewAboutChange) refreshView(furnitureName)
    }

    override fun onFrontConfigDimensChanged(furnitureName: String) {
        refreshView(furnitureName)
    }

    override fun onPedestalHeightChanged(name: String) {
        refreshView(name)
    }

}