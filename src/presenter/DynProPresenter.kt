package presenter

import contract.DynProContract
import model.DynProModel
class DynProPresenter: DynProContract.Presenter {

    override fun onDisplayFurnitureMetadata(furnitureProperty: FurnitureProperty) {
        metadataPresenter.onDisplayFurnitureMetadata(furnitureProperty)
    }

    private val dynProModel: DynProContract.Model = DynProModel()
    private val projectTreePresenter: DynProTreePresenter = DynProTreePresenter(dynProModel)
    private val metadataPresenter: MetadataPresenter = DynProMetadataPresenter(dynProModel)
    private var dynProView: DynProContract.View? = null

    override fun attachView(view: DynProContract.View) {
        dynProView = view
        dynProModel.createNewProject()
        metadataPresenter.attachView(view)
        projectTreePresenter.attachView(view)
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
