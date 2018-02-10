package main

import java.awt.event.KeyEvent
import java.awt.event.KeyListener
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


val furnitureNameJTABinder = JTFBinder(DynProMain.furnitureNameDisplay)
val furnitureWidthSpinnerBinder = JSpinnerBinder(DynProMain.furnitureWidthDisplay)
val furnitureDepthSpinnerBinder = JSpinnerBinder(DynProMain.furnitureDepthDisplay)
val furnitureHeightSpinnerBinder= JSpinnerBinder(DynProMain.furnitureHeightDisplay)
val furnitureModuleUnitPriceSpinnerBinder = JSpinnerBinder(DynProMain.furnitureModuleUnitPriceDisplay)
val furnitureFrontPriceSpinnerBinder = JSpinnerBinder(DynProMain.furnitureFrontPriceDisplay)
val furnitureTypeComboBinder = JComboboxBinder(DynProMain.furnitureTypeDisplay)