package pers.ccy.hs.UI

import pers.ccy.hs.data.HouseData
import pers.ccy.hs.operation.Op.MoveDown
import pers.ccy.hs.operation.Op.MoveUp
import pers.ccy.hs.operation.Op.Remove
import java.awt.event.ActionListener
import javax.swing.*
import javax.swing.event.ListSelectionListener


class JPanel_Info(model: DefaultListModel<String>, houseData: HouseData) : JPanel() {
    val scrollPane = JScrollPane()
    val list = JList(model)
    val btn = arrayOf(
        JButton("修改"), JButton("删除"),
        JButton("上移"), JButton("下移")
    )
    var str: String? = null

    init {
        this.layout = null
        this.border = (BorderFactory.createTitledBorder("线形信息"))
        scrollPane.setViewportView(list)
        list.selectionMode = ListSelectionModel.SINGLE_SELECTION
        list.addListSelectionListener(ListSelectionListener {
            str = list.selectedValue?.indexOf(',')?.let { it1 -> list.selectedValue?.substring(0, it1) }
        })
        this.add(scrollPane)

        btn.forEachIndexed { index, it ->
            it.setBounds(10 + (index % 2) * 70, 275 + (index / 2) * 40, 60, 30)
            this.add(it)
            it.addActionListener(ActionListener {
                when (index) {
                    0 -> {
                        if (str != null) {
                            ReviseUI(houseData, str!!.toInt()).show()//show()不能删
                        }
                    }
                    1 ->
                        if (str == null)
                            Remove(houseData)
                        else
                            Remove(houseData, str!!.toInt())
                    2 -> if (str != null) MoveUp(houseData, str!!.toInt())
                    3 -> if (str != null) MoveDown(houseData, str!!.toInt())
                }
                houseData.UpdatModel()
                houseData.windowDoorData!!.UpdatModel()
                this.parent.repaint()
            })
        }
    }
}