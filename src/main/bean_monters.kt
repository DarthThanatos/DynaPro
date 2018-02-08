package main

import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.JSpinner
import javax.swing.JTextField


class JTFBinder(private val jtf: JTextField): Binder() {

    init{
        jtf.addKeyListener(object : KeyListener{
            override fun keyTyped(e: KeyEvent?) {
//                notifyViewChanged(jtf.text)
            }

            override fun keyPressed(e: KeyEvent?) {
//                notifyViewChanged(jtf.text)
            }

            override fun keyReleased(e: KeyEvent?) {
                notifyViewChanged(jtf.text)
            }

        })
    }

}


class JSpinnerBinder(private val spinner: JSpinner): Binder() {

    init{
        spinner.addChangeListener { e -> notifyViewChanged(spinner.value)  }
    }

}

val furnitureNameJTABinder = JTFBinder(DynProMain.furnitureNameDisplay)
val furnitureWidthSpinnerBinder = JSpinnerBinder(DynProMain.furnitureWidthDisplay)