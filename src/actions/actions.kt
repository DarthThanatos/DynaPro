package actions

import com.beust.klaxon.Klaxon
import config.Config
import contract.ProjectSave
import display.*
import display._3d.FurniturePerspective
import main.DynProMain
import model.Element
import org.json.JSONObject
import java.awt.CardLayout
import java.awt.event.ActionEvent
import java.io.File
import javax.swing.*


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
        val maxDimension = dynProMain.presenter.model.getFurnitureByName(frontConfigViewElem.furnitureName)?.frontConfiguration?.getMaxDimensionOf(modelElem.id)!!
        val maxWidth = maxDimension.width
        val maxHeight = maxDimension.height
        dynProMain.presenter.view.displayFrontConfigElemDialog(frontConfigViewElem.furnitureName, frontConfigViewElem.modelKey, modelElem.type, modelElem.width, modelElem.height, maxWidth, maxHeight, modelElem.name, modelElem.blockedWidth, modelElem.blockedHeight, modelElem.growthRingVerticallyOriented, modelElem.shelvesNumber)
    }

}

abstract class MoveToAction : AbstractAction(){
    abstract val childPanelId: String
    lateinit var viewSwitcherPanel: JPanel

    override fun actionPerformed(e: ActionEvent?) {
        val viewSwitcherLayout = viewSwitcherPanel.layout as CardLayout
        viewSwitcherLayout.show(viewSwitcherPanel, childPanelId)
    }

}

class MoveToMainMenuAction(override val childPanelId: String = "input_main_panel") : MoveToAction()

class MoveToFurniturePerspectiveAction(private val dynProMain: DynProMain): MoveToAction(){
    override val childPanelId: String = "output3D"
    lateinit var furniturePerspective: FurniturePerspective

    override fun actionPerformed(e: ActionEvent?) {
        furniturePerspective.setFurniture(
                dynProMain.presenter.model.getFurnitureByName(dynProMain.presenter.getCurrentDisplayedFurnitureName())
        )
        super.actionPerformed(e)
    }
}

abstract class MoveToDisembowelmentAction(private val dynProMain: DynProMain): MoveToAction(){
    override val childPanelId: String = "outputDisembowelment"

    lateinit var furnitureDisembowelmentDisplay: FurnitureDisembowelmentDisplay
    lateinit var disembowelmentSwitcher: JButton
    lateinit var disembowelmentContainer: JPanel

    abstract val disembowelAction : (FurnitureDisembowelmentDisplay ) -> Unit

    override fun actionPerformed(e: ActionEvent?) {
        (disembowelmentContainer.layout as CardLayout).show(disembowelmentContainer, "forUser")
        disembowelAction(furnitureDisembowelmentDisplay)
        super.actionPerformed(e)
    }
}

class MoveToProjectDisembowelmentAction(dynProMain: DynProMain): MoveToDisembowelmentAction(dynProMain){
    override val disembowelAction: (FurnitureDisembowelmentDisplay) -> Unit = {it.displayAllProjectSlabs(dynProMain.presenter.model.getCurrentProject() ); disembowelmentSwitcher.setEnabled(true)}
}

class MoveToFurnitureDisembowelmentAction(dynProMain: DynProMain): MoveToDisembowelmentAction(dynProMain){
    override val disembowelAction: (FurnitureDisembowelmentDisplay) -> Unit = {it.displayFurnitureSlabs(dynProMain.presenter.model.getFurnitureByName(dynProMain.presenter.getCurrentDisplayedFurnitureName())); ; disembowelmentSwitcher.setEnabled(false)}

}

class PrintDisembowelmentAction: AbstractAction() {
    lateinit var furnitureDisembowelmentDisplay: FurnitureDisembowelmentDisplay
    lateinit var drevitDisembowelment: DrevitDisembowelment
    override fun actionPerformed(e: ActionEvent?) {
        if(furnitureDisembowelmentDisplay.isVisible) furnitureDisembowelmentDisplay.printComponent()
        if(drevitDisembowelment.isVisible) drevitDisembowelment.printComponent()
    }
}



class SwitchDisembowelmentAction(private val dynProMain: DynProMain): AbstractAction(){
    lateinit var furnitureDisembowelmentDisplay: FurnitureDisembowelmentDisplay
    lateinit var drevitDisembowelment: DrevitDisembowelment

    lateinit var disembowelmentContainer: JPanel

    override fun actionPerformed(e: ActionEvent?) {
        drevitDisembowelment.displayAllProjectSlabs(dynProMain.presenter.model.getCurrentProject())
        furnitureDisembowelmentDisplay.displayAllProjectSlabs(dynProMain.presenter.model.getCurrentProject())
        (disembowelmentContainer.layout as CardLayout).next(disembowelmentContainer)
    }

}

class SaveAction(private val dynProMain: DynProMain) : AbstractAction(){
    lateinit var parent: JFrame
    override fun actionPerformed(e: ActionEvent?) {
        val jc = JFileChooser()
        val showSaveDialog = jc.showSaveDialog(parent)
        if(showSaveDialog == JFileChooser.APPROVE_OPTION){
            val projectSave = dynProMain.presenter.model.getCurrentProject().savedState()
            File(jc.selectedFile.absolutePath.toString())
                    .writeText(JSONObject(Klaxon().toJsonString(projectSave)).toString(4))
        }
    }

}

class OpenAction(private val dynProMain: DynProMain): AbstractAction(){
    lateinit var parent: JFrame
    override fun actionPerformed(e: ActionEvent?) {
        val jc = JFileChooser()
        val openDialog = jc.showOpenDialog(parent)
        if(openDialog == JFileChooser.APPROVE_OPTION) {
            println(jc.selectedFile.name)
            val projectSave = Klaxon().parse<ProjectSave>(file = File(jc.selectedFile.absoluteFile.toString()))!!
            dynProMain.presenter.model.getCurrentProject().restoreState(projectSave)
            dynProMain.presenter.onNewProjectCreated()
        }
    }

}