package pers.ccy.hs.UI.combination

import pers.ccy.hs.data.CombinationData
import pers.ccy.hs.data.HouseData
import pers.ccy.hs.data.Point
import pers.ccy.hs.operation.OpCombination
import pers.ccy.hs.operation.OpStructure.CCircle
import pers.ccy.hs.operation.OpStructure.getDis
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.geom.Path2D
import javax.swing.BorderFactory
import javax.swing.JPanel
import kotlin.math.*


class CombinationDraw(var combinationData: ArrayList<CombinationData>, val w: Int, val h: Int, val p: Double) :
    JPanel() {

    private val wmid = w / 2.0
    private val hmid = h / 2.0
    override fun paintComponent(graphics: Graphics) {
        val graphics = graphics as Graphics2D
        /*Graphics 类是所有图形上下文的抽象基类，允许应用程序在组件（已经在各种设备上实现）以及闭屏图像上进行绘制。
        此 Graphics2D 类扩展 Graphics 类，以提供对几何形状、坐标转换、颜色管理和文本布局更为复杂的控制。它是用于在
        Java(tm) 平台上呈现二维形状、文本和图像的基础类。
        基本上用Graphics 就够平时绘图用了*/
        combinationData.forEach {
            val l = arrayListOf<Array<Double>>()
            val w_mid = wmid + it.start.x * p
            val h_mid = hmid + it.start.y * p
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
            graphics.drawLine(l[len][0].toInt(), l[len][1].toInt(), l[len][2].toInt(), l[len][3].toInt())
            loop@ while (hd != null) {
                graphics.color = Color.BLACK
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
                                val x = sin(angle) * hd.info2 * p
                                val y = cos(angle) * hd.info2 * p
                                l.add(arrayOf(l[len][2], l[len][3], l[len][2] + x, l[len][3] + y, hd.id.toDouble()))
                            }
                            1 -> {
                                val a = (l[len][3] - l[len][1]).toDouble()
                                val b = (l[len][2] - l[len][0]).toDouble()
                                var angle = PI / 2 - atan2(a, b)
                                if (a == .0 && b == .0) angle = ((180 - hd.info) / 180) * PI
                                else angle -= ((180 - hd.info) / 180) * PI
                                val x = (sin(angle) * hd.info2 * p)
                                val y = (cos(angle) * hd.info2 * p)
                                l.add(arrayOf(l[len][2], l[len][3], l[len][2] + x, l[len][3] + y, hd.id.toDouble()))
                            }
                            2 -> {
                                val a = hd.info * p
                                val b = hd.info2 * p
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
                                val a = hd.info * p
                                val b = hd.info2 * p
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
                        graphics.drawLine(
                            l[len][0].toInt(),
                            l[len][1].toInt(),
                            l[len][2].toInt(),
                            l[len][3].toInt()
                        )

                        //门窗
                        var wd = hd.windowDoorData
                        graphics.color = Color.RED
                        while (wd != null) {
                            val outline: Path2D = Path2D.Float()
                            val angle = atan2(l[len][1] - l[len][3], l[len][0] - l[len][2])
                            //先前后偏移，再两侧偏移
                            //graphics.drawLine(l[len][0].toInt(), l[len][1].toInt(),(l[len][0]-10*sin(angle)).toInt(), (l[len][1]+10*cos(angle)).toInt())
                            if (wd.info + wd.info2 > getDis(l[len][0], l[len][1], l[len][2], l[len][3])) {
                            }
                            when (wd.type) {
                                0 -> {
                                    graphics.color = Color.RED
                                    val d = it.selectDoor(wd.id)
                                    var a = d.point[0]!!.x * p
                                    var b = d.point[0]!!.y * p
                                    var xx =
                                        (a) * cos(it.angle) - (b) * sin(it.angle)
                                    var yy =
                                        (a) * sin(it.angle) + (b) * cos(it.angle)
                                    outline.moveTo(
                                        w_mid + xx,
                                        h_mid + yy
                                    )
                                    a = d.point[1]!!.x * p
                                    b = d.point[1]!!.y * p
                                    xx = (a) * cos(it.angle) - (b) * sin(it.angle)
                                    yy = (a) * sin(it.angle) + (b) * cos(it.angle)
                                    outline.lineTo(
                                        w_mid + xx,
                                        h_mid + yy
                                    )
                                    a = d.point[3]!!.x * p
                                    b = d.point[3]!!.y * p
                                    xx = (a) * cos(it.angle) - (b) * sin(it.angle)
                                    yy = (a) * sin(it.angle) + (b) * cos(it.angle)
                                    outline.lineTo(
                                        w_mid + xx,
                                        h_mid + yy
                                    )
                                    a = d.point[2]!!.x * p
                                    b = d.point[2]!!.y * p
                                    xx = (a) * cos(it.angle) - (b) * sin(it.angle)
                                    yy = (a) * sin(it.angle) + (b) * cos(it.angle)
                                    outline.lineTo(
                                        w_mid + xx,
                                        h_mid + yy
                                    )
                                    outline.closePath()
                                    graphics.fill(outline)
                                }
                                1 -> {
                                    graphics.color = Color.BLUE
                                    outline.moveTo(
                                        l[len][0] - wd.info * p * cos(angle) - 2 * p * sin(angle),
                                        l[len][1] - wd.info * p * sin(angle) + 2 * p * cos(angle)
                                    )
                                    outline.lineTo(
                                        l[len][0] - wd.info * p * cos(angle) + 2 * p * sin(angle),
                                        l[len][1] - wd.info * p * sin(angle) - 2 * p * cos(angle)
                                    )
                                    outline.lineTo(
                                        l[len][0] - (wd.info * p + wd.info2 * p) * cos(angle) + 2 * p * sin(angle),
                                        l[len][1] - (wd.info * p + wd.info2 * p) * sin(angle) - 2 * p * cos(angle)
                                    )
                                    outline.lineTo(
                                        l[len][0] - (wd.info * p + wd.info2 * p) * cos(angle) - 2 * p * sin(angle),
                                        l[len][1] - (wd.info * p + wd.info2 * p) * sin(angle) + 2 * p * cos(angle)
                                    )
                                    outline.closePath()
                                    graphics.fill(outline)
                                }
                                else -> graphics.color = Color.BLACK
                            }
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
                                val angle = ((180 - hd.info) / 180) * PI - it.angle
                                val x = sin(angle) * hd.info2 * p
                                val y = cos(angle) * hd.info2 * p
                                rx = l[len][2] + x
                                ry = l[len][3] + y
                                val angle_hd = hd.info3 / 180 * PI
                                val xx = (l[len][2] - rx) * cos(angle_hd) - (l[len][3] - ry) * sin(angle_hd) + rx
                                val yy = (l[len][2] - rx) * sin(angle_hd) + (l[len][3] - ry) * cos(angle_hd) + ry
                                l.add(arrayOf(l[len][2], l[len][3], xx, yy, hd.id.toDouble()))
                                angle2= -hd.info3
                            }
                            1 -> {
                                val a = (l[len][3] - l[len][1]).toDouble()
                                val b = (l[len][2] - l[len][0]).toDouble()
                                var angle = PI / 2 - atan2(a, b)
                                if (a == .0 && b == .0) angle = ((180 - hd.info) / 180) * PI
                                else angle -= ((180 - hd.info) / 180) * PI
                                val x = (sin(angle) * hd.info2 * p)
                                val y = (cos(angle) * hd.info2 * p)
                                rx = l[len][2] + x
                                ry = l[len][3] + y
                                val angle_hd = hd.info3 / 180 * PI
                                val xx = (l[len][2] - rx) * cos(angle_hd) - (l[len][3] - ry) * sin(angle_hd) + rx
                                val yy = (l[len][2] - rx) * sin(angle_hd) + (l[len][3] - ry) * cos(angle_hd) + ry
                                l.add(arrayOf(l[len][2], l[len][3], xx, yy, hd.id.toDouble()))
                                angle2= -hd.info3
                            }
                            2 -> {
                                //https://blog.csdn.net/u011030529/article/details/84779566 三点确定一个圆的计算方法
                                val a = hd.info * p
                                val b = hd.info2 * p
                                val xx =
                                    (a) * cos(-it.angle) - (b) * sin(-it.angle)
                                val yy =
                                    (a) * sin(-it.angle) + (b) * cos(-it.angle)
                                val a2 = hd.info3 * p
                                val b2 = hd.info4 * p
                                val xx2 =
                                    (a) * cos(-it.angle) - (b) * sin(-it.angle)
                                val yy2 =
                                    (a) * sin(-it.angle) + (b) * cos(-it.angle)
                                val xy = CCircle(
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
                                        (w_mid + hd.info3 * p),
                                        (h_mid - hd.info4 * p),
                                        hd.id.toDouble()
                                    )
                                )
                                val angle = OpCombination.VectorAngle(
                                    Point(l[len][2] - (w_mid + hd.info * p), l[len][3] - (h_mid - hd.info2 * p)),
                                    Point(l[len][2] - (w_mid + hd.info3 * p), l[len][3] - (h_mid - hd.info4 * p))
                                )
                                angle1 = 90 + atan2((rx - l[len][2]), (ry - l[len][3])) * 180 / PI
                                if (angle1 < 0) angle1 += 360
                                val anglet =
                                    90 + atan2((rx - (w_mid + hd.info3 * p)), (ry - (h_mid - hd.info4 * p))) * 180 / PI
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
            //闭合
            graphics.color = Color.BLACK
            graphics.drawLine(w_mid.toInt(), h_mid.toInt(), l[len][2].toInt(), l[len][3].toInt())
            /*graphics.color = Color.RED
        graphics.fill3DRect(a, b, 100, 100, true)
        a+=10
        b+=10*/
        }

    }

    init {
        this.layout = null
        this.border = (BorderFactory.createTitledBorder("房间组合俯视图"))
    }
}