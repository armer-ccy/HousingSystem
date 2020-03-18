package pers.ccy.hs.test

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*
import com.borland.jbcl.layout.*;
import javax.swing.WindowConstants.EXIT_ON_CLOSE

class UI_test :  ActionListener {
    private val jf = JFrame()
    private val jp1 = JPanel()
    private val jp2 = JPanel()
    private val jp3 = JPanel()

    private val jrba1 = arrayOf(
        JRadioButton("a"), JRadioButton("b")
    )
    private val jrba2 = arrayOf(
        JRadioButton("5~15岁"), JRadioButton("16~35岁")
    )
    private val jrba3 = arrayOf(
        JRadioButton("26~35岁"), JRadioButton("36~45岁")
    )
    private val bg = ButtonGroup()

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
        jf.layout = XYLayout()
        jp1.layout = null
        jp2.layout = null
        jp3.layout = null
        jrba1.get(0).isSelected = true
        jrba1.forEachIndexed { index, item ->  item.setBounds(40 + index * 100, 40, 80, 30)}
        jrba2.forEachIndexed { index, item ->  item.setBounds(40 + index * 120, 40, 80, 30)}
        jrba3.forEachIndexed { index, item ->  item.setBounds(40 + index * 120, 40, 80, 30)}
        jrba1.forEach { jp1.add(it) }
        jrba2.forEach { jp2.add(it) }
        jrba3.forEach { jp3.add(it) }
        jrba1.forEach { it.addActionListener(this) }
        jrba2.forEach { it.addActionListener(this) }
        jrba3.forEach { it.addActionListener(this) }
        jrba1.forEach { bg.add(it) }
        //jrba2.forEach { bg.add(it) }
        //jrba3.forEach { bg.add(it) }

        jf.add(jp1,XYConstraints(0,0,200,200))
        jf.add(jp2,XYConstraints(0,200,400,200))
        jf.add(jp3,XYConstraints(0,400,200,200))
        jf.title = "食物调查表"
        jf.setBounds(100, 100, 700, 280)
        jf.isVisible = true
        //jp1.isVisible = true
        //jp2.isVisible = false
        //jp3.isVisible = true
        jf.isResizable = true
        jf.defaultCloseOperation = EXIT_ON_CLOSE
    }
}