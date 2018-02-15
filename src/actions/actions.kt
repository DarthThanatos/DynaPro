package actions

import config.Config
import display.FrontConfigViewElem
import display.FrontConfigurationDisplayer
import display.ProjectTree
import main.DynProMain
import model.Element
import java.awt.event.ActionEvent
import javax.swing.AbstractAction


class NewFurnitureAction(private val dynProMain: DynProMain) : AbstractAction() {

    override fun actionPerformed(e: ActionEvent) {
        dynProMain.presenter.model.addDefaultFurniture()

    }
}

class NewProjectAction(private val dynProMain: DynProMain) : AbstractAction() {

    override fun actionPerformed(e: ActionEvent) {
        dynProMain.presenter.model.createNewProject()
    }
}

class RemoveFurnitureAction(private val dynProMain: DynProMain) : AbstractAction() {
    private var projectTree: ProjectTree? = null

    override fun actionPerformed(e: ActionEvent) {
        dynProMain.presenter.model.removeFurniture(projectTree!!.lastSelectedPathComponent.toString())
    }

    fun setProjectTree(projectTree: ProjectTree) {
        this.projectTree = projectTree
    }
}

class RenameProjectTreeFurnitureAction(private val dynProMain: DynProMain) : AbstractAction() {
    private var projectTree: ProjectTree? = null

    override fun actionPerformed(e: ActionEvent) {
        val furnitureName = projectTree!!.lastSelectedPathComponent.toString()
        val newFurnitureName = dynProMain.presenter.view.promptForUserInput(Config.GIVE_NEW_FURNITURE_NAME_MSG, furnitureName)
        dynProMain.presenter.model.renameFurniture(furnitureName, newFurnitureName)

    }

    fun setProjectTree(projectTree: ProjectTree) {
        this.projectTree = projectTree
    }
}


class RenameMetadataFurnitureAction(private val dynProMain: DynProMain) : AbstractAction() {

    override fun actionPerformed(e: ActionEvent) {

        val newFurnitureName = dynProMain.presenter.view.promptForUserInput(Config.GIVE_NEW_FURNITURE_NAME_MSG, dynProMain.presenter.getCurrentDisplayedFurnitureName())
        dynProMain.presenter.model.renameFurniture( dynProMain.presenter.getCurrentDisplayedFurnitureName(), newFurnitureName)
    }

}

class RenameProjectAction(private val dynProMain: DynProMain) : AbstractAction() {

    override fun actionPerformed(e: ActionEvent) {
        dynProMain.presenter.model.renameProject(dynProMain.presenter.view.promptForUserInput(Config.GIVE_NEW_PROJECT_NAME_MSG, dynProMain.presenter.model.getCurrentProject().getName()))
    }
}

abstract class FrontConfigAction(protected val dynProMain: DynProMain): AbstractAction(){
    var frontConfigurationDisplayer: FrontConfigurationDisplayer? = null
    abstract val presenterAction: ((String, String)->Unit)

    override fun actionPerformed(e: ActionEvent?) {
        presenterAction(frontConfigurationDisplayer?.currentlyDisplayedFurnitureName!!, frontConfigurationDisplayer?.recentlyClickedFurnitureElementId!!)
    }
  }


class RemoveFrontConfigElementAction(dynProMain: DynProMain): FrontConfigAction(dynProMain){
    override val presenterAction: (String, String) -> Unit = {s1,s2->dynProMain.presenter.model.removeFrontElementFromFurniture(s1,s2)}
}

class AddElementNextToAction(dynProMain: DynProMain): FrontConfigAction(dynProMain){
    override val presenterAction: (String, String) -> Unit = {s1, s2 -> dynProMain.presenter.model.addFrontConfigElementNextTo(s1, s2)}
}

class AddElementBeforeAction(dynProMain: DynProMain): FrontConfigAction(dynProMain){
    override val presenterAction: (String, String) -> Unit = {s1, s2 -> dynProMain.presenter.model.addFrontConfigElementBefore(s1,s2)}
}

class AddOneElementAggregateNextToAction(dynProMain: DynProMain): FrontConfigAction(dynProMain){
    override val presenterAction: (String, String) -> Unit = {s1, s2 -> dynProMain.presenter.model.addOneAggregateFrontConfigElemenetNextTo(s1, s2)}
}

class AddMultiElementAggragateNextToAction(dynProMain: DynProMain): FrontConfigAction(dynProMain){
    override val presenterAction: (String, String) -> Unit = {s1, s2 -> dynProMain.presenter.model.addMultiFrontConfigElementAggregateNextTo(s1, s2)}
}

class AddOneElementAggregateBeforeAction(dynProMain: DynProMain): FrontConfigAction(dynProMain){
    override val presenterAction: (String, String) -> Unit = {s1, s2 -> dynProMain.presenter.model.addOneFrontConfigElementAggregateBefore(s1,s2)}

}

class AddMultiElementAggregateBeforeAction(dynProMain: DynProMain): FrontConfigAction(dynProMain){
    override val presenterAction: (String, String) -> Unit = {s1, s2 -> dynProMain.presenter.model.addMultiFrontConfigElementAggregateBefore(s1,s2)}

}

class ModifyConfigElemAction(private val dynProMain: DynProMain): AbstractAction(){
    override fun actionPerformed(e: ActionEvent?) {
        val frontConfigViewElem = e?.source as FrontConfigViewElem
        val modelElem: Element = dynProMain.presenter.model.getFurnitureByName(frontConfigViewElem.furnitureName)?.frontConfiguration?.fetchElementWithId(frontConfigViewElem.modelKey)!!
        dynProMain.presenter.view.displayFrontConfigElemDialog(frontConfigViewElem.furnitureName, frontConfigViewElem.modelKey, modelElem.type, modelElem.width, modelElem.height, modelElem.name)
    }

}