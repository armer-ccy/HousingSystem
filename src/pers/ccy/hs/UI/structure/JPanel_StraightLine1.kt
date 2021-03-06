package pers.ccy.hs.UI.structure

import javax.swing.BorderFactory
import javax.swing.JLabel


class JPanel_StraightLine1 : JPanel_StraightLine0() {

    val label = arrayOf(JLabel("绝对角度："), JLabel("相对距离："))
    val label2 = arrayOf(JLabel("度"), JLabel("分米"))

    init {
        this.layout = null
        this.border = (BorderFactory.createTitledBorder("绝对角度"))
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
