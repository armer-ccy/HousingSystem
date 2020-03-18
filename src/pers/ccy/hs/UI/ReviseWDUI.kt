package pers.ccy.hs.UI

import com.borland.jbcl.layout.XYConstraints
import com.borland.jbcl.layout.XYLayout
import pers.ccy.hs.data.WindowDoorData
import pers.ccy.hs.operation.Op.Select
import pers.ccy.hs.operation.Op.WDD_Update
import java.awt.Dialog
import java.awt.event.*
import javax.swing.*


class ReviseWDUI(windowDoorData: WindowDoorData, i: Int) : JDialog(), ActionListener {
    private val jp = JPanel()
    val str_result = Select(windowDoorData, i)
    val strArr = str_result.split(",")
    private var select = 0
    private var select2 = 0
    private val title_ = JLabel("选择构造种类：")
    private val str = arrayOf(
        "门", "窗"
    )
    val cmb_: JComboBox<String> = JComboBox<String>()

    val label = arrayOf(
        arrayOf(JLabel("门所在墙："), JLabel("离墙边距离："), JLabel("门宽度："), JLabel("门高度：")),
        arrayOf(JLabel("门所在墙："), JLabel("离墙边角度："), JLabel("门角度："), JLabel("门高度：")),
        arrayOf(JLabel("窗户所在墙："), JLabel("离墙边距离："), JLabel("窗户宽度："), JLabel("窗户高度："), JLabel("离地高度：")),
        arrayOf(
            JLabel("窗户所在墙："), JLabel("离墙边角度："), JLabel("窗户角度："), JLabel("窗户高度："), JLabel("离地高度：")
        )
    )
    val label2 = arrayOf(
        arrayOf(JLabel("单位"), JLabel("单位"), JLabel("单位")),
        arrayOf(JLabel("单位"), JLabel("单位"), JLabel("单位")),
        arrayOf(JLabel("单位"), JLabel("单位"), JLabel("单位"), JLabel("单位")),
        arrayOf(JLabel("单位"), JLabel("单位"), JLabel("单位"), JLabel("单位"))
    )

    val txtfield = arrayOf(JTextField(28), JTextField(28), JTextField(28), JTextField(28))
    val jbtn = JButton("确定")

    private val cmb: JComboBox<String> = JComboBox<String>()

    override fun actionPerformed(e: ActionEvent?) {


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

        jp.add(title_, XYConstraints(5, 15, 80, 30))

        label.forEach {
            it.forEachIndexed { index, it ->
                jp.add(it, XYConstraints(5, 55 + index * 40, 75, 30))
            }
        }
        label2.forEach {
            it.forEachIndexed { index, it ->
                jp.add(it, XYConstraints(155, 95 + index * 40, 30, 30))
            }
        }

        txtfield.forEachIndexed { index, it ->
            jp.add(it, XYConstraints(80, 95 + index * 40, 75, 30))
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
        txtfield[3].isVisible = false//默认关闭

        str.forEach { cmb_.addItem(it) }
        jp.add(cmb_, XYConstraints(80, 15, 100, 30))
        cmb_.addItemListener(ItemListener { e ->
            if (e.stateChange == ItemEvent.SELECTED) {
                select = if (cmb_.selectedIndex == 1) 1 else 0
                label.forEach { it.forEach { it.isVisible = false } }
                label[select * 2 + select2].forEach { it.isVisible = true }
                label2.forEach { it.forEach { it.isVisible = false } }
                label2[select * 2 + select2].forEach { it.isVisible = true }
                txtfield.forEach { it.isVisible = true }
                txtfield[3].isVisible = cmb_.selectedIndex == 1
            }
        })


        windowDoorData.houseData?.allToString()?.forEach { cmb.addItem(it) }
        jp.add(cmb, XYConstraints(80, 55, 100, 30))
        cmb.addItemListener(ItemListener { e ->
            if (e.stateChange == ItemEvent.SELECTED) {
                val strArr = cmb.selectedItem.toString().split(",")
                select2 = if (strArr[1] == "2") 1 else 0
                label.forEach { it.forEach { it.isVisible = false } }
                label[select * 2 + select2].forEach { it.isVisible = true }
                label2.forEach { it.forEach { it.isVisible = false } }
                label2[select * 2 + select2].forEach { it.isVisible = true }
            }
        })

        cmb_.selectedIndex = strArr[2].toInt()
        cmb.selectedItem = windowDoorData.houseData?.let { Select(it, strArr[1].toInt()) }

        jp.add(jbtn, XYConstraints(90, 400, 60, 30))
        jbtn.addActionListener(ActionListener {
            val a = cmb_.selectedIndex
            val b = cmb.selectedItem.toString().substring(0, cmb.selectedItem.toString().indexOf(',')).toInt()
            var str = strArr[0] + "," + b + "," + a
            txtfield.forEach { str += "," + if (it.text == "") "0.0" else it.text }
            WDD_Update(
                windowDoorData, i, str
            )
            this.dispose()
        })
    }
}
