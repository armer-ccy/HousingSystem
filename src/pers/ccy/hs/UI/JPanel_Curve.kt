package pers.ccy.hs.UI

import pers.ccy.hs.data.HouseData
import pers.ccy.hs.operation.Op.Add
import java.awt.event.*
import javax.swing.*

class JPanel_Curve(houseData: HouseData) : JPanel(), ActionListener {
    val label = JLabel("弧线类型：")
    val cmb: JComboBox<String> = JComboBox<String>()
    val str = arrayOf(
        "绝对角度圆心", "相对角度圆心", "三点画圆"
    )
    val jp_ = arrayOf(
        JPanel_Curve1(), JPanel_Curve2(), JPanel_Curve3()
    )
    var jp = jp_[0]
    val jbtn = JButton("确定")

    override fun actionPerformed(e: ActionEvent) {
        this.remove(jp)
        jp = jp_[cmb.selectedIndex]
        jp.setBounds(5, 45, 190, 155)
        this.add(jp)
        this.repaint()
    }

    init {
        this.layout = null
        this.border = (BorderFactory.createTitledBorder("选择"))
        str.forEach { cmb.addItem(it) }
        cmb.addActionListener(this)
        label.setBounds(5, 15, 75, 30)
        this.add(label)
        cmb.setBounds(80, 15, 100, 30)
        this.add(cmb)
        jp.setBounds(5, 45, 190, 155)
        this.add(jp)
        jbtn.setBounds(70, 210, 60, 30)
        this.add(jbtn)
        jbtn.addActionListener(ActionListener {
            if (jp.txtfield[3].isVisible) {
                jp.txtfield[0].text.toDoubleOrNull()?.let { it1 ->
                    jp.txtfield[1].text.toDoubleOrNull()?.let { it2 ->
                        jp.txtfield[2].text.toDoubleOrNull()?.let { it3 ->
                            jp.txtfield[3].text.toDoubleOrNull()?.let { it4 ->
                                Add(
                                    houseData, HouseData(
                                        ++houseData.id, 2, cmb.selectedIndex,
                                        it1, it2, it3, it4
                                    )
                                )
                            }
                        }
                    }
                }
            } else {
                jp.txtfield[0].text.toDoubleOrNull()?.let { it1 ->
                    jp.txtfield[1].text.toDoubleOrNull()?.let { it2 ->
                        jp.txtfield[2].text.toDoubleOrNull()?.let { it3 ->
                            Add(
                                houseData, HouseData(
                                    ++houseData.id, 2, cmb.selectedIndex,
                                    it1, it2, it3
                                )
                            )
                        }
                    }
                }
            }
            houseData.UpdatModel()
        })
    }
}