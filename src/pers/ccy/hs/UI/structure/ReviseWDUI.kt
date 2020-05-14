package pers.ccy.hs.UI.structure

import com.borland.jbcl.layout.XYConstraints
import com.borland.jbcl.layout.XYLayout
import pers.ccy.hs.data.HouseData
import pers.ccy.hs.data.WindowDoorData
import pers.ccy.hs.operation.OpStructure.RemoveWD
import pers.ccy.hs.operation.OpStructure.Select
import pers.ccy.hs.operation.OpStructure.SelectWD
import pers.ccy.hs.operation.OpStructure.WDD_Update
import java.awt.Dialog
import java.awt.event.*
import javax.swing.*


class ReviseWDUI(houseData: HouseData, i: Int, Wid: Int) : JDialog(), ActionListener {
    private val jp = JPanel()
    private val str_result = SelectWD(houseData, i)
    private val strArr = str_result.split(",")
    private var select = 0
    private var select2 = 0
    private val title_ = JLabel("选择构造种类：")
    private val str = arrayOf(
        "门", "窗"
    )
    val cmb_: JComboBox<String> = JComboBox<String>()

    val label = arrayOf(
        arrayOf(JLabel("门所在墙："), JLabel("门的厚度："), JLabel("离墙边距离："), JLabel("门宽度："), JLabel("门高度：")),
        arrayOf(JLabel("门所在墙："), JLabel("门的厚度："), JLabel("离墙边角度："), JLabel("门角度："), JLabel("门高度：")),
        arrayOf(
            JLabel("窗户所在墙："),
            JLabel("窗户的厚度："),
            JLabel("离墙边距离："),
            JLabel("窗户宽度："),
            JLabel("窗户高度："),
            JLabel("离地高度：")
        ),
        arrayOf(
            JLabel("窗户所在墙："), JLabel("窗户的厚度："), JLabel("离墙边角度："), JLabel("窗户角度："), JLabel("窗户高度："), JLabel("离地高度：")
        )
    )
    val label2 = arrayOf(
        arrayOf(JLabel("分米"), JLabel("分米"), JLabel("分米"), JLabel("分米")),
        arrayOf(JLabel("分米"), JLabel("度"), JLabel("度"), JLabel("分米")),
        arrayOf(JLabel("分米"), JLabel("分米"), JLabel("分米"), JLabel("分米"), JLabel("分米")),
        arrayOf(JLabel("分米"), JLabel("度"), JLabel("度"), JLabel("分米"), JLabel("分米"))
    )

    val txtfield = arrayOf(JTextField(28), JTextField(28), JTextField(28), JTextField(28), JTextField(28))
    val jbtn = JButton("确定")

    private val cmb: JComboBox<String> = JComboBox<String>()

    override fun actionPerformed(e: ActionEvent?) {}

    init {
        this.setBounds(100, 100, 300, 500)
        this.modalityType = Dialog.ModalityType.APPLICATION_MODAL
        this.title = "修改"
        this.isResizable = true
        this.defaultCloseOperation = DISPOSE_ON_CLOSE
        this.add(jp)

        jp.setBounds(100, 100, 300, 500)
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
            it.text = strArr[2 + index]//if (strArr[3 + index] == "0.0") "" else strArr[3 + index]
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
        txtfield[4].isVisible = false//默认关闭

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
                txtfield[4].isVisible = cmb_.selectedIndex == 1
            }
        })


        houseData.allToString()?.forEach { cmb.addItem(it) }
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

        cmb_.selectedIndex = strArr[1].toInt()
        cmb.selectedItem = houseData.let { Select(it, Wid) }

        jp.add(jbtn, XYConstraints(90, 400, 60, 30))
        jbtn.addActionListener(ActionListener {
            val a = cmb_.selectedIndex
            val widnow =
                cmb.selectedItem.toString().indexOf(',')?.let { it1 -> cmb.selectedItem.toString().substring(0, it1) }
                    .toInt()
            var str = strArr[0] + "," + a
            txtfield.forEach { str += "," + if (it.text == "") "0.0" else it.text }
            if (widnow == Wid) {
                WDD_Update(
                    houseData, i, str
                )
            }else{
                RemoveWD(houseData,i)
                houseData.Search(widnow).Add(WindowDoorData(str))
            }
            this.dispose()
        })
    }
}
