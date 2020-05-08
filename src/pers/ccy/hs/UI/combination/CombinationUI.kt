package pers.ccy.hs.UI.combination

import com.borland.jbcl.layout.XYConstraints
import com.borland.jbcl.layout.XYLayout
import pers.ccy.hs.UI.JPanel_SelectTasks
import pers.ccy.hs.data.CombinationData
import pers.ccy.hs.operation.OpCombination.UpdatModel
import pers.ccy.hs.operation.OpCombination.initOpen
import pers.ccy.hs.operation.OpFile
import pers.ccy.hs.operation.OpFile.import
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JFileChooser
import javax.swing.JPanel
import javax.swing.SwingWorker


class CombinationUI(var combinationData: ArrayList<CombinationData>) : JPanel(), ActionListener {

    val jp1 = JPanel_SelectTasks()
    val cominationInfo = CombinationInfo(combinationData)
    var jfc = JFileChooser() // 文件选择器
    val jp2 = CombinationSet(combinationData)
    var draw_w = 350
    var draw_h = 350
    val jp3 = CombinationDrawNew(draw_w - 10, draw_h - 20, 1.0)

    var draww = 350
    var drawh = 350
    val jp4 = CombinationDraw(combinationData, draww - 10, drawh - 20, 1.0)

    companion object {
        var comData: CombinationData? = null
    }

    override fun actionPerformed(e: ActionEvent) {
    }


    init {
        this.layout = XYLayout()
        this.add(jp1, XYConstraints(0, 0, 200, 50))
        this.add(jp2, XYConstraints(0, 55, 200, 290))
        this.isVisible = false

        this.add(cominationInfo, XYConstraints(205, 0, 150, 350))
        this.add(jp3, XYConstraints(360, 0, draw_w, draw_h))

        jp1.jrba[1].addActionListener {
            initOpen(combinationData)
            UpdatModel(combinationData)
            this.parent.repaint()
            comData?.toPrint()
        }
    }

}