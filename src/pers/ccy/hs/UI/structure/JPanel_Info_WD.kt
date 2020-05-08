package pers.ccy.hs.UI.structure

import pers.ccy.hs.data.HouseData
import pers.ccy.hs.operation.OpStructure.RemoveWD
import java.awt.event.ActionListener
import javax.swing.*
import javax.swing.event.ListSelectionListener


class JPanel_Info_WD(houseData: HouseData) : JPanel() {
    val scrollPane = JScrollPane()
    val list = JList(HouseData.modelWD)
    val btn = arrayOf(
        JButton("修改"), JButton("删除")
    )
    //var str: String? = null
    var strArr: List<String>? = null

    init {
        this.layout = null
        this.border = (BorderFactory.createTitledBorder("门窗信息"))
        scrollPane.setViewportView(list)
        list.selectionMode = ListSelectionModel.SINGLE_SELECTION
        list.addListSelectionListener(ListSelectionListener {
            //str = list.selectedValue?.indexOf(',')?.let { it1 -> list.selectedValue?.substring(0, it1) }
            strArr = list.selectedValue?.split(",")
        })
        this.add(scrollPane)

        btn.forEachIndexed { index, it ->
            it.setBounds(22, 275 + index * 40, 60, 30)
            this.add(it)
            it.addActionListener(ActionListener {
                when (index) {
                    0 -> {
                        if (strArr != null) {
                            ReviseWDUI(
                                houseData,
                                strArr!![0].toInt(),
                                strArr!![1].toInt()
                            ).show()//show()不能删
                        }
                    }
                    1 ->
                        //if (str == null)
                        //    str = list//list.lastVisibleIndex
                        //else
                        if (strArr != null)
                            RemoveWD(houseData, strArr!![0].toInt())
                }
                houseData.UpdatModel()
                this.parent.repaint()
            })
        }
    }
}