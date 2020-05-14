package pers.ccy.hs.UI.structure

import javax.swing.BorderFactory
import javax.swing.JLabel


class JPanel_StraightLine3 : JPanel_StraightLine0() {

    val label = arrayOf(JLabel("坐标点x："), JLabel("坐标点y："))
    val label2 = arrayOf(JLabel("分米"), JLabel("分米"))

    init {
        this.layout = null
        this.border = (BorderFactory.createTitledBorder("0"))
        label.forEachIndexed { index, it ->
            it.setBounds(5, 15 + index * 30, 75, 30)
            this.add(it)
        }

        label2.forEachIndexed { index, it ->
            it.setBounds(155, 15 + index * 30, 30, 30)
            this.add(it)
        }

        txtfield.forEachIndexed { index, it ->
            it.setBounds(80, 15 + index * 30, 75, 30)
            this.add(it)
        }
    }
}
