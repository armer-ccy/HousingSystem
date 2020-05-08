package pers.ccy.hs.UI.combination

import com.borland.jbcl.layout.XYConstraints
import com.borland.jbcl.layout.XYLayout
import pers.ccy.hs.data.CombinationData
import pers.ccy.hs.data.Point
import pers.ccy.hs.operation.OpCombination
import pers.ccy.hs.operation.OpCombination.VectorAngle
import pers.ccy.hs.operation.OpCombination.importOpen
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.ItemListener
import javax.swing.*
import kotlin.math.cos
import kotlin.math.sin


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
                    a.isused = true
                    b.isused = true
                    //a.point[3]->b.point[0]
                    //求角度
                    combinationData[combinationData.count() - 1].angle =
                        -VectorAngle(
                            a.point[0]!!.sub(a.point[1]!!),
                            b.point[1]!!.sub(b.point[0]!!)
                        ) + combinationData[Arr[0].toInt() - 1].angle
                    //求起始
                    //https://www.cnblogs.com/herd/p/11620760.html
                    val angle = combinationData[combinationData.count() - 1].angle
                    val angle1 = combinationData[Arr[0].toInt() - 1].angle
                    var real = a.point[3]!!
                    real = Point(
                        (real.x) * cos(angle1) - (real.y) * sin(angle1),
                        (real.x) * sin(angle1) + (real.y) * cos(angle1)
                    )
                    real = Point(
                        real.x + combinationData[Arr[0].toInt() - 1].start.x,
                        real.y + combinationData[Arr[0].toInt() - 1].start.y
                    )
                    val m = real.sub(b.point[0]!!)
                    val xx: Double =
                        (m.x - real.x) * cos(angle) - (m.y - real.y) * sin(angle) + real.x
                    val yy: Double =
                        (m.x - real.x) * sin(angle) + (m.y - real.y) * cos(angle) + real.y
                    combinationData[combinationData.count() - 1].start = Point(xx, yy)
                    combinationData.forEach {
                        println("id:${it.id},angle:${it.angle},startx:${it.start.x},starty:${it.start.y}")
                    }
                    //保存连接
                    combinationData[combinationData.count() - 1].connect = old.selectedItem.toString()
                    CombinationUI.comData = null
                    CombinationData.modelNew.removeAllElements()
                    OpCombination.UpdatModel(combinationData)
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
