package pers.ccy.hs.UI

import com.borland.jbcl.layout.XYConstraints
import com.borland.jbcl.layout.XYLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*

class CombinationUI :JPanel(), ActionListener {

    val jp1 = JPanel_SelectTasks()

    override fun actionPerformed(e: ActionEvent) {
    }

    init {
        this.layout = XYLayout()
        this.add(jp1, XYConstraints(0, 0, 200, 50))

        this.isVisible = true
    }
}