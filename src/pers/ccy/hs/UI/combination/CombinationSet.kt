package pers.ccy.hs.UI.combination

import com.borland.jbcl.layout.XYConstraints
import com.borland.jbcl.layout.XYLayout
import pers.ccy.hs.data.CombinationData
import pers.ccy.hs.data.HouseData
import pers.ccy.hs.operation.OpCombination.importOpen
import pers.ccy.hs.operation.OpStructure.HD_Update
import pers.ccy.hs.operation.OpStructure.Select
import java.awt.Dialog
import java.awt.event.*
import javax.swing.*


class CombinationSet(combinationData: ArrayList<CombinationData>) : JPanel(), ActionListener {
    private val old: JComboBox<String> = JComboBox<String>()
    private val new: JComboBox<String> = JComboBox<String>()

    private val label = arrayOf(
        arrayOf(JLabel("选择整体的门："), JLabel("选择需要连接的门："))
    )

    private val bttn = JButton("确定")
    private val import = JButton("导入")

    override fun actionPerformed(e: ActionEvent?) {

        repaint()
    }

    fun NODoor() {
        JOptionPane.showOptionDialog(
            this,
            "房间没有门",
            "房间没有门",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null, arrayOf("取消"), "取消"
        )
    }

    init {
        this.layout = XYLayout()
        this.border = (BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED))
        this.isVisible = true


        label.forEach { it1 ->
            it1.forEachIndexed { index, it ->
                add(
                    it,
                    XYConstraints(10, 10 + 40 * index, 90, 30)
                )
            }
        }

        old.model = CombinationData.modelCB
        old.addItemListener(ItemListener {

        })
        add(old, XYConstraints(100, 10, 100, 30))

        new.model = CombinationData.modelNew
        new.addActionListener(this)
        add(new, XYConstraints(100, 50, 100, 30))

        add(bttn, XYConstraints(20, 250, 60, 30))
        bttn.addActionListener(ActionListener {
            if (CombinationUI.comData != null) {
                val Arr = old.selectedItem.toString().split(",")
                val a = combinationData[Arr[0].toInt() - 1].selectDoor(Arr[1].toInt())
                val str = new.selectedItem.toString()
                val b = CombinationUI.comData!!.selectDoor(str.substring(0, str.indexOf("号")).toInt())
                if (a.length == b.length && a.thick == b.thick) {
                    combinationData.add(CombinationUI.comData!!)
                    combinationData[combinationData.count() - 1].id = combinationData.count()
                    CombinationUI.comData = null
                    CombinationData.modelNew.removeAllElements()
                    this.parent.repaint()
                } else {
                    JOptionPane.showOptionDialog(
                        this,
                        "房门不一致，请重新选择",
                        "房门不一致",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null, arrayOf("取消"), "取消"
                    )
                }
            }
        })

        add(import, XYConstraints(120, 250, 60, 30))
        import.addActionListener(ActionListener {
            if (CombinationData.modelCB.size != 0) {
                CombinationUI.comData = importOpen()
                if (CombinationUI.comData == null) {
                    NODoor()
                }
                CombinationData.modelNew.removeAllElements()
                CombinationUI.comData?.UpdatCBModel()
                this.parent.repaint()
            } else {
                NODoor()
            }
        })


    }
}
