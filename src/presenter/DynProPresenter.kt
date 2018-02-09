package presenter

import contract.DynProContract
import model.DynProModel
import model.Furniture

class DynProPresenter(private var dynProView: DynProContract.View): DynProContract.Presenter {

    private val dynProModel: DynProContract.Model = DynProModel(this)
    private val projectTreePresenter: DynProTreePresenter = DynProTreePresenter(dynProModel, dynProView)
    private val metadataPresenter: MetadataPresenter = DynProMetadataPresenter(dynProModel, dynProView)

    override fun attachView() {
        dynProModel.createNewProject()
        metadataPresenter.attachView()
        projectTreePresenter.attachView()
    }

    override fun onFurnituresListChange() {
        projectTreePresenter?.onFurnitureListChanged()
        metadataPresenter?.onFurnitureListChanged()
    }

    override fun onProjectChanged() {
        projectTreePresenter.onNewProject()
        metadataPresenter.onNewProject()
    }

    override fun onProjectNameChanged() {
//        projectTreePresenter.onRenameProject()
    }

    override fun onFurnitureNameChanged(oldFurnitureName: String, newValue: String) {
        metadataPresenter.onFurnitureNameChanged(newValue)
        projectTreePresenter.onFurnitureNameChanged()
    }

    override fun onFurnitureTypeChanged(newValue: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFrontUnitPriceChanged(newValue: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onElementUnitPriceChanged(newValue: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFurnitureHeightChanged(newValue: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFurnitureWidthChanged(newValue: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFurnitureDepthChanged(newValue: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDisplayFurnitureMetadata(furniture: Furniture) {
        metadataPresenter.onDisplayFurnitureMetadata(furniture)
    }


    override fun onProjectTreePopupSelection(name: String?) { projectTreePresenter.onProjectTreePopupSelection(name)  }

    override fun onNewFurnitureAdd() { projectTreePresenter.onNewFurnitureAdd()  }

    override fun onRenameProject() { projectTreePresenter.onRenameProject() }

    override fun onRenameFurniture(oldFurnitureName: String) { projectTreePresenter.onRenameFurniture(oldFurnitureName)  }

    override fun onRemoveFurniture(furnitureName: String?) { projectTreePresenter.onRemoveFurniture(furnitureName)  }

    override fun onNewProject() {
        dynProModel.createNewProject()
        projectTreePresenter.onNewProject()
    }

    override fun onMetadataSetSelected(furnitureName: String?) { metadataPresenter.onMetadataSetSelected(furnitureName) }
}
