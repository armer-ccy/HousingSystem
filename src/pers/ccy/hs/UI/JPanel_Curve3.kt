package pers.ccy.hs.UI

import javax.swing.BorderFactory
import javax.swing.JLabel


class JPanel_Curve3 : JPanel_Curve0() {

    val label = arrayOf(JLabel("中间点坐标点x："), JLabel("中间点坐标点y："), JLabel("末点坐标点x："), JLabel("末点坐标点y："))
    val label2 = arrayOf(JLabel("单位"), JLabel("单位"), JLabel("单位"), JLabel("单位"))

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
