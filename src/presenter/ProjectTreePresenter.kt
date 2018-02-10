package presenter

import contract.DynProContract
import model.Furniture
import model.Project
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

interface ProjectTreePresenter {
    fun attachView()
    fun onProjectTreePopupSelection(name: String?)
    fun onProjectRenamed()
    fun onFurnitureNameChanged()
    fun onNewProjectCreated()
    fun onFurnitureListChanged()
    fun onFurnitureAdded()
    fun onFurnitureRemoved()
}

class DynProTreePresenter(private var dynProModel: DynProContract.Model, private var dynProView: DynProContract.View): ProjectTreePresenter {

    override fun attachView() {
        changeTree(dynProModel.currentProject)
    }

    private fun changeTree(project: Project){
        dynProView.setupProjectTreeModel(
                transformProjectModelForTree(project)
        )
    }

    private fun transformProjectModelForTree(project: Project): DefaultTreeModel {
        val root = DefaultMutableTreeNode(project.getName())
        for(furniture: Furniture in project.furnituresList){
            root.add(DefaultMutableTreeNode(furniture.name))
        }
        return DefaultTreeModel(root)
    }

    override fun onProjectTreePopupSelection(name: String?) {
        if(dynProModel.isProject(name)) dynProView.displayProjectPopup()
        else dynProView.displayFurniturePopup()
    }

    override fun onFurnitureAdded() {
        changeTree(dynProModel.currentProject)
    }

    override fun onFurnitureRemoved() {
        changeTree(dynProModel.currentProject)
    }

    override fun onProjectRenamed() {
        changeTree(dynProModel.currentProject)
    }

    override fun onNewProjectCreated() {
        changeTree(dynProModel.currentProject)
    }

    override fun onFurnitureListChanged() {
        changeTree(dynProModel.currentProject)
    }

    override fun onFurnitureNameChanged() {
        changeTree(dynProModel.currentProject)
    }



}