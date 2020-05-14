package pers.ccy.hs.UI.structure

import pers.ccy.hs.data.HouseData
import pers.ccy.hs.data.WindowDoorData
import java.awt.event.*
import javax.swing.*

class JPanel_Door(houseData: HouseData) : JPanel(), ActionListener {
    val label = arrayOf(
        arrayOf(JLabel("门所在墙："), JLabel("门的厚度："), JLabel("离墙边距离："), JLabel("门宽度："), JLabel("门高度：")),
        arrayOf(JLabel("门所在墙："), JLabel("门的厚度："), JLabel("离墙边角度："), JLabel("门角度："), JLabel("门高度："))
    )
    val label2 = arrayOf(
        arrayOf(JLabel("分米"), JLabel("分米"), JLabel("分米"), JLabel("分米")),
        arrayOf(JLabel("分米"), JLabel("度"), JLabel("度"), JLabel("分米"))
    )
    val cmb: JComboBox<String> = JComboBox<String>()
    val txtfield = arrayOf(JTextField(28), JTextField(28), JTextField(28), JTextField(28))
    val jbtn = JButton("确定")

    override fun actionPerformed(e: ActionEvent) {  }

    init {
        this.layout = null
        this.border = (BorderFactory.createTitledBorder("选择"))

        cmb.model = HouseData.modelCB
        cmb.setBounds(80, 15, 100, 30)
        this.add(cmb)
        cmb.addItemListener(ItemListener { e ->
            if (e.stateChange == ItemEvent.SELECTED) {
                val strArr = cmb.selectedItem.toString().split(",")
                if (strArr[1] == "2") {
                    label[0].forEach { it.isVisible = false }
                    label[1].forEach { it.isVisible = true }
                    label2[0].forEach { it.isVisible = false }
                    label2[1].forEach { it.isVisible = true }
                } else {
                    label[0].forEach { it.isVisible = true }
                    label[1].forEach { it.isVisible = false }
                    label2[0].forEach { it.isVisible = true }
                    label2[1].forEach { it.isVisible = false }
                }
            }
        })

        label.forEach {
            it.forEachIndexed { index, it ->
                it.setBounds(5, 15 + index * 30, 75, 30)
                this.add(it)
            }
        }
        label2.forEach {
            it.forEachIndexed { index, it ->
                it.setBounds(155, 45 + index * 30, 30, 30)
                this.add(it)
            }
        }

        jbtn.setBounds(70, 210, 60, 30)
        this.add(jbtn)
        jbtn.addActionListener(ActionListener {
            txtfield[0].text.toDoubleOrNull()?.let { it1 ->
                txtfield[1].text.toDoubleOrNull()?.let { it2 ->
                    txtfield[2].text.toDoubleOrNull()?.let { it3 ->
                        txtfield[3].text.toDoubleOrNull()?.let { it4 ->
                            val str = cmb.selectedItem.toString()
                            houseData.Search(str.substring(0, str.indexOf(',')).toInt()).Add(
                                WindowDoorData(
                                    ++WindowDoorData.number, 0,
                                    it1, it2, it3, it4
                                )
                            )
                        }
                    }
                }
            }
            houseData.UpdatModel()
            this.parent.repaint()
        })

        txtfield.forEachIndexed { index, it ->
            it.setBounds(80, 45 + index * 30, 75, 30)
            this.add(it)
            it.addKeyListener(object : KeyListener {
                override fun keyTyped(e: KeyEvent?) {
                    val keyChar = e!!.keyChar
                    if (keyChar >= '0' && keyChar <= '9' || keyChar.toInt() == 8) {
                    } else if (keyChar == '.') {
                        if (it.text.contains('.')) {
                            e.consume()
                        }
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