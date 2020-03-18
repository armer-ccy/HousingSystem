package pers.ccy.hs.UI

import javax.swing.*


class JPanel_SelectTasks : JPanel() {

    val jrba = arrayOf(
        JRadioButton("房间构造"), JRadioButton("房间组合")
    )
    val bg = ButtonGroup()

    init {
        this.layout = null
        this.border = (BorderFactory.createTitledBorder("选择工作内容"))
        jrba.get(0).isSelected = true
        jrba.forEachIndexed { index, item ->  item.setBounds(5 + index * 100, 15, 80, 30)}
        jrba.forEach { this.add(it) }
        jrba.forEach { bg.add(it) }
    }
}