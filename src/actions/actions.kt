package actions

import display.ProjectTree
import main.DynProMain
import java.awt.event.ActionEvent
import javax.swing.AbstractAction


class NewFurnitureAction(private val dynProMain: DynProMain) : AbstractAction() {

    override fun actionPerformed(e: ActionEvent) {
        dynProMain.presenter.onAddNewFurniture()

    }
}

class NewProjectAction(private val dynProMain: DynProMain) : AbstractAction() {

    override fun actionPerformed(e: ActionEvent) {
        dynProMain.presenter.onCreateNewProject()
    }
}

class RemoveFurnitureAction(private val dynProMain: DynProMain) : AbstractAction() {
    private var projectTree: ProjectTree? = null

    override fun actionPerformed(e: ActionEvent) {
        dynProMain.presenter.onRemoveFurniture(projectTree!!.lastSelectedPathComponent.toString())
    }

    fun setProjectTree(projectTree: ProjectTree) {
        this.projectTree = projectTree
    }
}

class RenameProjectTreeFurnitureAction(private val dynProMain: DynProMain) : AbstractAction() {
    private var projectTree: ProjectTree? = null

    override fun actionPerformed(e: ActionEvent) {
        dynProMain.presenter.onRenameProjectTreeFurniture(projectTree!!.lastSelectedPathComponent.toString())

    }

    fun setProjectTree(projectTree: ProjectTree) {
        this.projectTree = projectTree
    }
}


class RenameMetadataFurnitureAction(private val dynProMain: DynProMain) : AbstractAction() {

    override fun actionPerformed(e: ActionEvent) {
        dynProMain.presenter.onRenameMetadataFurniture()

    }

}


class RenameProjectAction(private val dynProMain: DynProMain) : AbstractAction() {

    override fun actionPerformed(e: ActionEvent) {
        dynProMain.presenter.onRenameProject()
    }
}