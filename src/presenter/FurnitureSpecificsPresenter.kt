package presenter

import config.Config
import contract.DynProContract
import main.Binder
import main.frontConfigurationOrientationBinder
import model.Furniture
import model.FrontConfigurationVM

interface FurnitureSpecificsPresenter{
    fun attachView()
    fun onAddElementToFrontConfiguration(furnitureName: String?, columnIndex: Int)
    fun onFrontConfigurationElementAdded(furnitureName: String?, newElementIndex: Int)
    fun onRemoveElementFromConfiguration(furnitureName: String?, columnIndex: Int, elementIndex: Int)
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

    private fun registerSubscriber(furniture: Furniture){
        val frontConfiguration = fetchFrontConfigurationFromFurniture(furniture)
        frontConfigurationOrientationBinder.registerSubscriber(Config.CURRENT_FURNITURE , object:Binder.OnChange{
            override fun onChange(value: Any) {
                frontConfiguration.columnOriented = value == Config.COLUMN_ORIENTED
            }
        })
    }

    private fun displayFrontInfo(furniture: Furniture){
        val frontConfiguration = fetchFrontConfigurationFromFurniture(furniture)
        view.displayFrontConfiguration(
                FrontConfigurationVM(frontConfiguration.getConfiguration(), typeToImgMapper),
                if(frontConfiguration.columnOriented) Config.COLUMN_ORIENTED else Config.ROW_ORIENTED
        )
        view.displaySpecificsPanel(furniture.type)
    }

    override fun onFurnitureSelected(furnitureName: String) {
        if(dynaProModel.isProject(furnitureName)) return
        currentlyDisplayed = dynaProModel.getFurnitureByName(furnitureName)
        if(currentlyDisplayed==null) return
        registerSubscriber(currentlyDisplayed!!)
        displayFrontInfo(currentlyDisplayed!!)
    }

    override fun onNewProjectCreated() {
        onFurnitureSelected( dynaProModel.defaultFurniture.name) //default furniture is the first and the only one in dynaProModel
    }

    override fun onFurnitureNameChanged(name: String) {
        onFurnitureSelected(name) //set focus on newly created furniture
    }

    override fun onFurnitureAdded(addedFurnitureName: String) {
        onFurnitureSelected(addedFurnitureName)
    }

    override fun onFurnitureRemoved(removedFurnitureName: String?) {
        if(currentlyDisplayed?.name == removedFurnitureName){
            onFurnitureSelected(dynaProModel.defaultFurniture.name)
        }
    }

    override fun onFurnitureTypeChanged(furnitureName: String) {
        onFurnitureSelected(furnitureName)
    }

    override fun onFrontConfigurationElementAdded(furnitureName: String?, newElementIndex: Int) {

    }

    override fun onRemoveElementFromConfiguration(furnitureName: String?, columnIndex: Int, elementIndex: Int) {

    }

    override fun onFrontConfigurationElementRemoved(furnitureName: String?, columnIndex: Int) {

    }

    override fun onAddElementToFrontConfiguration(furnitureName: String?, columnIndex: Int) {

    }


}