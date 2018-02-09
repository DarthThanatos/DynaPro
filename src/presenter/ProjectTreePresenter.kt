package presenter

import contract.DynProContract
import main.Binder
import main.furnitureNameJTABinder
import model.Furniture
import model.Project
import javax.swing.event.ChangeListener
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

interface ProjectTreePresenter {
    fun attachView(view: DynProContract.View)
    fun onProjectTreePopupSelection(name: String?)
    fun onNewFurnitureAdd()
    fun onRenameProject()
    fun onRenameFurniture(oldFurnitureName: String)
    fun onRemoveFurniture(furnitureName: String?)
    fun onNewProject()
}
class DynProTreePresenter(private var dynProModel: DynProContract.Model): ProjectTreePresenter {

    lateinit private var dynProView: DynProContract.View

    private fun changeTree(project: Project){
        dynProView.setupProjectTreeModel(
                transformProjectModelForTree(project)
        )
    }

    private fun transformProjectModelForTree(project: Project): DefaultTreeModel {
        val root = DefaultMutableTreeNode(project.getName().get())
        for(furniture: Furniture in project.furnituresList){
            root.add(DefaultMutableTreeNode(furniture.name.get()))
        }
        return DefaultTreeModel(root)
    }

    override fun attachView(view: DynProContract.View) {
        dynProView = view
        changeTree(dynProModel.currentProject)
        listenToAllFurnitureNames()
    }


    private fun listenToAllFurnitureNames(){
        dynProModel.currentProject.furnituresList.forEach{ it.name.addChangeListener(Config.PROJECT_TREE_SUB, ChangeListener { changeTree(dynProModel.currentProject)})}

    }

    override fun onProjectTreePopupSelection(name: String?) {
        if(dynProModel.isProject(name)) dynProView.displayProjectPopup()
        else dynProView.displayFurniturePopup()
    }


    override fun onNewFurnitureAdd() {
        dynProModel.addDefaultFurniture()
        listenToAllFurnitureNames()
        changeTree(dynProModel.currentProject)
    }


    override fun onRenameProject() {
        dynProModel.renameProject(dynProView.promptForUserInput(Config.GIVE_NEW_PROJECT_NAME_MSG))
        changeTree(dynProModel.currentProject)
    }

    override fun onRenameFurniture(oldFurnitureName: String) {
        val newFurnitureName = dynProView.promptForUserInput(Config.GIVE_NEW_FURNITURE_NAME_MSG)
        dynProModel.renameFurniture(oldFurnitureName, newFurnitureName)
        changeTree(dynProModel.currentProject)
    }

    override fun onRemoveFurniture(furnitureName: String?) {
        dynProModel.removeFurniture(furnitureName)
        changeTree(dynProModel.currentProject)
    }

    override fun onNewProject() {
        changeTree(dynProModel.currentProject)
        listenToAllFurnitureNames()
    }
}