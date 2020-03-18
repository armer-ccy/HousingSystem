package pers.ccy.hs.test

import com.borland.jbcl.layout.XYConstraints
import com.borland.jbcl.layout.XYLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*
import javax.swing.WindowConstants.EXIT_ON_CLOSE


class KMain2 :  ActionListener {
    private val jf = JFrame()
    private val jp1 = JPanel()
    private val jp2 = JPanel()
    private val jp3 = JPanel()
    private val jp4 = JPanel()

    private val jrba1 = arrayOf(
        JRadioButton("房间构造"), JRadioButton("房间组合")
    )
    private val bg1 = ButtonGroup()
    private val jrba2 = arrayOf(
        JRadioButton("直线"), JRadioButton("曲线")
    )
    private val bg2 = ButtonGroup()
    private val jrba3 = arrayOf(
        JRadioButton("26~35岁"), JRadioButton("36~45岁")
    )
    var label1 = JLabel("证件类型：")

    var cmb: JComboBox<String> = JComboBox<String>()

    var txtfield1 = JTextField() //创建文本框


    override fun actionPerformed(e: ActionEvent) {
        if (jrba1.get(1).isSelected) {
            jp2.isVisible = false
            jp3.isVisible = true
        } else {
            jp2.isVisible = true
            jp3.isVisible = false
        }
        /*if (e.source === jba[1]) {
            for (i in 0 until jcba.length) {
                jcba.get(i).setSelected(false)
                jtf.text = ""
            }
        } else {
            val temp1 = StringBuffer("你是一个")
            val temp2 = StringBuffer()
            for (i in 0..4) {
                if (jrba.get(i).isSelected()) {
                    temp1.append(jrba.get(i).getText())
                }
                if (jcba.get(i).isSelected()) {
                    temp2.append(jcba.get(i).getText().toString() + ".")
                }
            }
            if (temp2.length == 0) {
                jtf.text = "爱好为空？？？"
            } else {
                temp1.append("的人，比较喜欢")
                temp1.append(temp2.substring(0, temp2.length - 1))
                jtf.text = temp1.append("。").toString()
            }
        }*/
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            KMain2()
        }
    }

    init {
        //jp1
        jp1.layout = null
        jp1.border = (BorderFactory.createTitledBorder("选择工作内容"))
        //jp1.border = (BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("选择工作内容"),BorderFactory.createLineBorder(Color.blue, 2)))
        //jp1.isVisible = true
        jrba1.get(0).isSelected = true
        jrba1.forEachIndexed { index, item ->  item.setBounds(5 + index * 100, 15, 80, 30)}
        jrba1.forEach { jp1.add(it) }
        jrba1.forEach { it.addActionListener(this) }
        jrba1.forEach { bg1.add(it) }

        //jp2
        jp2.layout = null
        jp2.border = (BorderFactory.createTitledBorder("选择构造线形"))
        //jp2.isVisible = false
        jrba2.get(0).isSelected = true
        jrba2.forEachIndexed { index, item ->  item.setBounds(5 + index * 100, 15, 80, 30)}
        jrba2.forEach { jp2.add(it) }
        jrba2.forEach { it.addActionListener(this) }
        jrba2.forEach { bg2.add(it) }

        //jp3
        jp3.layout = null
        jp3.border = (BorderFactory.createTitledBorder("选择"))
        //jp3.isVisible = true
        /*jrba3.forEachIndexed { index, item ->  item.setBounds(40 + index * 120, 40, 80, 30)}
        jrba3.forEach { jp3.add(it) }
        jrba3.forEach { it.addActionListener(this) }*/
        //jrba3.forEach { bg.add(it) }

        //cmd
        cmb.addItem("--请选择--");    //向下拉列表中添加一项
        cmb.addItem("身份证");
        cmb.addItem("驾驶证");
        cmb.addItem("军官证");
        label1.setBounds(5,15,75,30)
        jp3.add(label1)
        cmb.setBounds(80,15,75,30)
        jp3.add(cmb)

        //jp4
        txtfield1.setText("普通文本框")

        //jf
        jf.layout = XYLayout()
        jf.add(jp1,XYConstraints(0,0,200,50))
        jf.add(jp2,XYConstraints(0,50,200,50))
        jf.add(jp3,XYConstraints(0,100,200,50))
        jf.title = "构造一个房间"
        jf.setBounds(100, 100, 700, 280)
        jf.isVisible = true
        jf.isResizable = true
        jf.defaultCloseOperation = EXIT_ON_CLOSE
    }
}