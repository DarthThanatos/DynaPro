package main

import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.JSpinner
import javax.swing.JTextField


class JTFBinder(private val jtf: JTextField): Binder() {

    init{
        jtf.addKeyListener(object : KeyListener{
            override fun keyTyped(e: KeyEvent?) {
//                notifyChange(jtf.text)
            }

            override fun keyPressed(e: KeyEvent?) {
//                notifyChange(jtf.text)
            }

            override fun keyReleased(e: KeyEvent?) {
                notifyChange(jtf.text)
            }

        })
    }

}


class JSpinnerBinder(private val spinner: JSpinner): Binder() {

    init{
        spinner.addChangeListener { e -> notifyChange(spinner.value)  }
    }

}

val furnitureNameJTABinder = JTFBinder(DynProMain.furnitureNameDisplay)
val furnitureWidthSpinnerBinder = JSpinnerBinder(DynProMain.furnitureWidthDisplay)