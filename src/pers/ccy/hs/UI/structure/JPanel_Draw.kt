package pers.ccy.hs.UI.structure

import pers.ccy.hs.data.HouseData
import pers.ccy.hs.data.Point
import pers.ccy.hs.operation.OpCombination.VectorAngle
import pers.ccy.hs.operation.OpStructure.CCircle
import pers.ccy.hs.operation.OpStructure.Collision
import pers.ccy.hs.operation.OpStructure.Remove
import pers.ccy.hs.operation.OpStructure.getDis
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.geom.Path2D
import javax.swing.BorderFactory
import javax.swing.JOptionPane
import javax.swing.JPanel
import kotlin.math.*


class JPanel_Draw(val houseData: HouseData, val w: Int, val h: Int) : JPanel() {

    val w_mid = w / 2
    val h_mid = h / 2
    override fun paintComponent(graphics: Graphics) {
        val graphics = graphics as Graphics2D
        /*Graphics 类是所有图形上下文的抽象基类，允许应用程序在组件（已经在各种设备上实现）以及闭屏图像上进行绘制。
        此 Graphics2D 类扩展 Graphics 类，以提供对几何形状、坐标转换、颜色管理和文本布局更为复杂的控制。它是用于在
        Java(tm) 平台上呈现二维形状、文本和图像的基础类。
        基本上用Graphics 就够平时绘图用了*/

        val l = arrayListOf<Array<Double>>()
        l.add(arrayOf(w_mid.toDouble(), h_mid.toDouble(), w_mid.toDouble(), h_mid.toDouble()))
        var len = 0
        var hd: HouseData? = houseData
        graphics.drawLine(l[len][0].toInt(), l[len][1].toInt(), l[len][2].toInt(), l[len][3].toInt())

        loop@ while (hd != null) {
            graphics.color = Color.BLACK
            when (hd.type) {
                0 -> {
                    //println("${l[len][0].toInt()-245}, ${l[len][1].toInt()-165}, ${l[len][2].toInt()-245}, ${l[len][3].toInt()-165}")

                    if (hd.next == null) break@loop
                    hd = hd.next!!
                    continue@loop
                }
                1 -> {
                    when (hd.type2) {
                        0 -> {
                            val angle = ((180 - hd.info) / 180) * PI
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
                        2 -> l.add(
                            arrayOf(
                                l[len][2],
                                l[len][3],
                                w_mid + hd.info,
                                h_mid - hd.info2,
                                hd.id.toDouble()
                            )
                        )
                        3 -> l.add(
                            arrayOf(
                                l[len][2],
                                l[len][3],
                                l[len][2] + hd.info,
                                l[len][3] - hd.info2,
                                hd.id.toDouble()
                            )
                        )
                    }
                    len++
                    graphics.drawLine(l[len][0].toInt(), l[len][1].toInt(), l[len][2].toInt(), l[len][3].toInt())
                    /*println("${l[len][0]}, ${l[len][1]}, ${l[len][2]}, ${l[len][3]}")
                    println("${l[len][0].toInt()}, ${l[len][1].toInt()}, ${l[len][2].toInt()}, ${l[len][3].toInt()}")
                    println("${l[len][0].toInt()-245}, ${l[len][1].toInt()-165}, ${l[len][2].toInt()-245}, ${l[len][3].toInt()-165}")*/

                    //门窗
                    var wd = hd.windowDoorData
                    graphics.color = Color.RED
                    while (wd != null) {
                        if (wd.info + wd.info2 > getDis(l[len][0], l[len][1], l[len][2], l[len][3])) {
                        }
                        graphics.color = when (wd.type) {
                            0 -> Color.RED
                            1 -> Color.BLUE
                            else -> Color.BLACK
                        }
                        val outline: Path2D = Path2D.Float()
                        val angle = atan2(l[len][1] - l[len][3], l[len][0] - l[len][2])
                        //先前后偏移，再两侧偏移
                        //graphics.drawLine(l[len][0].toInt(), l[len][1].toInt(),(l[len][0]-10*sin(angle)).toInt(), (l[len][1]+10*cos(angle)).toInt())
                        outline.moveTo(
                            l[len][0] - wd.info * cos(angle) - 2 * sin(angle),
                            l[len][1] - wd.info * sin(angle) + 2 * cos(angle)
                        )
                        outline.lineTo(
                            l[len][0] - wd.info * cos(angle) + 2 * sin(angle),
                            l[len][1] - wd.info * sin(angle) - 2 * cos(angle)
                        )
                        outline.lineTo(
                            l[len][0] - (wd.info + wd.info2) * cos(angle) + 2 * sin(angle),
                            l[len][1] - (wd.info + wd.info2) * sin(angle) - 2 * cos(angle)
                        )
                        outline.lineTo(
                            l[len][0] - (wd.info + wd.info2) * cos(angle) - 2 * sin(angle),
                            l[len][1] - (wd.info + wd.info2) * sin(angle) + 2 * cos(angle)
                        )
                        outline.closePath()
                        graphics.fill(outline)

                        wd = wd.next
                    }
                }
                2 -> {
                    var rx = .0
                    var ry = .0
                    var angle1 = .0
                    var angle2 = .0
                    when (hd.type2) {
                        //https://www.cnblogs.com/fengliu-/p/10944151.html 在平面中，一个点绕任意点旋转θ度后的点的坐标
                        0 -> {
                            val angle = ((180 - hd.info) / 180) * PI
                            val x = sin(angle) * hd.info2
                            val y = cos(angle) * hd.info2
                            rx = l[len][2] + x
                            ry = l[len][3] + y
                            val angle_hd = hd.info3 / 180 * PI
                            val xx = (l[len][2] - rx) * cos(angle_hd) - (l[len][3] - ry) * sin(angle_hd) + rx
                            val yy = (l[len][2] - rx) * sin(angle_hd) + (l[len][3] - ry) * cos(angle_hd) + ry
                            l.add(arrayOf(l[len][2], l[len][3], xx, yy, hd.id.toDouble()))
                            angle2 = -hd.info3
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
                            val angle_hd = hd.info3 / 180 * PI
                            val xx = (l[len][2] - rx) * cos(angle_hd) - (l[len][3] - ry) * sin(angle_hd) + rx
                            val yy = (l[len][2] - rx) * sin(angle_hd) + (l[len][3] - ry) * cos(angle_hd) + ry
                            l.add(arrayOf(l[len][2], l[len][3], xx, yy, hd.id.toDouble()))
                            angle2 = -hd.info3
                        }
                        2 -> {
                            //https://blog.csdn.net/u011030529/article/details/84779566 三点确定一个圆的计算方法
                            val xy = CCircle(
                                l[len][2],
                                l[len][3],
                                w_mid + hd.info,
                                h_mid - hd.info2,
                                w_mid + hd.info3,
                                h_mid - hd.info4
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
                            val angle = VectorAngle(
                                Point(l[len][2] - (w_mid + hd.info), l[len][3] - (h_mid - hd.info2)),
                                Point(l[len][2] - (w_mid + hd.info3), l[len][3] - (h_mid - hd.info4))
                            )
                            angle1 = 90 + atan2((rx - l[len][2]), (ry - l[len][3])) * 180 / PI
                            if (angle1 < 0) angle1 += 360
                            val anglet = 90 + atan2((rx - (w_mid + hd.info3)), (ry - (h_mid - hd.info4))) * 180 / PI
                            if (anglet < 0) angle2 += 360
                            angle2 = anglet - angle1
                            if (angle2 * angle > 0)
                                angle2 = if (angle2 > 0) angle2 - 360
                                else angle2 + 360
                        }
                    }
                    len++
                    val d =
                        sqrt(((rx - l[len][0]) * (rx - l[len][0]) + (ry - l[len][1]) * (ry - l[len][1])))
                    angle1 = 90 + atan2((rx - l[len][0]), (ry - l[len][1])) * 180 / PI
                    if (angle1 < 0) angle1 += 360
                    //https://blog.csdn.net/wangbowj123/article/details/72785849 JAVA 基本绘图——利用JFrame JPanel 绘制扇形
                    //graphics.drawArc(x：圆心-width/2, y：圆心-hight/2, width：x轴直径, hight：y轴直径, startAngle：启示角度（x轴正半轴方向为0）, arcAngle：扫过角度（逆时针为正）)
                    graphics.drawArc(
                        (rx - d).toInt(),
                        (ry - d).toInt(),
                        (d * 2).toInt(),
                        (d * 2).toInt(),
                        angle1.toInt(),
                        angle2.toInt()
                    )

                    /*println("${l[len][0]}, ${l[len][1]}, ${l[len][2]}, ${l[len][3]}")
                    println("${l[len][0].toInt()}, ${l[len][1].toInt()}, ${l[len][2].toInt()}, ${l[len][3].toInt()}")
                    println("${l[len][0].toInt()-245}, ${l[len][1].toInt()-165}, ${l[len][2].toInt()-245}, ${l[len][3].toInt()-165}")*/
                    //println("rx=${rx},ry=${ry},d=$d,angle1=${angle1},angle2=${angle2}")
                    //始末点
                    //graphics.drawLine(l[len][0].toInt(), l[len][1].toInt(), l[len][2].toInt(), l[len][3].toInt())
                    //圆心
                    //graphics.color = Color.GREEN
                    //graphics.drawLine(l[len][0].toInt(), l[len][1].toInt(), rx.toInt(), ry.toInt())

                    //门窗
                    var wd = hd.windowDoorData
                    graphics.color = Color.RED
                    while (wd != null) {
                        graphics.color = when (wd.type) {
                            0 -> Color.RED
                            1 -> Color.BLUE
                            else -> Color.BLACK
                        }
                        wd = wd.next
                    }
                }
                else -> {
                }
            }
            hd = hd.next

        }
        val collision = Collision(l)
        if (collision > 0) {
            val worr = JOptionPane.showOptionDialog(
                this,
                "目前线段和id为${collision}的线段冲突",
                "线段冲突",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null, arrayOf("删除", "取消"), "删除"
            )
            if (worr == 0) Remove(houseData)
            houseData.UpdatModel()
            this.parent.repaint()
        }
        //闭合
        graphics.color = Color.GREEN
        graphics.drawLine(w_mid, h_mid, l[len][2].toInt(), l[len][3].toInt())
        /*graphics.color = Color.RED
        graphics.fill3DRect(a, b, 100, 100, true)
        a+=10
        b+=10*/
    }

    init {
        this.layout = null
        this.border = (BorderFactory.createTitledBorder("房间俯视图"))
    }
}