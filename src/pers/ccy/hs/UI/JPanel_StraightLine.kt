package pers.ccy.hs.UI

import pers.ccy.hs.data.HouseData
import pers.ccy.hs.operation.Op.Add
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*


class JPanel_StraightLine(houseData: HouseData) : JPanel(), ActionListener {

    private val label = JLabel("直线画法：")
    private val cmb: JComboBox<String> = JComboBox<String>()
    private val str = arrayOf(
        "绝对角度", "相对角度", "坐标点", "相对位移"
    )
    private val jp_ = arrayOf(
        JPanel_StraightLine1(), JPanel_StraightLine2(), JPanel_StraightLine3(), JPanel_StraightLine4()
    )
    private var jp = jp_[0]
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
            jp.txtfield[0].text.toDoubleOrNull()?.let { it1 ->
                jp.txtfield[1].text.toDoubleOrNull()?.let { it2 ->
                    Add(
                        houseData, HouseData(
                            ++houseData.id, 1, cmb.selectedIndex,
                            it1, it2
                        )
                    )
                }
            }
            houseData.UpdatModel()
        })
    }
}