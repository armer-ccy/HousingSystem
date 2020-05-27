package pers.ccy.hs.UI.output

import com.borland.jbcl.layout.XYConstraints
import com.borland.jbcl.layout.XYLayout
import pers.ccy.hs.data.*
import pers.ccy.hs.operation.*
import pers.ccy.hs.operation.OpFile.importToSTL
import java.awt.Dialog
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.*
import kotlin.math.*

class OutputUI() : JDialog(), ActionListener {
    var stl = STLFile()
    var combinationData = ArrayList<CombinationData>()
    private val jp = JPanel()
    var jfc = JFileChooser() // 文件选择器
    val label = arrayOf(
        JLabel("门窗样式："), JLabel("房间高度：")
    )
    val txtfield = arrayOf(JTextField(28))
    val label2 = arrayOf(JLabel("分米"))
    val jbtn = JButton("确定")
    val cmb: JComboBox<String> = JComboBox<String>()

    override fun actionPerformed(e: ActionEvent?) {}

    init {
        jfc.fileSelectionMode = 0 // 设定只能选择到文件
        jfc.removeChoosableFileFilter(jfc.acceptAllFileFilter) // 不显示“所有文件”
        jfc.fileFilter = MyFileFilter("房间组合文件(*.HSASM)", ".HSASM")
        jfc.dialogTitle = "请选择一个房屋生成建模"

        val state = jfc.showOpenDialog(null)
        if (state == 1) {
            dispose()
        } else {
            importToSTL(jfc.selectedFile, combinationData)
            UI()
        }
    }

    fun UI() {
        this.setBounds(100, 100, 300, 500)
        this.modalityType = Dialog.ModalityType.APPLICATION_MODAL
        this.title = "请设置生成建模的一些参数"
        this.isResizable = true
        this.defaultCloseOperation = DISPOSE_ON_CLOSE
        this.add(jp)

        jp.setBounds(100, 100, 300, 500)
        jp.layout = XYLayout()
        jp.isVisible = true

        label.forEachIndexed { index, it ->
            jp.add(it, XYConstraints(5, 55 + index * 40, 75, 30))
        }
        var model = DefaultComboBoxModel<String>()
        model.addAll(listOf("空", "一", "十"))
        cmb.model = model
        cmb.selectedIndex = 0
        jp.add(cmb, XYConstraints(80, 55, 100, 30))

        label2.forEachIndexed { index, it ->
            jp.add(it, XYConstraints(155, 95 + index * 40, 30, 30))
        }

        txtfield.forEachIndexed { index, it ->
            jp.add(it, XYConstraints(80, 95 + index * 40, 75, 30))
            it.addKeyListener(object : KeyListener {
                override fun keyTyped(e: KeyEvent?) {
                    val keyChar = e!!.keyChar
                    if (keyChar in '0'..'9' || keyChar.toInt() == 8) {
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

        jp.add(jbtn, XYConstraints(90, 400, 60, 30))
        jbtn.addActionListener(ActionListener {
            txtfield[0].text.toDoubleOrNull()?.let { it1 ->
                jfc.fileSelectionMode = 0 // 设定只能选择到文件
                jfc.removeChoosableFileFilter(jfc.acceptAllFileFilter) // 不显示“所有文件”
                jfc.fileFilter = MyFileFilter("STL", ".STL")
                jfc.dialogTitle = "保存为STL"
                val state = jfc.showSaveDialog(null)
                if (state == 1) {
                } else {
                    val ends = (jfc.fileFilter as MyFileFilter).gets()
                    val stlPath = if (jfc.selectedFile.absolutePath.toUpperCase().endsWith(ends.toUpperCase())) {
                        // 如果文件是以选定扩展名结束的，则使用原名
                        jfc.selectedFile.absolutePath
                    } else {
                        // 否则加上选定的扩展名
                        jfc.selectedFile.absolutePath + ends
                    }
                    Op(it1)
                    OutputAsSTL(stl, stlPath)
                    dispose()

                }
            }
        })
    }

    fun Op(high: Double) {
        combinationData.forEach {
            val l = arrayListOf<Array<Double>>()
            val w_mid = it.start.x
            val h_mid = it.start.y
            l.add(
                arrayOf(
                    w_mid.toDouble() - 1 * sin(it.angle),
                    h_mid.toDouble() + 1 * cos(it.angle),
                    w_mid.toDouble(),
                    h_mid.toDouble()
                )
            )
            var len = 0
            var hd: HouseData? = it.houseData
            loop@ while (hd != null) {
                when (hd.type) {
                    0 -> {
                        if (hd.next == null) break@loop
                        hd = hd.next!!
                        continue@loop
                    }
                    1 -> {
                        when (hd.type2) {
                            0 -> {
                                val angle = ((180 - hd.info) / 180) * PI - it.angle
                                val x = sin(angle) * hd.info2
                                val y = cos(angle) * hd.info2
                                l.add(arrayOf(l[len][2], l[len][3], l[len][2] + x, l[len][3] + y, hd.id.toDouble()))
                            }
                            1 -> {
                                val a = (l[len][3] - l[len][1]).toDouble()
                                val b = (l[len][2] - l[len][0]).toDouble()
                                var angle = PI / 2 - atan2(a, b)
                                if (a == .0 && b == .0) angle = ((180 - hd.info) / 180) * PI
                                else angle -= ((180 - hd.info) / 180) * PI
                                val x = (sin(angle) * hd.info2)
                                val y = (cos(angle) * hd.info2)
                                l.add(arrayOf(l[len][2], l[len][3], l[len][2] + x, l[len][3] + y, hd.id.toDouble()))
                            }
                            2 -> {
                                val a = hd.info
                                val b = hd.info2
                                val xx =
                                    (a) * cos(-it.angle) - (b) * sin(-it.angle)
                                val yy =
                                    (a) * sin(-it.angle) + (b) * cos(-it.angle)
                                l.add(
                                    arrayOf(
                                        l[len][2],
                                        l[len][3],
                                        w_mid + xx,
                                        h_mid - yy,
                                        hd.id.toDouble()
                                    )
                                )
                            }
                            3 -> {
                                val a = hd.info
                                val b = hd.info2
                                val xx =
                                    (a) * cos(-it.angle) - (b) * sin(-it.angle)
                                val yy =
                                    (a) * sin(-it.angle) + (b) * cos(-it.angle)
                                l.add(
                                    arrayOf(
                                        l[len][2],
                                        l[len][3],
                                        l[len][2] + xx,
                                        l[len][3] - yy,
                                        hd.id.toDouble()
                                    )
                                )
                            }
                        }
                        len++
                        var last = Point(l[len][0], l[len][1])
                        //门窗
                        var wd = hd.windowDoorData
                        while (wd != null) {
                            val angle = atan2(l[len][1] - l[len][3], l[len][0] - l[len][2])
                            //先前后偏移，再两侧偏移
                            //graphics.drawLine(l[len][0].toInt(), l[len][1].toInt(),(l[len][0]-10*sin(angle)).toInt(), (l[len][1]+10*cos(angle)).toInt()
                            when (wd.type) {
                                0 -> {
                                    val d = it.selectDoor(wd.id)
                                    var a = d.point[0]!!.x
                                    var b = d.point[0]!!.y
                                    var xx =
                                        (a) * cos(it.angle) - (b) * sin(it.angle)
                                    var yy =
                                        (a) * sin(it.angle) + (b) * cos(it.angle)
                                    stl.add(last.x, last.y, w_mid + xx, h_mid + yy, .0, high)
                                    last = (Point(w_mid + xx, h_mid + yy))
                                    a = d.point[2]!!.x
                                    b = d.point[2]!!.y
                                    xx = (a) * cos(it.angle) - (b) * sin(it.angle)
                                    yy = (a) * sin(it.angle) + (b) * cos(it.angle)
                                    stl.add(last.x, last.y, w_mid + xx, h_mid + yy, .0, high)

                                    a = d.point[1]!!.x
                                    b = d.point[1]!!.y
                                    xx = (a) * cos(it.angle) - (b) * sin(it.angle)
                                    yy = (a) * sin(it.angle) + (b) * cos(it.angle)
                                    if (high > wd.info3) stl.add(last.x, last.y, w_mid + xx, h_mid + yy, wd.info3, high)
                                    //样式
                                    stl.addStyle(
                                        cmb.selectedIndex,
                                        last.x,
                                        last.y,
                                        w_mid + xx,
                                        h_mid + yy,
                                        .0,
                                        wd.info3
                                    )
                                    last = (Point(w_mid + xx, h_mid + yy))

                                    a = d.point[3]!!.x
                                    b = d.point[3]!!.y
                                    xx = (a) * cos(it.angle) - (b) * sin(it.angle)
                                    yy = (a) * sin(it.angle) + (b) * cos(it.angle)
                                    stl.add(last.x, last.y, w_mid + xx, h_mid + yy, .0, high)
                                }
                                1 -> {
                                    stl.add(
                                        last.x,
                                        last.y,
                                        l[len][0] - wd.info * cos(angle),
                                        l[len][1] - wd.info * sin(angle),
                                        .0,
                                        high
                                    )
                                    last = (Point(l[len][0] - wd.info * cos(angle), l[len][1] - wd.info * sin(angle)))

                                    stl.add(
                                        last.x,
                                        last.y,
                                        l[len][0] - (wd.info + wd.info2) * cos(angle),
                                        l[len][1] - (wd.info + wd.info2) * sin(angle),
                                        .0,
                                        wd.info3
                                    )
                                    //样式
                                    if (high < wd.info3 + wd.info4)
                                        stl.addStyle(
                                            cmb.selectedIndex,
                                            last.x,
                                            last.y,
                                            l[len][0] - (wd.info + wd.info2) * cos(angle),
                                            l[len][1] - (wd.info + wd.info2) * sin(angle),
                                            wd.info3,
                                            high
                                        )
                                    else
                                        stl.addStyle(
                                            cmb.selectedIndex,
                                            last.x,
                                            last.y,
                                            l[len][0] - (wd.info + wd.info2) * cos(angle),
                                            l[len][1] - (wd.info + wd.info2) * sin(angle),
                                            wd.info3,
                                            wd.info3 + wd.info
                                        )
                                    if (high > wd.info3 + wd.info4)
                                        stl.add(
                                            last.x,
                                            last.y,
                                            l[len][0] - (wd.info + wd.info2) * cos(angle),
                                            l[len][1] - (wd.info + wd.info2) * sin(angle),
                                            wd.info3 + wd.info4,
                                            high
                                        )
                                    last = (Point(
                                        l[len][0] - (wd.info + wd.info2) * cos(angle),
                                        l[len][1] - (wd.info + wd.info2) * sin(angle)
                                    ))
                                }
                                else -> {
                                }
                            }
                            wd = wd.next
                        }
                        stl.add(last.x, last.y, l[len][2], l[len][3], .0, high)
                    }
                    2 -> {
                        var rx = .0
                        var ry = .0
                        var last = Point(l[len][2], l[len][3])
                        when (hd.type2) {
                            //https://www.cnblogs.com/fengliu-/p/10944151.html 在平面中，一个点绕任意点旋转θ度后的点的坐标
                            0 -> {
                                val angle = ((180 - hd.info) / 180) * PI - it.angle
                                val x = sin(angle) * hd.info2
                                val y = cos(angle) * hd.info2
                                rx = l[len][2] + x
                                ry = l[len][3] + y
                                var angle_hd = hd.info3 / 180 * PI
                                var xx = (l[len][2] - rx) * cos(angle_hd) - (l[len][3] - ry) * sin(angle_hd) + rx
                                var yy = (l[len][2] - rx) * sin(angle_hd) + (l[len][3] - ry) * cos(angle_hd) + ry
                                l.add(arrayOf(l[len][2], l[len][3], xx, yy, hd.id.toDouble()))
                                for (i in 1..60) {
                                    angle_hd = hd.info3 / 60 / 180 * PI
                                    xx = (last.x - rx) * cos(angle_hd) - (last.y - ry) * sin(angle_hd) + rx
                                    yy = (last.x - rx) * sin(angle_hd) + (last.y - ry) * cos(angle_hd) + ry
                                    stl.add(last.x, last.y, xx, yy, .0, high)
                                    last = Point(xx, yy)
                                }
                            }
                            1 -> {
                                val a = (l[len][3] - l[len][1]).toDouble()
                                val b = (l[len][2] - l[len][0]).toDouble()
                                var angle = PI / 2 - atan2(a, b)
                                if (a == .0 && b == .0) angle = ((180 - hd.info) / 180) * PI
                                else angle -= ((180 - hd.info) / 180) * PI
                                val x = (sin(angle) * hd.info2)
                                val y = (cos(angle) * hd.info2)
                                rx = l[len][2] + x
                                ry = l[len][3] + y
                                var angle_hd = hd.info3 / 180 * PI
                                var xx = (l[len][2] - rx) * cos(angle_hd) - (l[len][3] - ry) * sin(angle_hd) + rx
                                var yy = (l[len][2] - rx) * sin(angle_hd) + (l[len][3] - ry) * cos(angle_hd) + ry
                                l.add(arrayOf(l[len][2], l[len][3], xx, yy, hd.id.toDouble()))
                                for (i in 1..60) {
                                    angle_hd = hd.info3 / 60 / 180 * PI
                                    xx = (last.x - rx) * cos(angle_hd) - (last.y - ry) * sin(angle_hd) + rx
                                    yy = (last.x - rx) * sin(angle_hd) + (last.y - ry) * cos(angle_hd) + ry
                                    stl.add(last.x, last.y, xx, yy, .0, high)
                                    last = Point(xx, yy)
                                }
                            }
                            2 -> {
                                //https://blog.csdn.net/u011030529/article/details/84779566 三点确定一个圆的计算方法
                                val a = hd.info
                                val b = hd.info2
                                val xx =
                                    (a) * cos(-it.angle) - (b) * sin(-it.angle)
                                val yy =
                                    (a) * sin(-it.angle) + (b) * cos(-it.angle)
                                val a2 = hd.info3
                                val b2 = hd.info4
                                val xx2 =
                                    (a) * cos(-it.angle) - (b) * sin(-it.angle)
                                val yy2 =
                                    (a) * sin(-it.angle) + (b) * cos(-it.angle)
                                val xy = OpStructure.CCircle(
                                    l[len][2],
                                    l[len][3],
                                    w_mid + xx,
                                    h_mid - yy,
                                    w_mid + xx2,
                                    h_mid - yy2
                                )
                                rx = xy[0]
                                ry = xy[1]
                                l.add(
                                    arrayOf(
                                        l[len][2],
                                        l[len][3],
                                        (w_mid + hd.info3),
                                        (h_mid - hd.info4),
                                        hd.id.toDouble()
                                    )
                                )
                            }
                        }
                        len++
                    }
                    else -> {
                    }
                }
                hd = hd.next

            }
            //闭合
            stl.add(w_mid, h_mid, l[len][2], l[len][3], .0, high)
        }
    }
}