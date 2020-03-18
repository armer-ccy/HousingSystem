package pers.ccy.hs.operation

import pers.ccy.hs.data.HouseData
import pers.ccy.hs.data.WindowDoorData
import java.lang.Math.*
import javax.swing.JPanel
import kotlin.math.pow

object Op {
    fun DisplayHiding(i: Int, vararg args: JPanel) {
        for (j in 0..args.size - 1)
            args[j].isVisible = false
        args[i].isVisible = true
        //args[i].repaint()
    }

    fun getHouseDataNumber(houseData: HouseData): Int {
        var houseData = houseData
        var js = 1
        while (houseData.next != null) {
            js++
            houseData = houseData.next!!
        }
        return js
    }

    fun Collision(l: ArrayList<Array<Double>>): Int {
        var bj = 0
        var len = 1
        val z = l[l.size - 1]
        while (len < l.size - 1 && bj == 0) {
            val a = l[len]
            bj = if (Collision_StraightLine(a, z)) 0 else l[len][4].toInt()
            len++
        }
        return bj
    }

    fun Collision_StraightLine(a: Array<Double>, z: Array<Double>): Boolean {
        //https://www.cnblogs.com/tuyang1129/p/9390376.html 判断两条线段是否相交—（向量叉乘）
        var bj = true
        val ab = arrayOf(z[0] - z[2], z[1] - z[3])
        val ac = arrayOf(z[0] - a[0], z[1] - a[1])
        val ad = arrayOf(z[0] - a[2], z[1] - a[3])
        val cd = arrayOf(a[0] - a[2], a[1] - a[3])
        val ca = arrayOf(a[0] - z[0], a[1] - z[1])
        val cb = arrayOf(a[0] - z[2], a[1] - z[3])
        val abc = CrossProduct(ab[0], ab[1], ac[0], ac[1])
        val abd = CrossProduct(ab[0], ab[1], ad[0], ad[1])
        val cda = CrossProduct(cd[0], cd[1], ca[0], ca[1])
        val cdb = CrossProduct(cd[0], cd[1], cb[0], cb[1])
        val n0 = BToI(abc == .0) + BToI(abd == .0) + BToI(cda == .0) + BToI(cdb == .0)
        if (n0 > 2) {
            val z_max = max(z[0], z[2])
            val z_min = min(z[0], z[2])
            val a_max = max(a[0], a[2])
            val a_min = min(a[0], a[2])
            if ((a_min < z_max && z_max < a_max) || (a_min < z_min && z_min < a_max) || (z_min < a_max && a_max < z_max) || (z_min < a_min && a_min < z_max)) {

                bj = false
                println("aaa")
            }
        } else if (abc * abd < 0 && cda * cdb < 0) {
            bj = false
        }
        return bj
    }

    fun CrossProduct(x1: Double, y1: Double, x2: Double, y2: Double): Double = x1 * y2 - y1 * x2 //向量叉乘

    fun BToI(b: Boolean): Int = if (b) 1 else 0

    fun CCircle(x1: Double, y1: Double, x2: Double, y2: Double, x3: Double, y3: Double): Array<Double> {
        val a = x1 - x2
        val b = y1 - y2
        val c = x1 - x3
        val d = y1 - y3
        val e = (x1 * x1 - x2 * x2 + (y1 * y1 - y2 * y2)) / 2.0
        val f = (x1 * x1 - x3 * x3 + (y1 * y1 - y3 * y3)) / 2.0
        val det = b * c - a * d
        if (abs(det) < 1e-5) {
            return arrayOf(.0, .0)
        }
        val x0 = -(d * e - b * f) / det
        val y0 = -(a * f - c * e) / det
        return arrayOf(x0, y0)
    }

    fun Remove(houseData: HouseData) {
        var hd = houseData
        while (hd.next != null) {
            if (hd.next!!.next == null) {
                houseData.windowDoorData?.let { Remove_idl(it, hd.next!!.id) }
                hd.next = null
                return
            }
            hd = hd.next!!
        }
    }

    fun Remove(houseData: HouseData, i: Int) {
        var hd = houseData
        houseData.windowDoorData?.let { Remove_idl(it, i) }
        while (hd.next != null) {
            if (hd.next!!.id == i) {
                var t = hd.next
                hd.next = hd.next!!.next
                t = null
                return
            }
            hd = hd.next!!
        }
    }

    fun MoveUp(houseData: HouseData, i: Int) {
        var hd = houseData
        var hd_old = hd
        if (hd.next != null && hd.next!!.id == i) return
        while (hd.next != null) {
            if (hd.next!!.id == i) {
                var t = hd.next!!.next
                hd.next!!.next = hd
                hd_old.next = hd.next
                hd.next = t
                return
            }
            hd_old = hd
            hd = hd.next!!
        }
    }

    fun MoveDown(houseData: HouseData, i: Int) {
        var hd = houseData
        while (hd.next != null) {
            if (hd.next!!.id == i && hd.next!!.next != null) {
                var t = hd.next
                hd.next = t!!.next
                t.next = hd.next!!.next
                hd.next!!.next = t
                return
            }
            hd = hd.next!!
        }
    }

    fun Select(houseData: HouseData, i: Int): String {
        var hd = houseData
        while (hd.next != null) {
            if (hd.next!!.id == i) {
                return hd.next.toString()
            }
            hd = hd.next!!
        }
        return ""
    }

    fun HD_Update(houseData: HouseData, i: Int, str: String) {
        var hd = houseData
        val strArr = str.split(",")
        while (hd.next != null) {
            if (hd.next!!.id == i) {
                hd.next!!.type = strArr[1].toInt()
                hd.next!!.type2 = strArr[2].toInt()
                hd.next!!.info = strArr[3].toDouble()
                hd.next!!.info2 = strArr[4].toDouble()
                hd.next!!.info3 = strArr[5].toDouble()
                hd.next!!.info4 = strArr[6].toDouble()
            }
            hd = hd.next!!
        }
    }

    fun Add(houseData: HouseData, add: HouseData) {
        var now = houseData
        while (now.next != null) now = now.next!!
        now.next = add
    }

    fun RemoveAll(houseData: HouseData) {
        while (houseData.next != null) Remove(houseData)
    }

    fun Remove(windowDoorData: WindowDoorData) {
        var wdd = windowDoorData
        while (wdd.next != null) {
            if (wdd.next!!.next == null) {
                wdd.next = null
                return
            }
            wdd = wdd.next!!
        }
    }

    fun Remove(windowDoorData: WindowDoorData, i: Int) {
        var wdd = windowDoorData
        while (wdd.next != null) {
            if (wdd.next!!.id == i) {
                var t = wdd.next
                wdd.next = wdd.next!!.next
                t = null
                return
            }
            wdd = wdd.next!!
        }
    }

    fun Remove_idl(windowDoorData: WindowDoorData, i: Int) {
        var wdd = windowDoorData
        while (wdd.next != null) {
            if (wdd.next!!.id_l == i) {
                var t = wdd.next
                wdd.next = wdd.next!!.next
                t = null
                continue
            }
            wdd = wdd.next!!
        }
    }

    fun Select(windowDoorData: WindowDoorData, i: Int): String {
        var wdd = windowDoorData
        while (wdd.next != null) {
            if (wdd.next!!.id == i) {
                return wdd.next.toString()
            }
            wdd = wdd.next!!
        }
        return ""
    }

    fun WDD_Update(windowDoorData: WindowDoorData, i: Int, str: String) {
        var wdd = windowDoorData
        val strArr = str.split(",")
        while (wdd.next != null) {
            if (wdd.next!!.id == i) {
                wdd.next!!.id_l = strArr[1].toInt()
                wdd.next!!.type = strArr[2].toInt()
                wdd.next!!.info = strArr[3].toDouble()
                wdd.next!!.info2 = strArr[4].toDouble()
                wdd.next!!.info3 = strArr[5].toDouble()
                wdd.next!!.info4 = strArr[6].toDouble()
            }
            wdd = wdd.next!!
        }
    }

    fun Add(windowDoorData: WindowDoorData, add: WindowDoorData) {
        var now = windowDoorData
        while (now.next != null) now = now.next!!
        now.next = add
    }

    fun RemoveAll(windowDoorData: WindowDoorData) {
        while (windowDoorData.next != null) Remove(windowDoorData)
    }

    fun getDis(x1: Double, y1: Double, x2: Double, y2: Double): Double =
        kotlin.math.sqrt((x1 - x2).pow(2) + (y1 - y2).pow(2))
}