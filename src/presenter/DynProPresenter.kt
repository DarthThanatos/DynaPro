package presenter

import contract.DynProContract

class DynProPresenter(
        override val view: DynProContract.View,
        private val furnitureSpecificsPresenter: FurnitureSpecificsPresenter,
        private val projectTreePresenter: ProjectTreePresenter,
        private val metadataPresenter: MetadataPresenter,
        override val model: DynProContract.Model

): DynProContract.Presenter,
        FurnitureSpecificsPresenter by furnitureSpecificsPresenter,
        ProjectTreePresenter by projectTreePresenter,
        MetadataPresenter by metadataPresenter{


    override fun attachView() {
        metadataPresenter.attachView()
        projectTreePresenter.attachView()
        furnitureSpecificsPresenter.attachView()
    }


    override fun onNewProjectCreated() {
        projectTreePresenter.onNewProjectCreated()
        metadataPresenter.onNewProjectCreated()
        furnitureSpecificsPresenter.onNewProjectCreated()
    }

    override fun onFurnitureAdded(addedFurnitureName: String) {
        projectTreePresenter.onFurnitureAdded()
        metadataPresenter.onFurnitureAdded(addedFurnitureName)
        furnitureSpecificsPresenter.onFurnitureAdded(addedFurnitureName)
    }


    override fun onFurnitureRemoved(removedFurnitureName: String) {
        projectTreePresenter.onFurnitureRemoved()
        metadataPresenter.onFurnitureRemoved(removedFurnitureName)
        furnitureSpecificsPresenter.onFurnitureRemoved(removedFurnitureName)
    }


    override fun onFurnitureNameChanged(name: String) {
        metadataPresenter.onFurnitureNameChanged(name)
        projectTreePresenter.onFurnitureNameChanged(name)
        furnitureSpecificsPresenter.onFurnitureNameChanged(name)
    }

    override fun onFurnitureSelected(furnitureName: String) {
        metadataPresenter.onFurnitureSelected(furnitureName)
        furnitureSpecificsPresenter.onFurnitureSelected(furnitureName)
    }




}
