package presenter

import config.Config
import contract.DynProContract
import model.DynProModel
import model.Furniture

class DynProPresenter(private var dynProView: DynProContract.View): DynProContract.Presenter {

    private val dynProModel: DynProContract.Model = DynProModel(this)
    private val projectTreePresenter: DynProTreePresenter? = DynProTreePresenter(dynProModel, dynProView)
    private val metadataPresenter: MetadataPresenter? = DynProMetadataPresenter(dynProModel, dynProView)

    override fun attachView() {
        metadataPresenter?.attachView()
        projectTreePresenter?.attachView()
    }

    override fun onCreateNewProject() {
        dynProModel.createNewProject()
    }

    override fun onNewProjectCreated() {
        projectTreePresenter?.onNewProjectCreated()
        metadataPresenter?.onNewProjectCreated()
    }

    override fun onRenameProject() {
        dynProModel.renameProject(dynProView.promptForUserInput(Config.GIVE_NEW_PROJECT_NAME_MSG))
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
    }

    override fun onRemoveFurniture(furnitureName: String?) {
        dynProModel.removeFurniture(furnitureName)
    }

    override fun onFurnitureRemoved(removedFurnitureName: String?) {
        projectTreePresenter?.onFurnitureRemoved()
        metadataPresenter?.onFurnitureRemoved(removedFurnitureName)
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
    }


    override fun onDisplayFurnitureMetadata(furniture: Furniture) {
        metadataPresenter?.onDisplayFurnitureMetadata(furniture)
    }

    override fun onMetadataSetSelected(furnitureName: String) {
        metadataPresenter?.onMetadataSetSelected(furnitureName)
    }

    override fun onProjectTreePopupSelection(name: String?) { projectTreePresenter?.onProjectTreePopupSelection(name)  }

    override fun onFurnitureTypeChanged(newValue: String) {
    }

    override fun onFrontUnitPriceChanged(newValue: Int) {
    }

    override fun onElementUnitPriceChanged(newValue: Int) {
    }

    override fun onFurnitureHeightChanged(newValue: Int) {
    }

    override fun onFurnitureWidthChanged(newValue: Int) {
    }

    override fun onFurnitureDepthChanged(newValue: Int) {
    }
}
