package pers.ccy.hs.UI.combination

import pers.ccy.hs.data.CombinationData
import pers.ccy.hs.operation.OpCombination.UpdatModel
import pers.ccy.hs.operation.OpCombination.initOpen
import java.awt.event.ActionListener
import javax.swing.*


class CombinationInfo(combinationData: ArrayList<CombinationData>) : JPanel() {
    val scrollPane = JScrollPane()
    val list = JList(CombinationData.model)
    val btn = JButton("删除")
    var str: String? = null

    init {
        this.layout = null
        this.border = (BorderFactory.createTitledBorder("房间信息"))
        scrollPane.setViewportView(list)
        //设置不能选中未解决
        list.selectionMode = ListSelectionModel.SINGLE_SELECTION
        this.add(scrollPane)
        scrollPane.setBounds(5, 15, 140, 285)

        btn.setBounds(45, 315, 60, 30)
        this.add(btn)
        btn.addActionListener(ActionListener {
            combinationData.remove(combinationData.last())
            UpdatModel(combinationData)
            this.repaint()
            initOpen(combinationData)
            UpdatModel(combinationData)
            this.repaint()
        })
    }
}