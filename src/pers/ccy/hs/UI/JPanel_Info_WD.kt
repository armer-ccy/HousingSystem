package pers.ccy.hs.UI

import pers.ccy.hs.data.WindowDoorData
import pers.ccy.hs.operation.Op.Remove
import java.awt.event.ActionListener
import javax.swing.*
import javax.swing.event.ListSelectionListener


class JPanel_Info_WD(model: DefaultListModel<String>, windowDoorData: WindowDoorData) : JPanel() {
    val scrollPane = JScrollPane()
    val list = JList(model)
    val btn = arrayOf(
        JButton("修改"), JButton("删除")
    )
    var str: String? = null

    init {
        this.layout = null
        this.border = (BorderFactory.createTitledBorder("门窗信息"))
        scrollPane.setViewportView(list)
        list.selectionMode = ListSelectionModel.SINGLE_SELECTION
        list.addListSelectionListener(ListSelectionListener {
            str = list.selectedValue?.indexOf(',')?.let { it1 -> list.selectedValue?.substring(0, it1) }
        })
        this.add(scrollPane)

        btn.forEachIndexed { index, it ->
            it.setBounds(22, 275 + index * 40, 60, 30)
            this.add(it)
            it.addActionListener(ActionListener {
                when (index) {
                    0 -> {
                        if (str != null) {
                            ReviseWDUI(windowDoorData, str!!.toInt()).show()//show()不能删
                        }
                    }
                    1 ->
                        if (str == null)
                            Remove(windowDoorData)
                        else
                            Remove(windowDoorData, str!!.toInt())
                }
                windowDoorData.UpdatModel()
                this.parent.repaint()
            })
        }
    }
}