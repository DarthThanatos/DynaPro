package presenter

import config.Config
import contract.DynProContract
import main.Binder
import main.ProjectTreeNotifyValue
import main.projectTreeBinder
import model.Furniture
import model.Project
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

interface ProjectTreePresenter {
    var parentPresenter : DynProContract.Presenter
    fun attachView()
    fun onProjectRenamed()
    fun onFurnitureNameChanged(name: String)
    fun onNewProjectCreated()
    fun onFurnitureListChanged()
    fun onFurnitureAdded()
    fun onFurnitureRemoved()
}

class DynProTreePresenter(private var dynProModel: DynProContract.Model, private var dynProView: DynProContract.View): ProjectTreePresenter {

    override lateinit var parentPresenter: DynProContract.Presenter

    override fun attachView() {
        projectTreeBinder.registerSubscriber(Config.PROJECT_TREE_SUB, object: Binder.OnChange {
            override fun onChange(value: Any) {
                val ( popupActionTriggered: Boolean,  furnitureName: String) = value as ProjectTreeNotifyValue
                if(popupActionTriggered) onProjectTreePopupSelection(furnitureName)
                else parentPresenter.onFurnitureSelected(furnitureName)
            }

        })
        changeTree(dynProModel.getCurrentProject())
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

    private fun onProjectTreePopupSelection(name: String) {
        if(dynProModel.isProject(name)) dynProView.displayProjectPopup()
        else dynProView.displayFurniturePopup()
    }

    override fun onFurnitureAdded() {
        changeTree(dynProModel.getCurrentProject())
    }

    override fun onFurnitureRemoved() {
        changeTree(dynProModel.getCurrentProject())
    }

    override fun onProjectRenamed() {
        changeTree(dynProModel.getCurrentProject())
    }

    override fun onNewProjectCreated() {
        changeTree(dynProModel.getCurrentProject())
    }

    override fun onFurnitureListChanged() {
        changeTree(dynProModel.getCurrentProject())
    }

    override fun onFurnitureNameChanged(name :String) {
        changeTree(dynProModel.getCurrentProject())
    }



}