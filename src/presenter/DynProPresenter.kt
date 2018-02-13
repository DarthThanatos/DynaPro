package presenter

import config.Config
import contract.DynProContract
import model.DynProModel

class DynProPresenter(private var dynProView: DynProContract.View): DynProContract.Presenter {

    private val dynProModel: DynProContract.Model = DynProModel(this)
    private val projectTreePresenter: DynProTreePresenter? = DynProTreePresenter(dynProModel, dynProView)
    private val metadataPresenter: MetadataPresenter? = DynProMetadataPresenter(dynProModel, dynProView)
    private val furnitureSpecificsPresenter: FurnitureSpecificsPresenter? = DynaProFurnitureSpecificsPresenter(dynProModel, dynProView)

    override fun attachView() {
        metadataPresenter?.attachView()
        projectTreePresenter?.attachView()
        furnitureSpecificsPresenter?.attachView()
    }

    override fun onCreateNewProject() {
        dynProModel.createNewProject()
    }

    override fun onNewProjectCreated() {
        projectTreePresenter?.onNewProjectCreated()
        metadataPresenter?.onNewProjectCreated()
        furnitureSpecificsPresenter?.onNewProjectCreated()
    }

    override fun onRenameProject() {
        dynProModel.renameProject(dynProView.promptForUserInput(Config.GIVE_NEW_PROJECT_NAME_MSG, dynProModel.currentProject.getName()))
    }

    override fun onProjectRenamed() {
        projectTreePresenter?.onProjectRenamed()
    }

    override fun onAddNewFurniture() {
        dynProModel.addDefaultFurniture()
    }

    override fun onFurnitureAdded(addedFurnitureName: String) {
        projectTreePresenter?.onFurnitureAdded()
        metadataPresenter?.onFurnitureAdded(addedFurnitureName)
        furnitureSpecificsPresenter?.onFurnitureAdded(addedFurnitureName)
    }

    override fun onRemoveFurniture(furnitureName: String?) {
        dynProModel.removeFurniture(furnitureName)
    }

    override fun onFurnitureRemoved(removedFurnitureName: String?) {
        projectTreePresenter?.onFurnitureRemoved()
        metadataPresenter?.onFurnitureRemoved(removedFurnitureName)
        furnitureSpecificsPresenter?.onFurnitureRemoved(removedFurnitureName)
    }

    override fun onRenameProjectTreeFurniture(oldFurnitureName: String) {
        val newFurnitureName = dynProView.promptForUserInput(Config.GIVE_NEW_FURNITURE_NAME_MSG, oldFurnitureName)
        dynProModel.renameFurniture(oldFurnitureName, newFurnitureName)
    }

    override fun onRenameMetadataFurniture() {
        val newFurnitureName = dynProView.promptForUserInput(Config.GIVE_NEW_FURNITURE_NAME_MSG, metadataPresenter?.getCurrentDisplayedFurnitureName())
        dynProModel.renameFurniture( metadataPresenter?.getCurrentDisplayedFurnitureName(), newFurnitureName)

    }

    override fun onFurnitureRenamed(oldFurnitureName: String, newValue: String) {
        metadataPresenter?.onFurnitureNameChanged(newValue)
        projectTreePresenter?.onFurnitureNameChanged()
        furnitureSpecificsPresenter?.onFurnitureNameChanged(newValue)
    }

    override fun onFurnitureTypeChanged(furnitureName: String) {
        furnitureSpecificsPresenter?.onFurnitureTypeChanged(furnitureName)
    }

    override fun onFurnitureSelected(furnitureName: String) {
        metadataPresenter?.onFurnitureSelected(furnitureName)
        furnitureSpecificsPresenter?.onFurnitureSelected(furnitureName)
    }

    override fun onProjectTreePopupSelection(name: String?) { projectTreePresenter?.onProjectTreePopupSelection(name)  }

    override fun onAddElementToFrontConfiguration(furnitureName: String?, columnIndex: Int) {
        furnitureSpecificsPresenter?.onAddElementToFrontConfiguration(furnitureName, columnIndex)
    }

    override fun onFrontConfigurationElementAdded(furnitureName: String?, columnIndex: Int, newElementIndex: Int) {
        furnitureSpecificsPresenter?.onFrontConfigurationElementAdded(furnitureName, newElementIndex)
    }

    override fun onRemoveElementFromConfiguration(furnitureName: String?, columnIndex: Int, elementIndex: Int) {
        furnitureSpecificsPresenter?.onRemoveElementFromConfiguration(furnitureName, columnIndex, elementIndex)
    }

    override fun onFrontConfigurationElementRemoved(furnitureName: String?, columnIndex: Int) {
        furnitureSpecificsPresenter?.onFrontConfigurationElementRemoved(furnitureName, columnIndex)
    }
}
