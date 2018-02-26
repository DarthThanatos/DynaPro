package main

import display.FrontConfigViewElem
import display.FrontConfigurationDisplayer
import display.ProjectTree
import java.awt.event.*
import javax.swing.JCheckBox
import javax.swing.JComboBox
import javax.swing.JSpinner
import javax.swing.JSpinner.DefaultEditor
import javax.swing.JTextField


class JTFBinder(private val jtf: JTextField): Binder() {

    init{
        jtf.addKeyListener(object : KeyListener{
            override fun keyTyped(e: KeyEvent?) { }

            override fun keyPressed(e: KeyEvent?) { }

            override fun keyReleased(e: KeyEvent?) { notifyChange(jtf.text) }

        })
    }

}


class JSpinnerBinder(private val spinner: JSpinner): Binder() {

    init{
        spinner.addChangeListener { e -> notifyChange(spinner.value)  }

        (spinner.editor as DefaultEditor).textField.addKeyListener(object : KeyListener{
            override fun keyTyped(e: KeyEvent?) {}

            override fun keyPressed(e: KeyEvent?) {}

            override fun keyReleased(e: KeyEvent?) {
                try{
                    notifyChange(Integer.parseInt((spinner.editor as DefaultEditor).textField.text))
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        })
    }

}

class JComboboxBinder(private val combobox: JComboBox<Any>): Binder(){
    init{
        combobox.addActionListener({notifyChange(combobox.selectedItem.toString())})
    }
}

data class ProjectTreeNotifyValue(val popupActionTriggered: Boolean, val furnitureName: String)

class ProjectTreeBinder(private val projectTree: ProjectTree): Binder(){
    init{
        projectTree.addMouseListener(object:MouseListener{
            override fun mouseReleased(e: MouseEvent) {
                val lastSelectedPathComponent = projectTree.lastSelectedPathComponent
                if (lastSelectedPathComponent != null) {
                    notifyChange(ProjectTreeNotifyValue(e.isPopupTrigger, lastSelectedPathComponent.toString()))
                }

            }

            override fun mouseEntered(e: MouseEvent?) { }

            override fun mouseClicked(e: MouseEvent?) { }

            override fun mouseExited(e: MouseEvent?) { }

            override fun mousePressed(e: MouseEvent?) { }
        })
    }
}

data class FrontConfDisplayNotifyValue(val popupActionTriggered: Boolean, val furnitureName: String, val elementId: String)

class FrontConfigurationDisplayBinder(private val frontConfigurationDisplayer: FrontConfigurationDisplayer): Binder(){
    override fun registerSubscriber(id:String, onChange: OnChange){
        super.registerSubscriber(id, onChange)
        frontConfigurationDisplayer.addMouseListener(object: MouseListener{
            override fun mouseReleased(e: MouseEvent?) {
                val frontConfigElem = e!!.getSource() as FrontConfigViewElem
                frontConfigElem.notifyParentAboutChange()
                notifyChange(FrontConfDisplayNotifyValue(e.isPopupTrigger(), frontConfigElem.furnitureName, frontConfigElem.modelKey))
            }

            override fun mouseEntered(e: MouseEvent?) {}

            override fun mouseClicked(e: MouseEvent?) {}

            override fun mouseExited(e: MouseEvent?) {}

            override fun mousePressed(e: MouseEvent?) {}
        })
    }
}

class JCheckBoxBinder(private val checkBox: JCheckBox): Binder(){
    init {
        checkBox.addItemListener(object: ItemListener{
            override fun itemStateChanged(e: ItemEvent?) {
                notifyChange(checkBox.isSelected)
            }
        })
    }
}

val furnitureNameJTABinder = JTFBinder(DynProMain.furnitureNameDisplay)
val furnitureWidthSpinnerBinder = JSpinnerBinder(DynProMain.furnitureWidthDisplay)
val furnitureDepthSpinnerBinder = JSpinnerBinder(DynProMain.furnitureDepthDisplay)
val furnitureHeightSpinnerBinder= JSpinnerBinder(DynProMain.furnitureHeightDisplay)
val furnitureModuleUnitPriceSpinnerBinder = JSpinnerBinder(DynProMain.furnitureModuleUnitPriceDisplay)
val furnitureFrontPriceSpinnerBinder = JSpinnerBinder(DynProMain.furnitureFrontPriceDisplay)
val furnitureTypeComboBinder = JComboboxBinder(DynProMain.furnitureTypeDisplay)
val frontConfigurationOrientationBinder = JComboboxBinder(DynProMain.frontConfigurationOrientation)
val furnitureRoofOptionsBinder = JComboboxBinder(DynProMain.furnitureRoofOptions)
val furnitureBackOptionsBinder = JComboboxBinder(DynProMain.furnitureBackOptions)
val projectTreeBinder = ProjectTreeBinder(DynProMain.projectTree)
val frontConfigurationDisplayBinder = FrontConfigurationDisplayBinder(DynProMain.frontConfigurationDisplayer)
val pedestalHeightDisplayBinder = JSpinnerBinder(DynProMain.pedestalHeightDisplayer)