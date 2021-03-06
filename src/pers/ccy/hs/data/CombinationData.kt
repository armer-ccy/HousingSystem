package pers.ccy.hs.data

import pers.ccy.hs.operation.OpCombination.isIpsilateral
import pers.ccy.hs.operation.OpStructure
import java.io.File
import javax.swing.DefaultComboBoxModel
import javax.swing.DefaultListModel
import kotlin.math.*

class CombinationData(var houseData: HouseData) {
    var door: ArrayList<Door> = ArrayList()	//门的链表
    lateinit var center: Point				//房间中心点
    var id = 0							    //房间索引
    var name = ""							//房间的名字
    var path = ""							//房间的储存路径
    var angle = .0							//房间倾斜角度
    var start = Point(.0, .0)			//房间的原点所在
    var connect = ""						//房间与总房间的连接信息

    companion object {
        val model = DefaultListModel<String>()
        val modelCB = DefaultComboBoxModel<String>()
        val modelNew = DefaultComboBoxModel<String>()
    }

    init {
        //得到房间
        var hd: HouseData? = houseData

        //求门
        val l = arrayListOf<Array<Double>>()
        l.add(arrayOf(.0, .0, .0, .0))
        var len = 0
        loop@ while (hd != null) {
            when (hd.type) {
                0 -> {
                    hd = hd.next
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
                                hd.info,
                                hd.info2,
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

                    //门
                    var wd = hd.windowDoorData
                    while (wd != null) {
                        if (wd.type == 0) {
                            val angle = atan2(l[len][1] - l[len][3], l[len][0] - l[len][2])
                            door.add(
                                Door(
                                    wd.id, wd.thick, wd.info2, angle,
                                    Point(
                                        l[len][0] - wd.info * cos(angle),
                                        l[len][1] - wd.info * sin(angle)
                                    ),
                                    Point(
                                        l[len][0] - (wd.info + wd.info2) * cos(angle),
                                        l[len][1] - (wd.info + wd.info2) * sin(angle)
                                    )
                                )
                            )
                        }
                        wd = wd.next
                    }
                }
                2 -> {
                    var rx = .0
                    var ry = .0
                    when (hd.type2) {
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
                        }
                        2 -> {
                            l.add(
                                arrayOf(
                                    l[len][2],
                                    l[len][3],
                                    hd.info3,
                                    hd.info4,
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

        //求中心center
        //https://www.cnblogs.com/ffgcc/p/10546437.html
        var x = .0
        var y = .0
        if (l[2][2] != l[1][0]) {
            val k = (l[2][3] - l[1][1]) / (l[2][2] - l[1][0])
            x = (k * k * l[1][0] + k * (l[1][3] - l[1][1]) + l[1][2]) / (k * k + 1)
            y = k * (x - l[1][0]) + l[1][1]
        } else {
            x = l[1][0]
            y = l[1][3]
        }
        center = Point(x, y)

        //求门剩下的两个点
        door.forEach {
            var point = arrayOfNulls<Point>(2)
            val angle = atan2(it.point[0]!!.y - it.point[1]!!.y, it.point[0]!!.x - it.point[1]!!.x)
            //https://zhidao.baidu.com/question/983851560834754739.html
            //判断两点是处于直线异侧还是同侧？
            var p = Point(
                it.point[0]!!.x - it.thick * sin(angle),
                it.point[0]!!.y + it.thick * cos(angle)
            )
            if (!isIpsilateral(it.point[0]!!, it.point[1]!!, p, center)) {
                it.addlast(
                    Point(
                        it.point[0]!!.x + it.thick * sin(angle),
                        it.point[0]!!.y - it.thick * cos(angle)
                    ),
                    Point(
                        it.point[1]!!.x + it.thick * sin(angle),
                        it.point[1]!!.y - it.thick * cos(angle)
                    )
                )
            } else {
                it.addlast(
                    Point(
                        it.point[0]!!.x - it.thick * sin(angle),
                        it.point[0]!!.y + it.thick * cos(angle)
                    ),
                    Point(
                        it.point[1]!!.x - it.thick * sin(angle),
                        it.point[1]!!.y + it.thick * cos(angle)
                    )
                )
            }
        }
    }

    constructor(houseData: HouseData, file: File) : this(houseData) {
        //得到名字，地址
        name = file.name
        path = file.toString()
    }

    fun UpdatModel() {
        model.addElement("$name,id:$id")
        door.forEach {
            if (!it.isused) modelCB.addElement(
                //"第${id}个房间${it.id}号门"
                "$id,${it.id}"
            )
        }
    }

    fun UpdatCBModel() {
        CombinationData.modelNew.removeAllElements()
        door.forEach {
            modelNew.addElement("${it.id}号门")
        }
    }

    fun toPrint() {
        println("路径：" + path)
        println("中心点：" + center.x + "，" + center.y)
        houseData.allToPrint()
        println()
    }

    fun selectDoor(id: Int): Door {
        door.forEach { if (it.id == id) return it }
        return door[0]
    }

    fun toSave(): String {
        var str = "<CombinationData id=\"$id\">\n" +
                "<data>\n" + houseData.allToSave() + "</data>\n" +
                "<configure>" +
                "<path>$path</path>\n" +
                "<name>$name</name>\n" +
                "<connect>$connect</connect>\n" +
                "</configure>\n" +
                "</CombinationData>\n\n"
        return str
    }
}