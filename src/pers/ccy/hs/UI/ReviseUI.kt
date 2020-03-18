package pers.ccy.hs.UI

import com.borland.jbcl.layout.XYConstraints
import com.borland.jbcl.layout.XYLayout
import pers.ccy.hs.data.HouseData
import pers.ccy.hs.operation.Op.HD_Update
import pers.ccy.hs.operation.Op.Select
import java.awt.Dialog
import java.awt.event.*
import javax.swing.*


class ReviseUI(houseData: HouseData, i: Int) : JDialog(), ActionListener {
    private val jp = JPanel()
    val str_result = Select(houseData, i)
    val strArr = str_result.split(",")
    private var select = 0
    private val title_ = JLabel("选择构造线形：")
    private val str = arrayOf(
        "直线", "曲线"
    )
    private val cmb: JComboBox<String> = JComboBox<String>()

    private val title_2 = JLabel("选择：")
    private val str_ = arrayOf(
        arrayOf("绝对角度", "相对角度", "坐标点", "相对位移"),
        arrayOf("绝对角度圆心", "相对角度圆心", "三点画圆")
    )
    private val cmb_: JComboBox<String> = JComboBox<String>()

    private val label = arrayOf(
        arrayOf(JLabel("绝对角度："), JLabel("相对距离：")),
        arrayOf(JLabel("相对角度："), JLabel("相对距离：")),
        arrayOf(JLabel("坐标点x："), JLabel("坐标点y：")),
        arrayOf(JLabel("x位移："), JLabel("y位移：")),
        arrayOf(JLabel("圆心绝对角度："), JLabel("圆心相对距离："), JLabel("扇形度数：")),
        arrayOf(JLabel("圆心相对角度："), JLabel("圆心相对距离："), JLabel("扇形度数：")),
        arrayOf(JLabel("中间点坐标点x："), JLabel("中间点坐标点y："), JLabel("末点坐标点x："), JLabel("末点坐标点y："))
    )
    private val txtfield = arrayOf(JTextField(28), JTextField(28), JTextField(28), JTextField(28))
    private val label2 = arrayOf(
        arrayOf(JLabel("单位"), JLabel("单位")),
        arrayOf(JLabel("单位"), JLabel("单位")),
        arrayOf(JLabel("单位"), JLabel("单位")),
        arrayOf(JLabel("单位"), JLabel("单位")),
        arrayOf(JLabel("单位"), JLabel("单位"), JLabel("单位")),
        arrayOf(JLabel("单位"), JLabel("单位"), JLabel("单位")),
        arrayOf(JLabel("单位"), JLabel("单位"), JLabel("单位"), JLabel("单位"))
    )

    private val bttn = JButton("确定")

    override fun actionPerformed(e: ActionEvent?) {

        //label.forEach { it1 -> it1.forEach { jp.remove(it) } }
        //label2.forEach { it1 -> it1.forEach { jp.remove(it) } }

        /*label[cmb.selectedIndex * 4 + cmb_.selectedIndex].forEachIndexed { index, it ->
            jp.add(
                it,
                XYConstraints(10, 90 + 40 * index, 90, 30)
            )
        }
        label2[cmb.selectedIndex * 4 + cmb_.selectedIndex].forEachIndexed { index, it ->
            jp.add(
                it,
                XYConstraints(160, 90 + 40 * index, 30, 30)
            )
        }*/

        label.forEach { it1 -> it1.forEach { it.isVisible = false } }
        label[cmb.selectedIndex * 4 + cmb_.selectedIndex].forEach { it.isVisible = true }

        label2.forEach { it1 -> it1.forEach { it.isVisible = false } }
        label2[cmb.selectedIndex * 4 + cmb_.selectedIndex].forEach { it.isVisible = true }

        txtfield.forEach { it.isVisible = true }
        if (cmb.selectedIndex * 4 + cmb_.selectedIndex < 6)
            txtfield[3].isVisible = false
        if (cmb.selectedIndex * 4 + cmb_.selectedIndex < 4)
            txtfield[2].isVisible = false

        jp.repaint()
    }

    init {
        this.setBounds(100, 100, 100, 500)
        this.modalityType = Dialog.ModalityType.APPLICATION_MODAL
        this.title = "修改"
        this.isResizable = true
        this.defaultCloseOperation = DISPOSE_ON_CLOSE
        this.add(jp)

        jp.setBounds(100, 100, 100, 500)
        jp.layout = XYLayout()
        jp.isVisible = true

        jp.add(title_, XYConstraints(10, 10, 90, 30))
        str.forEach { cmb.addItem(it) }
        cmb.addItemListener(ItemListener { e ->
            if (e.stateChange == ItemEvent.SELECTED) {
                if (select != cmb.selectedIndex) {
                    cmb_.removeAllItems()
                    str_[cmb.selectedIndex].forEach { cmb_.addItem(it) }
                    select = cmb.selectedIndex
                }
            }
        })
        jp.add(cmb, XYConstraints(100, 10, 100, 30))

        jp.add(title_2, XYConstraints(10, 50, 90, 30))
        str_[0].forEach { cmb_.addItem(it) }
        cmb_.addActionListener(this)
        jp.add(cmb_, XYConstraints(100, 50, 100, 30))

        cmb.selectedIndex = strArr[1].toInt() - 1
        cmb_.selectedIndex = strArr[2].toInt()

        txtfield.forEachIndexed { index, it ->
            jp.add(it, XYConstraints(100, 90 + 40 * index, 60, 30))
            it.text = if (strArr[3 + index] == "0.0") "" else strArr[3 + index]
            it.addKeyListener(object : KeyListener {
                override fun keyTyped(e: KeyEvent?) {
                    val keyChar = e!!.keyChar
                    if (keyChar >= '0' && keyChar <= '9' || keyChar.toInt() == 8) {
                    } else if (keyChar == '.') {
                        if (it.text.contains('.')) {
                            e.consume()
                        }
                    } else if (keyChar == '-' && it.text == "") {
                    } else {
                        e.consume()
                    }
                }

                override fun keyPressed(e: KeyEvent?) {}
                override fun keyReleased(e: KeyEvent?) {}
            })
        }


        label.forEach { it1 ->
            it1.forEachIndexed { index, it ->
                jp.add(
                    it,
                    XYConstraints(10, 90 + 40 * index, 90, 30)
                )
            }
        }

        label2.forEach { it1 ->
            it1.forEachIndexed { index, it ->
                jp.add(
                    it,
                    XYConstraints(160, 90 + 40 * index, 90, 30)
                )
            }
        }

        jp.add(bttn, XYConstraints(90, 400, 60, 30))
        bttn.addActionListener(ActionListener {
            val a = cmb.selectedIndex + 1
            val b = cmb_.selectedIndex
            var str = strArr[0] + "," + a + "," + b
            txtfield.forEach { str += "," + if (it.text == "") "0.0" else it.text }
            HD_Update(
                houseData,
                i,
                str
            )
            this.dispose()
        })
    }
}
