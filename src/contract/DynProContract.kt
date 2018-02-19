package contract

import model.FrontConfigurationVM
import model.Furniture
import model.Project

import javax.swing.tree.TreeModel

interface DynProContract {

    interface View {
        fun displayProjectPopup()
        fun displayFurniturePopup()
        fun setupProjectTreeModel(treeModel: TreeModel)
        fun promptForUserInput(message: String): String
        fun promptForUserInput(message: String?, initialValue: String?): String
        fun displayMetadata(type: String, name: String, height: Int, width: Int, depth: Int, fronPrice: Int, moduleUnitPrice: Int, pathToImage: String)
        fun displaySpecifics(pedestalOptionText: String, backOptionText: String, roofOptionText: String)
        fun displayFrontConfiguration(frontConfigurationVM: FrontConfigurationVM, configOrientationText: String)
        fun displayFrontConfigElemDialog(furnitureName: String, elementId: String, initialType: String, initialWidth: Int, initialHeight: Int, initialName: String,
                                         widthBlocked: Boolean, heightBlocked: Boolean, growthRingVertically: Boolean, shelvesNumber:Int)
        fun displayFrontConfigurationRowOrientedPopup(elementId: String)
        fun displayFrontConfigurationColumnOrientedPopup(elementId: String)

    }

    interface Presenter {
        fun attachView()
        val view: View
        val model: Model

        fun onModifyFrontConfigElement(furnitureName: String, elementId: String, selectedType: String, width: Int, height: Int, elemName: String, widthBlocked: Boolean, heightBlocked: Boolean, growthRingOrientedVertically: Boolean, shelvesAmount: Int)
        fun getCurrentDisplayedFurnitureName(): String
        fun onFurnitureSelected(furnitureName: String)
        fun onNewProjectCreated()
        fun onProjectRenamed()
        fun onFurnitureAdded(addedFurnitureName: String)
        fun onFurnitureRemoved(removedFurnitureName: String)
        fun onFurnitureNameChanged(name: String)
        fun onFurnitureTypeChanged(furnitureName: String)
        fun onFrontConfigurationChanged(furnitureName: String)
        fun onFrontConfigOrientationChanged(parentFurnitureName: String)
    }

    interface Model {
        fun getCurrentProject(): Project
        fun getDefaultFurniture(): Furniture
        fun getFurnitureByName(name: String): Furniture?
        fun createNewProject(): Project
        fun isProject(name: String): Boolean
        fun renameProject(name: String)
        fun addDefaultFurniture(): Boolean?
        fun renameFurniture(oldName: String, newName: String): Boolean?
        fun removeFurniture(oldChildName: String): Boolean
        fun getFurnitureWithChangedType(name: String, newType: String): Furniture

        fun removeFrontElementFromFurniture(furnitureName: String, elementId: String)
        fun addFrontConfigElementNextTo(furnitureName: String, elementId: String)
        fun addFrontConfigElementBefore(furnitureName: String, elementId: String)
        fun addOneAggregateFrontConfigElemenetNextTo(furnitureName: String, elementId: String)
        fun addMultiFrontConfigElementAggregateNextTo(furnitureName: String, elementId: String)
        fun addOneFrontConfigElementAggregateBefore(furnitureName: String, elementId: String)
        fun addMultiFrontConfigElementAggregateBefore(furnitureName: String, elementId: String)
    }
}
