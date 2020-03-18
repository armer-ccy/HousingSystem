package pers.ccy.hs.UI

import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.JPanel
import javax.swing.JTextField


open class JPanel_StraightLine0 : JPanel() {
    val txtfield = arrayOf(JTextField(28), JTextField(28))

    init {
        txtfield.forEach {
            it.addKeyListener(object : KeyListener {
                override fun keyTyped(e: KeyEvent?) {
                    val keyChar = e!!.keyChar
                    if (keyChar >= '0' && keyChar <= '9' || keyChar.toInt() == 8) {
                    } else if (keyChar == '.') {
                        if (it.text.contains('.')) {
                            e.consume()
                        }
                    } else if (keyChar == '-' && it.text == "") {
                    } else {
                        e.consume()
                    }
                }

                override fun keyPressed(e: KeyEvent?) {}
                override fun keyReleased(e: KeyEvent?) {}
            })
        }
    }
}
