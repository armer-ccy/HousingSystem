package pers.ccy.hs.UI.structure

import javax.swing.*

class JPanel_SelectStraightLineORCurve : JPanel() {

    val jrba = arrayOf(
        JRadioButton("直线"), JRadioButton("曲线"), JRadioButton("门"), JRadioButton("窗")
    )
    val bg = ButtonGroup()

    init {
        this.layout = null
        this.border = (BorderFactory.createTitledBorder("选择构造线形"))
        jrba.get(0).isSelected = true
        jrba.forEachIndexed { index, item ->
            if(index<2)
                item.setBounds(5 + index * 50, 15, 50, 30)
            else
                item.setBounds(25 + index * 40, 15, 40, 30)
        }
        jrba.forEach { this.add(it) }
        jrba.forEach { bg.add(it) }
    }
}